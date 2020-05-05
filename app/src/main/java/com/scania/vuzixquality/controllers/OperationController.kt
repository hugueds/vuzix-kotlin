package com.scania.vuzixquality.controllers

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import com.scania.vuzixquality.model.OperationResult
import com.scania.vuzixquality.model.OperationTask
import com.scania.vuzixquality.utils.OperationLogger

interface IOperationChanged {
    fun onUpdate()
    fun onFinish()
}


class OperationController {

    private var mView: Activity
    private lateinit var operationTasks: List<OperationTask>
    private var operationResults = mutableListOf<OperationResult>()

    val OPERATION_TIME: Long = 1000

    var totalOperations = 0
    var indexOperation = 0
    var chassi: String? = ""
    var locked = false

    lateinit var currentOperationTask: OperationTask

    constructor(mView: Activity) : super() {
        indexOperation = 0
        totalOperations = 0
        this.mView = mView
        this.chassi = mView.getIntent().getStringExtra("CHASSIS")
    }

    constructor(mView: Activity, operations: List<OperationTask>) : this(mView) {
        this.operationTasks = operations
        this.totalOperations = operations.size
        this.currentOperationTask = operations[0]
    }

    fun updateOperation(callback: () -> Unit, status: Int, errorPicture: String? = null) {

        if (this.locked) {
            Log.i("OPERATION", "OPERATION IS LOCKED")
            return
        }

        this.locked = true

        operationResults.add(OperationResult(
            status,currentOperationTask.id, chassi!!, errorPicture)
        )

        if (this.indexOperation < totalOperations - 1) {

            this.indexOperation += 1
            this.currentOperationTask.status = status
            this.currentOperationTask = operationTasks[indexOperation]

            val timer = object : CountDownTimer(OPERATION_TIME, OPERATION_TIME) {

                override fun onFinish() {
                    Log.i("OPERATION_DONE", currentOperationTask.name)
                    callback()
                    locked = false
                }

                override fun onTick(millisUntilFinished: Long) {}
            }

            timer.start()

        } else {
            Log.i("OperationController", "All operations Done")
            this.finish()
        }


    }

    fun updateOperation(index: Int, callback: () -> Unit) {
        this.indexOperation = index
        this.currentOperationTask = operationTasks[index]
        callback()
    }

    private fun finish() {

        val timer = object : CountDownTimer(OPERATION_TIME, OPERATION_TIME) {

            override fun onFinish() {
                OperationLogger.save(operationResults, mView.applicationContext)
                mView.finish()
            }

            override fun onTick(millisUntilFinished: Long) {}
        }
        timer.start()
    }

    fun reset(callback: () -> Unit) {
        this.indexOperation = 0
        this.currentOperationTask = operationTasks[0]
        callback()
    }



}

