package com.scania.vuzixquality.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scania.vuzixquality.utils.CameraController
import com.scania.vuzixquality.R



class CameraActivity : AppCompatActivity() {

    lateinit var cameraController: CameraController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)



    }
}
