package com.scania.vuzixquality.utils

import android.app.Activity
import android.content.IntentFilter
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient
import java.lang.Exception


class VoiceController {

    // TODO Check is the listener is active
    // TODO Create a singleton

    lateinit var sc: VuzixSpeechClient

    lateinit var activity: Activity

    constructor(activity: AppCompatActivity) {

        try {
            this.activity = activity
            VuzixSpeechClient.EnableRecognizer(this.activity.applicationContext, true)
            sc = VuzixSpeechClient(activity)
            this.registerPhrases()
            Toast.makeText(activity.applicationContext, "VOICE IS ACTIVE", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("VOICE_CONTROLLER_EXCEPTION", "DEVICE ERROR")
            Log.e("VOICE_CONTROLLER_EXCEPTION", e.message)
        }

    }

    fun startListening() {

    }

    fun stopListening() {

    }

    private fun registerPhrases() {

        sc.insertWakeWordPhrase("hello")
        sc.insertWakeWordPhrase("start")
        sc.insertWakeWordPhrase("vuzix")

        // praaxymoo praaxymow saertow saertoo
        // voow thar voo thar thees view

        sc.insertKeycodePhrase("okay", KeyEvent.KEYCODE_FORWARD_DEL)
        sc.insertKeycodePhrase("praaxymoo", KeyEvent.KEYCODE_FORWARD_DEL)
        sc.insertKeycodePhrase("praaxymow", KeyEvent.KEYCODE_FORWARD_DEL)
        sc.insertKeycodePhrase("saertow", KeyEvent.KEYCODE_FORWARD_DEL)
        sc.insertKeycodePhrase("saertoo", KeyEvent.KEYCODE_FORWARD_DEL)


//        sc.insertKeycodePhrase("not okay", KeyEvent.KEYCODE_DEL)
        sc.insertKeycodePhrase("thees view", KeyEvent.KEYCODE_DEL)
        sc.insertKeycodePhrase("theesview", KeyEvent.KEYCODE_DEL)
        sc.insertKeycodePhrase("this viel", KeyEvent.KEYCODE_DEL)
        sc.insertKeycodePhrase("thisviel", KeyEvent.KEYCODE_DEL)


        sc.insertKeycodePhrase("error", KeyEvent.KEYCODE_DEL)

    }


}