package com.placacheck

import android.content.Context
import android.graphics.Bitmap
import com.placacheck.logica.Veredito
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Log de análises em CSV, no diretório do app no device (sem permissão de
 * armazenamento — é escopo do app, dá pra puxar via adb/exportação depois).
 * Uma linha por análise salva.
 *
 * Fotos: só são gravadas em caso de INCONSISTÊNCIA (evidência). Em consistência
 * o chamador passa null e nenhuma imagem vai pro disco — não há por que guardar
 * foto de um acerto.
 */
object Registro {

    private val DATA = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val NOME = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    private const val CABECALHO =
        "timestamp,operador,moto,moto_conf,chassi,chassi_conf,resultado,motivo,foto_moto,foto_chassi"

    fun salvar(
        ctx: Context,
        operador: String,
        moto: Leitura?,
        chassi: Leitura?,
        v: Veredito,
        fotoMoto: Bitmap?,
        fotoChassi: Bitmap?,
    ): String {
        val dir = ctx.getExternalFilesDir(null) ?: ctx.filesDir
        val csv = File(dir, "analises.csv")
        val novo = !csv.exists()
        val carimbo = NOME.format(Date())

        val nomeMoto = fotoMoto?.let { salvarFoto(dir, "${carimbo}_moto", it) } ?: ""
        val nomeChassi = fotoChassi?.let { salvarFoto(dir, "${carimbo}_chassi", it) } ?: ""

        val linha = listOf(
            DATA.format(Date()),
            operador,
            moto?.texto ?: "",
            pct(moto),
            chassi?.texto ?: "",
            pct(chassi),
            v.resultado.name,
            v.motivo.replace(",", ";"),
            nomeMoto,
            nomeChassi,
        ).joinToString(",")

        csv.appendText((if (novo) "$CABECALHO\n" else "") + linha + "\n")
        return csv.absolutePath
    }

    private fun salvarFoto(dir: File, nome: String, bmp: Bitmap): String {
        val fotos = File(dir, "fotos").apply { mkdirs() }
        val arq = File(fotos, "$nome.jpg")
        FileOutputStream(arq).use { bmp.compress(Bitmap.CompressFormat.JPEG, 85, it) }
        return "fotos/${arq.name}"
    }

    private fun pct(l: Leitura?): String =
        l?.let { "${(it.confiancaMinima * 100).toInt()}%" } ?: ""
}
