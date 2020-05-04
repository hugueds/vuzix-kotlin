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
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_SCAN = 0
    lateinit var voiceController: VoiceController
    private val server = "http://192.168.1.16:5000/"
    private val DEVICE_TYPE = 2 // 0 -> Vuzix, 1 -> Emulator, 2 -> Mobile

    // TODO Create preferences Class
    // TODO Load preferences
    // TODO Enable buttons or not by preferences
    // TODO Create a preferences menu in Main



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

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
                        data?.getStringExtra(ScannerIntent.RESULT_EXTRA_BARCODE_TEXT);
                    // TODO Chassis Number Validation
                    if (resultString != "" || resultString.length < 7)
                        navigate(resultString)
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


    private fun navigate(resultString: String? = "999999") {
        val intent = Intent(this, OperationActivity::class.java)
        intent.putExtra("CHASSIS", resultString)
        startActivity(intent)
    }


}
