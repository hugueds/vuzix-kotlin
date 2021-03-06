package com.scania.vuzixquality.utils

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import com.scania.vuzixquality.model.OperationResult
import com.scania.vuzixquality.model.OperationResultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface Endpoint {
    @POST("vuzix")
    fun create(@Body body: List<OperationResult>): retrofit2.Call<OperationResultResponse>
}

class OperationLogger() {

    // TODO Load server from config

    companion object {

        fun save(results: List<OperationResult>, context: Context) {

            // TODO Store data in SQLite DB
            Toast.makeText(context, "RESULTADOS SALVOS COM SUCESSO", Toast.LENGTH_LONG).show()

            val displayLogs = false
            for (r in results) {
                if (displayLogs)
                    Log.i("RESULT", r.toString())
            }

            this.submit(context, results, "http://10.33.22.113:8080")

        }

        fun submit(
            context: Context,
            results: List<OperationResult>,
            server: String = "http://10.33.22.113:8080"
        ) {

            val client = NetworkUtils.getRetrofitInstance(server)
            val endpoint = client.create(Endpoint::class.java)

            endpoint.create(results)
                .enqueue(object : Callback<OperationResultResponse> {

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