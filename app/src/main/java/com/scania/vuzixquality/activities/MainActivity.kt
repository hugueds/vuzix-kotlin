package com.scania.vuzixquality.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.scania.vuzixquality.R
import com.scania.vuzixquality.model.OperationResult
import com.scania.vuzixquality.utils.OperationLogger
import com.scania.vuzixquality.utils.VoiceController
import com.vuzix.sdk.barcode.ScannerIntent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_SCAN = 0
    lateinit var voiceController: VoiceController
    private val server = "http://192.168.1.16:5000/"

    // TODO Create preferences Class
    // TODO Load preferences
    // TODO Enable buttons or not by preferences
    // TODO Create a preferences menu in Main



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        voiceController = VoiceController(this)

        button.setOnClickListener {
            val intent = Intent(ScannerIntent.ACTION)
            startActivityForResult(intent, REQUEST_CODE_SCAN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val context = this

        when (requestCode) {

            REQUEST_CODE_SCAN -> {

                if (resultCode == Activity.RESULT_OK) {
                    Log.i("SCAN", "SCAN OK")
                    val resultString =
                        data?.getStringExtra(ScannerIntent.RESULT_EXTRA_BARCODE_TEXT);
                    // TODO Chassis Number Validation
                    val intent = Intent(context, OperationActivity::class.java).apply {
                        putExtra("CHASSIS", resultString)
                    }
                    startActivity(intent)
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {

        when (event?.keyCode) {

            KeyEvent.KEYCODE_1 -> { // Skip Barcode reader
                if (event.action == KeyEvent.ACTION_UP) {
                    val intent = Intent(this, OperationActivity::class.java).apply {
                        putExtra("CHASSIS", "9999999")
                    }
                    startActivity(intent)
                }
            }

            KeyEvent.KEYCODE_R -> {
                val results = listOf(
                    OperationResult(1, 2, "CHASSIS", "BASE64")
                )
                OperationLogger.submit(this, server, results)
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


}
