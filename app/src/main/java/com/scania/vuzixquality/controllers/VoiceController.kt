package com.scania.vuzixquality.controllers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient


class VoiceController(activity: AppCompatActivity): BroadcastReceiver() {

    // TODO Check is the listener is active

    private lateinit var sc: VuzixSpeechClient
    private lateinit var activity: Activity

    init {
        try {
            this.activity = activity
            VuzixSpeechClient.EnableRecognizer(this.activity.applicationContext, true)
            sc = VuzixSpeechClient(activity)
            this.registerPhrases()
            Toast.makeText(activity.applicationContext, "VOICE IS ACTIVE", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("VOICE_CONTROLLER_EXCEPTION", "DEVICE ERROR")
            Log.e("VOICE_CONTROLLER_EXCEPTION", e.message!!)
        }
    }

    private fun registerPhrases() {


        sc.insertWakeWordPhrase("start")

//        sc.insertKeycodePhrase("okay", KeyEvent.KEYCODE_FORWARD_DEL)
//        sc.insertKeycodePhrase("ok", KeyEvent.KEYCODE_FORWARD_DEL)
//        sc.insertKeycodePhrase("all k", KeyEvent.KEYCODE_FORWARD_DEL)
//        sc.insertKeycodePhrase("saertow", KeyEvent.KEYCODE_FORWARD_DEL)
//        sc.insertKeycodePhrase("saertoo", KeyEvent.KEYCODE_FORWARD_DEL)
//
//        sc.insertKeycodePhrase("thees view", KeyEvent.KEYCODE_DEL)
//        sc.insertKeycodePhrase("thees_view", KeyEvent.KEYCODE_DEL)
//        sc.insertKeycodePhrase("theesview", KeyEvent.KEYCODE_DEL)
//        sc.insertKeycodePhrase("this viel", KeyEvent.KEYCODE_DEL)
//        sc.insertKeycodePhrase("thisviel", KeyEvent.KEYCODE_DEL)

        sc.insertKeycodePhrase("okay", KeyEvent.KEYCODE_DPAD_RIGHT)
        sc.insertKeycodePhrase("ok", KeyEvent.KEYCODE_DPAD_RIGHT)
        sc.insertKeycodePhrase("all k", KeyEvent.KEYCODE_DPAD_RIGHT)
        sc.insertKeycodePhrase("ceartoow", KeyEvent.KEYCODE_DPAD_RIGHT)

        sc.insertKeycodePhrase("thees view", KeyEvent.KEYCODE_DPAD_LEFT)
        sc.insertKeycodePhrase("thees_view", KeyEvent.KEYCODE_DPAD_LEFT)
        sc.insertKeycodePhrase("theesview", KeyEvent.KEYCODE_DPAD_LEFT)
        sc.insertKeycodePhrase("this viel", KeyEvent.KEYCODE_DPAD_LEFT)
        sc.insertKeycodePhrase("thisviel", KeyEvent.KEYCODE_DPAD_LEFT)

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
    }

    fun unregister() {
        try {
            this.activity.unregisterReceiver(this)
            Log.i("VUZIX", "Custom vocab removed")
        } catch (e: Exception) {
            Log.e("VUZIX", "Custom vocab died " + e.message)
        }
    }


}