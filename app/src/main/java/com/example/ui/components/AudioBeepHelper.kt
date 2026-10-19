package com.example.ui.components

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

class AudioBeepHelper(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 90)
        } catch (e: Exception) {
            Log.e("AudioBeepHelper", "ToneGenerator init failed", e)
        }
    }

    fun playCountdownBeep() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
            vibrate(80)
        } catch (e: Exception) {
            Log.e("AudioBeepHelper", "playCountdownBeep failed", e)
        }
    }

    fun playTransitionBeep() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 350)
            vibrate(250)
        } catch (e: Exception) {
            Log.e("AudioBeepHelper", "playTransitionBeep failed", e)
        }
    }

    fun playWorkoutCompleteBeep() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 500)
            vibrate(400)
        } catch (e: Exception) {
            Log.e("AudioBeepHelper", "playWorkoutCompleteBeep failed", e)
        }
    }

    private fun vibrate(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (e: Exception) {
            // vibration fallback ignored gracefully
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            // ignore
        }
    }
}
