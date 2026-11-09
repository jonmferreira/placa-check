package com.placacheck

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Sinal de "check" quando uma leitura trava. Conforme a [Config]:
 *   SOM      → beep curto (ToneGenerator, sem arquivo de áudio)
 *   VIBRAÇÃO → pulso curto
 * A ideia é o operador se guiar pelo sinal (lê moto → check → lê chassi → check)
 * sem precisar olhar a tela a cada passo.
 */
class Feedback(private val ctx: Context) {

    private val tom by lazy { ToneGenerator(AudioManager.STREAM_MUSIC, 90) }

    fun check() {
        when (Config.sinal(ctx)) {
            Config.Sinal.SOM -> tom.startTone(ToneGenerator.TONE_PROP_BEEP, 180)
            Config.Sinal.VIBRACAO -> vibrar(120)
        }
    }

    private fun vibrar(ms: Long) {
        val vib: Vibrator =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
                    .defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
        vib.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
