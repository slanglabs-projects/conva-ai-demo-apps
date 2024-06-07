package `in`.slanglabs.androidpg.convaai.impl.asr.utils

import android.annotation.SuppressLint
import android.app.Application
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Handler
import android.os.Looper

class AudioUtils {
    companion object {
        private lateinit var sASRSoundPool: SoundPool
        private lateinit var sASRStreamIds: IntArray

        @SuppressLint("DiscouragedApi")
        fun initASRSounds(application: Application) {
            sASRSoundPool = createSoundPool()
            sASRStreamIds = IntArray(3)

            val asrBeginSound = application.resources.getIdentifier(
                "conva_lib_asr_begin",
                "raw",
                application.packageName
            )

            val asrEndSound = application.resources.getIdentifier(
                "conva_lib_asr_end",
                "raw",
                application.packageName
            )

            val asrSuspendSound = application.resources.getIdentifier(
                "conva_lib_asr_suspend",
                "raw",
                application.packageName
            )

            sASRStreamIds[0] = sASRSoundPool.load(application, asrBeginSound, 1)
            sASRStreamIds[1] = sASRSoundPool.load(application, asrEndSound, 1)
            sASRStreamIds[2] = sASRSoundPool.load(application, asrSuspendSound, 1)
        }

        fun playASRBeginSound() {
            Handler(Looper.getMainLooper()).post {
                // Null check necessary to avoid NPE if app is backgrounded
                sASRSoundPool?.let {
                    it.play(sASRStreamIds[0], 1f, 1f, 1, 0, 1f)
                }
            }
        }

        fun playASREndSound() {
            Handler(Looper.getMainLooper()).post {
                // Null check necessary to avoid NPE if app is backgrounded
                sASRSoundPool?.let {
                    it.play(sASRStreamIds[1], 1f, 1f, 1, 0, 1f)
                }
            }
        }

        fun playASRSuspendSound() {
            Handler(Looper.getMainLooper()).post {
                // Null check necessary to avoid NPE if app is backgrounded
                sASRSoundPool?.let {
                    it.play(sASRStreamIds[2], 1f, 1f, 1, 0, 1f)
                }
            }
        }


        private fun createSoundPool(): SoundPool {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SoundPool.Builder().setMaxStreams(1).build()
            } else {
                SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0)
            }
        }
    }
}