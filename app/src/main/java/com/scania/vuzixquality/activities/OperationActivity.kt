package com.scania.vuzixquality.activities

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.scania.vuzixquality.OperationController
import com.scania.vuzixquality.R
import kotlinx.android.synthetic.main.activity_operation.*

import com.scania.vuzixquality.repository.OperationLoader

class OperationActivity : AppCompatActivity(), View.OnClickListener {

    private var locked = false
    private val animationTime: Long = 750
    private lateinit var operationController: OperationController
    private val mView = this
    private val REQUEST_IMAGE_CAPTURE = 1

    // TODO Implement Voice Controller
    // TODO Enable buttons or not via config
    //    val cameraStateCallbacks =  Camera

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        setContentView(R.layout.activity_operation)

        val operations = OperationLoader.json(this.applicationContext)

        operationController =
            OperationController(this, operations)
        operationController.updateOperation(0, updateTasks)

        text_chassi.text = operationController.chassi

        registerListeners()

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

    fun update() {

    }


    override fun onClick(v: View?) {

        val context = this
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

                val builder = AlertDialog.Builder(this)
                builder.setTitle("MENSAGEM")
                builder.setMessage("Gostaria de Fotografar o Desvio?")

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
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                Log.i("KEY", "LEFT")
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                Log.i("KEY", "RIGHT")
            }
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                Log.i("KEY", "CENTER")
            }
            KeyEvent.KEYCODE_DEL -> {
                this.animateBackground(Color.RED)
                operationController.updateOperation(updateTasks, 2)
            }
            KeyEvent.KEYCODE_FORWARD_DEL -> {
                this.animateBackground(Color.GREEN)
                operationController.updateOperation(updateTasks, 1)
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
        button_notok.requestFocusFromTouch()
        button_notok.setOnClickListener(this)
        button_ok.requestFocusFromTouch()
        button_ok.setOnClickListener(this)
    }


}
