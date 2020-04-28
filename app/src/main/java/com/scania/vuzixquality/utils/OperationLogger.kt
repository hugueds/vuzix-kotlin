package com.scania.vuzixquality.utils

import android.content.ContextWrapper
import android.widget.Toast
import com.scania.vuzixquality.model.Endpoint
import com.scania.vuzixquality.model.OperationResult
import com.scania.vuzixquality.model.OperationResultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OperationLogger() {


    companion object {

        fun save(results: List<OperationResult>, submit: Boolean = false) {

        }

        fun submit(context: ContextWrapper, server: String = "", results: List<OperationResult>) {

            val client = NetworkUtils.getRetrofitInstance(server)
            val a = client.create(Endpoint::class.java)


            a.createOperationResult(results).enqueue(object : Callback<OperationResultResponse> {

                override fun onFailure(call: Call<OperationResultResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<OperationResultResponse>,
                    response: Response<OperationResultResponse>
                ) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                }

            })

        }


    }

}