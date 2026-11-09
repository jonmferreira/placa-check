package com.placacheck.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.placacheck.Leitura
import com.placacheck.Reconhecedor
import kotlinx.coroutines.tasks.await

/**
 * Fase 1 (POC, sem treino): ML Kit on-device, grátis, nativo Android.
 *
 * Serve pra validar o fluxo e, principalmente, pra COLETAR as fotos reais
 * na condição da linha de montagem. Vai errar em alguns DPM (número a laser
 * em metal) — e tudo bem: o Verificador manda pro humano quando a confiança
 * cai. Na fase 2 trocamos por um modelo treinado nas fotos reais.
 */
class ReconhecedorMLKit : Reconhecedor {

    private val recognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun ler(bitmap: Bitmap): Leitura? {
        val entrada = InputImage.fromBitmap(bitmap, 0)
        val texto = recognizer.process(entrada).await()

        // Só interessam os dígitos. Juntamos todos os símbolos numéricos
        // e guardamos a MENOR confiança vista — é o elo mais fraco da leitura.
        val digitos = StringBuilder()
        var menorConfianca = 1.0f
        var achouAlgo = false

        for (bloco in texto.textBlocks) {
            for (linha in bloco.lines) {
                for (elemento in linha.elements) {
                    val apenasNumeros = elemento.text.filter { it.isDigit() }
                    if (apenasNumeros.isEmpty()) continue
                    digitos.append(apenasNumeros)
                    achouAlgo = true
                    // confidence pode vir null em alguns devices → trata como baixo
                    val c = elemento.confidence ?: 0f
                    if (c < menorConfianca) menorConfianca = c
                }
            }
        }

        if (!achouAlgo) return null
        return Leitura(texto = digitos.toString(), confiancaMinima = menorConfianca)
    }
}
