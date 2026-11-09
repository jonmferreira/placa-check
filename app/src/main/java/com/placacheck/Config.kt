package com.placacheck

import android.content.Context

/**
 * Preferências simples (SharedPreferences, persiste no device):
 *   - sinal: como avisar o operador quando uma leitura trava (SOM ou VIBRAÇÃO)
 *   - operador: nome que vai no log de cada análise salva
 */
object Config {
    enum class Sinal { SOM, VIBRACAO }

    private const val PREF = "placacheck"
    private const val K_SINAL = "sinal"
    private const val K_OPERADOR = "operador"

    fun sinal(ctx: Context): Sinal {
        val p = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return if (p.getString(K_SINAL, Sinal.SOM.name) == Sinal.VIBRACAO.name)
            Sinal.VIBRACAO else Sinal.SOM
    }

    fun setSinal(ctx: Context, s: Sinal) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(K_SINAL, s.name).apply()
    }

    fun operador(ctx: Context): String {
        val p = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return p.getString(K_OPERADOR, "operador") ?: "operador"
    }

    fun setOperador(ctx: Context, nome: String) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(K_OPERADOR, nome.ifBlank { "operador" }).apply()
    }
}
