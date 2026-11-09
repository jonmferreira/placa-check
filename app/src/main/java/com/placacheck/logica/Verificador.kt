package com.placacheck.logica

import com.placacheck.Leitura

enum class Resultado {
    /** Pode seguir: as três condições bateram com folga. Tela verde. */
    MATCH,

    /** Algo ficou duvidoso. Humano decide. Tela vermelha "CONFERIR". */
    CONFERIR,
}

data class Veredito(
    val resultado: Resultado,
    val numeroMoto: String,
    val numeroChassi: String,
    val motivo: String,
)

/**
 * O coração do co-piloto. Não é um "leitor automático" — é um filtro
 * de segurança. Só libera (MATCH) quando TODAS as três são verdade:
 *
 *   1. moto == chassi            (as duas gravações a laser conferem)
 *   2. número está no lote        (lista pré-cadastrada da semana)
 *   3. confiança >= limiar        (nenhum dígito veio pela metade)
 *
 * Qualquer uma falhando → CONFERIR. Erramos sempre pro lado seguro:
 * aceitamos alguns falso-alarmes; NUNCA um falso-aprovado (R$80k por erro).
 */
class Verificador(
    private val lote: Set<String>,
    private val limiarConfianca: Float = 0.80f,
) {

    fun verificar(moto: Leitura?, chassi: Leitura?): Veredito {
        val numMoto = moto?.texto ?: ""
        val numChassi = chassi?.texto ?: ""

        if (moto == null || chassi == null) {
            return Veredito(Resultado.CONFERIR, numMoto, numChassi,
                "Não consegui ler uma das placas.")
        }

        if (moto.confiancaMinima < limiarConfianca ||
            chassi.confiancaMinima < limiarConfianca) {
            return Veredito(Resultado.CONFERIR, numMoto, numChassi,
                "Dígito duvidoso (confiança abaixo do limiar).")
        }

        if (numMoto != numChassi) {
            return Veredito(Resultado.CONFERIR, numMoto, numChassi,
                "Moto e chassi NÃO batem.")
        }

        if (numMoto !in lote) {
            return Veredito(Resultado.CONFERIR, numMoto, numChassi,
                "Número não está no lote cadastrado.")
        }

        return Veredito(Resultado.MATCH, numMoto, numChassi,
            "Confere: moto = chassi, no lote, leitura firme.")
    }
}
