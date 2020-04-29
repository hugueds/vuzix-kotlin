package com.scania.vuzixquality.utils

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import com.scania.vuzixquality.model.Endpoint
import com.scania.vuzixquality.model.OperationResult
import com.scania.vuzixquality.model.OperationResultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OperationLogger() {

    // TODO Load server from config

    companion object {

        fun save(results: List<OperationResult>, context: Context) {

            // TODO Store data in SQLite DB
            Toast.makeText(context, "RESULTADOS SALVOS COM SUCESSO", Toast.LENGTH_LONG).show()
            for (r in results) {
                Log.i("RESULT", r.toString())
            }

            this.submit(context, "http://10.33.22.113:8080", results)

        }

        fun submit(context: Context, server: String = "http://10.33.22.113:8080", results: List<OperationResult>) {

            val client = NetworkUtils.getRetrofitInstance(server)
            val endpoint = client.create(Endpoint::class.java)

            endpoint.createOperationResult(results).enqueue(object : Callback<OperationResultResponse> {

                override fun onFailure(call: Call<OperationResultResponse>, t: Throwable) {
                    Log.e("SUBMIT", t.toString())
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