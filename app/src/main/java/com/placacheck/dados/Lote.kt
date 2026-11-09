package com.placacheck.dados

/**
 * Lista do lote cadastrada ANTES da chegada das motos (ex.: uma semana antes).
 *
 * Mock por enquanto. No produto isso vira: importar CSV/planilha do PCP,
 * ou sincronizar com o sistema da fábrica (tudo on-device, sem nuvem).
 * Troque os números abaixo por alguns chassis reais pra demonstrar pro chefe.
 */
object Lote {

    val NUMEROS: Set<String> = setOf(
        "12345678",
        "23456789",
        "34567890",
        "45678901",
        "56789012",
    )
}
