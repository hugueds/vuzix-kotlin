package com.scania.vuzixquality.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.scania.vuzixquality.R
import com.scania.vuzixquality.model.OperationResult
import com.scania.vuzixquality.utils.OperationLogger
import com.scania.vuzixquality.controllers.VoiceController
import com.vuzix.sdk.barcode.ScannerIntent
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    // TODO Create preferences Class
    // TODO Load preferences
    // TODO Enable buttons or not by preferences
    // TODO Create a preferences menu in Main
    // TODO Transform Vuzix into Static Class
    // TODO Regex Chassis Number Validation

    private val REQUEST_CODE_SCAN = 0
    private lateinit var voiceController: VoiceController
    private val server = "http://10.33.22.113:8080" //"http://192.168.1.16:5000/" // PUT IN SETTINGS
    private val DEVICE_TYPE = 0   // 0 -> Vuzix, 1 -> Emulator, 2 -> Mobile


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        voiceController = VoiceController(this)

        button.setOnClickListener {

            if (DEVICE_TYPE == 2) {
                navigate()
            }

            try {
                val intent = Intent(ScannerIntent.ACTION)
                startActivityForResult(intent, REQUEST_CODE_SCAN)
            } catch (e: Exception) {
                Log.e("VUZIX", e.toString())
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            REQUEST_CODE_SCAN -> {

                if (resultCode == Activity.RESULT_OK) {
                    Log.i("SCAN", "SCAN OK")
                    val resultString =
                        data?.getStringExtra(ScannerIntent.RESULT_EXTRA_BARCODE_TEXT)
                    val test = true // ENABLE ONLY IN PRODUCTION
                    if (resultString != "" && resultString?.length == 7 || test)
                        navigate(resultString)
                    else
                        Toast.makeText(
                            this@MainActivity, "CODIGO DE BARRA INVALIDO",
                            Toast.LENGTH_LONG
                        ).show()
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {

        when (event?.keyCode) {

            KeyEvent.KEYCODE_1 -> { // Skip Barcode reader
                if (event.action == KeyEvent.ACTION_UP) {
                    navigate("")
                }
            }

            KeyEvent.KEYCODE_R -> {
                val results = listOf(
                    OperationResult(1, 2, "CHASSIS", "BASE64")
                )
                if (event.action == KeyEvent.ACTION_UP)
                    OperationLogger.submit(this, results, server)
            }

            KeyEvent.KEYCODE_S -> {
                if (event.action == KeyEvent.ACTION_UP) {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }

            KeyEvent.KEYCODE_2 -> {
                if (event.action == KeyEvent.ACTION_UP) {
                    val intent = Intent(this, CameraActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        return super.dispatchKeyEvent(event)
    }


    private fun navigate(resultString: String? = "999999") {
        val intent = Intent(this, OperationActivity::class.java)
        intent.putExtra("CHASSIS", resultString)
        startActivity(intent)
    }

    override fun onDestroy() {
        voiceController.unregister()
        super.onDestroy()
    }


}
