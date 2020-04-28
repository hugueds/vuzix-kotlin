package com.scania.vuzixquality.model

import android.content.res.TypedArray
import android.telecom.Call
import android.util.TypedValue
import com.scania.vuzixquality.model.OperationResult
import com.scania.vuzixquality.model.OperationResultResponse
import com.scania.vuzixquality.model.OperationTask
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface Endpoint {

    @POST("vuzix")
    fun createOperationResult(@Body body: List<OperationResult>): retrofit2.Call<OperationResultResponse>




}