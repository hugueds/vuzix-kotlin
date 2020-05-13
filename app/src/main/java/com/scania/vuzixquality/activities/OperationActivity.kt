package com.scania.vuzixquality.activities

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.scania.vuzixquality.controllers.OperationController
import com.scania.vuzixquality.R
import kotlinx.android.synthetic.main.activity_operation.*

import com.scania.vuzixquality.repository.OperationLoader
import com.scania.vuzixquality.controllers.VoiceController
import com.scania.vuzixquality.utils.OperationLogger

class OperationActivity : AppCompatActivity(), View.OnClickListener {

    private var locked = false
    private val animationTime: Long = 750
    private lateinit var operationController: OperationController
    private val mView = this
    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var voiceController: VoiceController

    private var timeBar = 0

    // TODO Implement Voice Controller
    // TODO Enable buttons or not via config

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_operation)
        this.registerListeners()

        voiceController = VoiceController(this)

        val operations = OperationLoader.json(this.applicationContext)
        operationController = OperationController(this, operations)

        operationController.updateOperation(0, updateTasks)

        text_chassi.text = operationController.chassi

        timeBar = operationController.totalTime.toInt()
        Log.i("TOTAL TIME", timeBar.toString())

        val timer = object : CountDownTimer((operationController.totalTime * 1000).toLong(), 1000) {

            override fun onFinish() {
                Log.i("TIME", "ACTIVITY TIME HAS FINISHED")
            }

            override fun onTick(millisUntilFinished: Long) {
                timeBar -= 1
                if (timeBar > 0)
                    progressBar.progress = ((timeBar / operationController.totalTime) * 100).toInt()
                else
                    progressBar.progress = 0
                if (timeBar <= (0.3 * timeBar))
                    progressBar.progressTintList = ColorStateList.valueOf(Color.RED)
            }
        }
        timer.start()


    }

    // On Click, On Voice or On Key, add 1 to index and load the next picture
    @SuppressLint("SetTextI18n")
    val updateTasks: () -> Unit = {

        val operationTask = operationController.currentOperationTask.description
        val index = operationController.indexOperation
        val size = operationController.totalOperations
        val stringPicture = operationController.currentOperationTask.picture

        text_current_operation.text = "${index + 1} / $size"
        text_operation_name.text = operationTask

        layout_operation.setBackgroundColor(Color.BLACK)
        image_view_operation.setImageBitmap(loadImage(stringPicture))
    }

    override fun onClick(v: View?) {

        var color: Int = Color.WHITE
        when (v?.id) {

            button_ok.id -> {
                Log.i("BUTTON", "OK PRESSED")
                color = Color.GREEN
                operationController.updateOperation(updateTasks, 1)
            }

            button_notok.id -> {
                Log.i("BUTTON", "BUTTON NOT OK PRESSED")
                color = Color.RED

                // Abrir um Alert perguntando se gostaria de tirar uma foto
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("MENSAGEM")
//                builder.setMessage("Gostaria de Fotografar o Desvio?")
//                builder.show()
                operationController.updateOperation(updateTasks, 2)
            }

        }

        animateBackground(color)

    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {


        when (event?.keyCode) {

            KeyEvent.KEYCODE_DPAD_UP -> {
                Log.i("KEY", "UP")
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                Log.i("KEY", "DOWN")
            }

            KeyEvent.KEYCODE_DPAD_CENTER -> {
                Log.i("KEY", "CENTER")
            }
            KeyEvent.KEYCODE_DEL, KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (event.action == KeyEvent.ACTION_UP) {
                    this.animateBackground(Color.RED)
                    // Abrir a activity da foto
                    val intent = Intent(this, CameraActivity::class.java)
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                    // Receber a foto como string
//                    operationController.updateOperation(updateTasks, 2)
                }
            }
            KeyEvent.KEYCODE_FORWARD_DEL, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (event.action == KeyEvent.ACTION_UP) {
                    this.animateBackground(Color.GREEN)
                    operationController.updateOperation(updateTasks, 1)
                }
            }
            KeyEvent.KEYCODE_MENU -> {
                Log.i("KEY", "MENU")
            }
            KeyEvent.KEYCODE_R -> {
                Log.i("KEY", "NUMBER 1")
                operationController.reset(updateTasks)
            }

        }

        return super.dispatchKeyEvent(event)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val errorPicture = data?.getStringExtra("RESULT_STRING_BASE64")
                    operationController.updateOperation(updateTasks, 2, errorPicture)
                }
            }
        }

    }

    private fun loadImage(imgString: String): Bitmap {
        val splitImgString = imgString.split(",")[1]
        val img = Base64.decode(splitImgString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(img, 0, img.size)
    }

    private fun animateBackground(color: Int) {

        ObjectAnimator.ofObject(
            layout_operation, "backgroundColor", ArgbEvaluator(),
            Color.BLACK, color
        ).setDuration(animationTime).start()

    }


    private fun registerListeners() {
        button_notok.setOnClickListener(this)
        button_ok.setOnClickListener(this)
    }


}
