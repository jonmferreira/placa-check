package com.placacheck

import android.graphics.Bitmap

/**
 * Resultado de uma leitura: o número lido e a menor confiança
 * entre todos os caracteres (0.0 a 1.0). Se qualquer dígito veio
 * duvidoso, [confiancaMinima] cai — é esse valor que dispara o "CONFERIR".
 */
data class Leitura(
    val texto: String,
    val confiancaMinima: Float,
)

/**
 * Contrato do reconhecedor. Hoje é ML Kit (sem treino).
 * Amanhã pode ser Tesseract fine-tuned ou TFLite embutido no APK,
 * sem mexer no resto do app — basta trocar a implementação.
 */
interface Reconhecedor {
    suspend fun ler(bitmap: Bitmap): Leitura?
}
