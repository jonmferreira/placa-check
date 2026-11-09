package com.placacheck

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.placacheck.dados.Lote
import com.placacheck.databinding.ActivityMainBinding
import com.placacheck.logica.Resultado
import com.placacheck.logica.Veredito
import com.placacheck.logica.Verificador
import com.placacheck.ocr.ReconhecedorMLKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

/**
 * Fluxo automático, sem toque:
 *   aponta p/ a MOTO → leitura estável ~2s → trava (check sonoro/vibra) → vai p/ o CHASSI
 *   → leitura estável ~2s → trava → compara → MATCH / Inconsistência (com % de precisão).
 *
 * Viewfinder pequeno no topo; o foco é o painel. Cada linha (motor/chassi) mostra
 * o valor lido + % de certeza + botão ↻ pra refazer SÓ aquele item. No resultado,
 * as duas fotos aparecem lado a lado (zoom na placa). Salvar grava o log (CSV) e,
 * só em caso de inconsistência, as fotos de evidência; depois limpa pra próxima.
 * Tudo on-device: nenhuma imagem sai do aparelho sozinha.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var ui: ActivityMainBinding
    private lateinit var feedback: Feedback
    private val reconhecedor = ReconhecedorMLKit()
    private val verificador = Verificador(lote = Lote.NUMEROS)

    private enum class Item { MOTO, CHASSI }
    private enum class Etapa { MOTO, CHASSI, RESULTADO }

    private var etapa = Etapa.MOTO
    private var leituraMoto: Leitura? = null
    private var leituraChassi: Leitura? = null
    private var fotoMoto: Bitmap? = null
    private var fotoChassi: Bitmap? = null

    // Leitura automática: o mesmo número precisa se manter por [estavelMs] pra travar.
    private val estavelMs = 2000L
    private var candidato: String? = null
    private var candidatoDesde = 0L
    private var travadoAte = 0L
    @Volatile private var ocupado = false

    private val exec = Executors.newSingleThreadExecutor()

    private val pedirCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { ok -> if (ok) iniciarCamera() else semPermissao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        feedback = Feedback(this)

        ui.config.setOnClickListener { abrirConfig() }
        ui.refazerMoto.setOnClickListener { refazer(Item.MOTO) }
        ui.refazerChassi.setOnClickListener { refazer(Item.CHASSI) }
        ui.salvar.setOnClickListener { salvarAnalise() }

        reiniciar()
        if (temCamera()) iniciarCamera()
        else pedirCamera.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        exec.shutdown()
    }

    private fun temCamera() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED

    private fun iniciarCamera() {
        val futuro = ProcessCameraProvider.getInstance(this)
        futuro.addListener({
            val provider = futuro.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(ui.preview.surfaceProvider)
            }
            val analise = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { it.setAnalyzer(exec) { img -> analisar(img) } }

            provider.unbindAll()
            provider.bindToLifecycle(
                this, CameraSelector.DEFAULT_BACK_CAMERA, preview, analise
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun analisar(img: ImageProxy) {
        if (ocupado || etapa == Etapa.RESULTADO ||
            SystemClock.elapsedRealtime() < travadoAte) {
            img.close(); return
        }
        ocupado = true
        val bitmap = paraBitmap(img)
        img.close()
        lifecycleScope.launch {
            val leitura = withContext(Dispatchers.Default) { reconhecedor.ler(bitmap) }
            avaliar(leitura, bitmap)
            ocupado = false
        }
    }

    /** Estabilidade: trava quando o mesmo número se mantém por [estavelMs]. */
    private fun avaliar(leitura: Leitura?, frame: Bitmap) {
        val texto = leitura?.texto
        if (texto.isNullOrBlank()) {
            candidato = null
            ui.dica.text = "procurando o número…"
            return
        }
        val agora = SystemClock.elapsedRealtime()
        if (texto != candidato) {
            candidato = texto
            candidatoDesde = agora
            ui.dica.text = "lendo… $texto"
            return
        }
        if (agora - candidatoDesde >= estavelMs) travar(leitura!!, frame)
    }

    private fun travar(leitura: Leitura, frame: Bitmap) {
        candidato = null
        travadoAte = SystemClock.elapsedRealtime() + 1200L   // cooldown entre etapas
        feedback.check()                                      // som OU vibração
        val foto = recorteCentral(frame)                      // "tira a foto", zoom na placa
        when (etapa) {
            Etapa.MOTO -> { leituraMoto = leitura; fotoMoto = foto; preencher(Item.MOTO, leitura); proximaEtapa() }
            Etapa.CHASSI -> { leituraChassi = leitura; fotoChassi = foto; preencher(Item.CHASSI, leitura); proximaEtapa() }
            Etapa.RESULTADO -> {}
        }
    }

    /** Depois de travar um item: se o outro já existe → veredito; senão → lê o que falta. */
    private fun proximaEtapa() {
        if (leituraMoto != null && leituraChassi != null) {
            etapa = Etapa.RESULTADO; veredito()
        } else if (leituraMoto == null) {
            etapa = Etapa.MOTO; ui.titulo.text = "Aponte para a MOTO"
        } else {
            etapa = Etapa.CHASSI; ui.titulo.text = "Agora o CHASSI"
        }
    }

    private fun preencher(item: Item, leitura: Leitura) {
        val pct = (leitura.confiancaMinima * 100).toInt()
        if (item == Item.MOTO) {
            ui.valorMoto.text = leitura.texto
            ui.pctMoto.text = "$pct%"
            ui.valorMoto.setTextColor(Color.WHITE)
        } else {
            ui.valorChassi.text = leitura.texto
            ui.pctChassi.text = "$pct%"
            ui.valorChassi.setTextColor(Color.WHITE)
        }
    }

    private fun veredito() {
        val v: Veredito = verificador.verificar(leituraMoto, leituraChassi)
        val match = v.resultado == Resultado.MATCH
        val conf = minOf(
            leituraMoto?.confiancaMinima ?: 0f,
            leituraChassi?.confiancaMinima ?: 0f
        )
        val pct = (conf * 100).toInt()
        pintar(if (match) VERDE else VERMELHO)
        if (match) {
            ui.titulo.text = "Consistente"
            ui.dica.text = "precisão $pct% · salve ou toque para o próximo"
        } else {
            ui.titulo.text = "Inconsistência identificada"
            ui.dica.text = "$pct% de precisão · ${v.motivo}"
            if ((leituraMoto?.texto ?: "") != (leituraChassi?.texto ?: "")) {
                ui.valorChassi.setTextColor(Color.parseColor("#FF8A80"))
            }
        }
        // mostra as duas fotos lado a lado (zoom na placa) sempre que há resultado
        ui.imgMoto.setImageBitmap(fotoMoto)
        ui.imgChassi.setImageBitmap(fotoChassi)
        ui.fotos.visibility = View.VISIBLE
    }

    /** Refaz só um item; o fluxo volta a ler o que ficou faltando e reavalia. */
    private fun refazer(item: Item) {
        if (item == Item.MOTO) {
            leituraMoto = null; fotoMoto = null
            ui.valorMoto.text = "—"; ui.pctMoto.text = ""
            ui.valorMoto.setTextColor(Color.WHITE)
        } else {
            leituraChassi = null; fotoChassi = null
            ui.valorChassi.text = "—"; ui.pctChassi.text = ""
            ui.valorChassi.setTextColor(Color.WHITE)
        }
        candidato = null
        travadoAte = SystemClock.elapsedRealtime() + 600L
        ui.fotos.visibility = View.GONE
        pintar(NEUTRO)
        proximaEtapa()
    }

    private fun salvarAnalise() {
        if (leituraMoto == null || leituraChassi == null) {
            Toast.makeText(this, "Conclua a leitura da moto e do chassi.", Toast.LENGTH_SHORT).show()
            return
        }
        val v = verificador.verificar(leituraMoto, leituraChassi)
        val inconsistente = v.resultado == Resultado.CONFERIR
        // fotos só viram evidência em caso de inconsistência
        Registro.salvar(
            this, Config.operador(this), leituraMoto, leituraChassi, v,
            if (inconsistente) fotoMoto else null,
            if (inconsistente) fotoChassi else null
        )
        Toast.makeText(
            this,
            if (inconsistente) "Inconsistência salva com as fotos." else "Análise salva.",
            Toast.LENGTH_SHORT
        ).show()
        reiniciar()
    }

    private fun reiniciar() {
        etapa = Etapa.MOTO
        leituraMoto = null; leituraChassi = null
        fotoMoto = null; fotoChassi = null; candidato = null
        ui.valorMoto.text = "—"; ui.valorChassi.text = "—"
        ui.pctMoto.text = ""; ui.pctChassi.text = ""
        ui.valorMoto.setTextColor(Color.WHITE); ui.valorChassi.setTextColor(Color.WHITE)
        ui.imgMoto.setImageBitmap(null); ui.imgChassi.setImageBitmap(null)
        ui.fotos.visibility = View.GONE
        ui.titulo.text = "Aponte para a placa da MOTO"
        ui.dica.text = ""
        pintar(NEUTRO)
    }

    private fun abrirConfig() {
        val som = Config.sinal(this) == Config.Sinal.SOM
        val itens = arrayOf(
            (if (som) "✓ " else "") + "Som (beep)",
            (if (!som) "✓ " else "") + "Vibração",
            "Operador: ${Config.operador(this)}"
        )
        AlertDialog.Builder(this)
            .setTitle("Configurações")
            .setItems(itens) { _, which ->
                when (which) {
                    0 -> { Config.setSinal(this, Config.Sinal.SOM); feedback.check() }
                    1 -> { Config.setSinal(this, Config.Sinal.VIBRACAO); feedback.check() }
                    2 -> pedirOperador()
                }
            }
            .show()
    }

    private fun pedirOperador() {
        val campo = EditText(this).apply {
            setText(Config.operador(this@MainActivity))
            hint = "nome do operador"
        }
        AlertDialog.Builder(this)
            .setTitle("Operador")
            .setView(campo)
            .setPositiveButton("Salvar") { _, _ -> Config.setOperador(this, campo.text.toString()) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /** "Zoom na placa": recorte central do frame (o operador centraliza a placa no alvo). */
    private fun recorteCentral(bmp: Bitmap): Bitmap {
        val w = bmp.width; val h = bmp.height
        var cw = (w * 0.8f).toInt()
        var ch = (cw / 3f).toInt()
        if (ch > h) { ch = (h * 0.6f).toInt(); cw = minOf(w, ch * 3) }
        val x = ((w - cw) / 2).coerceAtLeast(0)
        val y = ((h - ch) / 2).coerceAtLeast(0)
        return Bitmap.createBitmap(bmp, x, y, cw, ch)
    }

    private fun paraBitmap(img: ImageProxy): Bitmap {
        val bmp = img.toBitmap()
        val rot = img.imageInfo.rotationDegrees
        if (rot == 0) return bmp
        val m = Matrix().apply { postRotate(rot.toFloat()) }
        return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true)
    }

    private fun pintar(cor: Int) { ui.painel.setBackgroundColor(cor) }

    private fun semPermissao() {
        pintar(VERMELHO)
        ui.titulo.text = "Preciso da câmera"
        ui.dica.text = "Autorize nas configurações do app."
    }

    private companion object {
        val NEUTRO = Color.parseColor("#1E2A30")
        val VERDE = Color.parseColor("#1B5E20")
        val VERMELHO = Color.parseColor("#B71C1C")
    }
}
