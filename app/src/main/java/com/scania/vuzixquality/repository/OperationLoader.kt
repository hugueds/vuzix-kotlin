package com.scania.vuzixquality.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scania.vuzixquality.model.OperationTask
import com.scania.vuzixquality.model.OperationList
import java.io.IOException

class OperationLoader {

    companion object {

        fun createOperations(): Array<OperationTask> {
            return arrayOf<OperationTask>(
                OperationTask(
                    1,
                    "OP1",
                    "OPER 1",
                    "R.drawable._01"
                ),
                OperationTask(
                    2,
                    "OP2",
                    "OPER 2",
                    "R.drawable._02"
                ),
                OperationTask(
                    3,
                    "OP3",
                    "OPER 3",
                    "R.drawable._03"
                )
            )
        }

        fun json(context: Context): List<OperationTask> {
            val jsonString: String? = getJsonDataFromAsset(context, "single_chassi.json")
            val type = object : TypeToken<OperationList>(){}.type
            val gson : Gson = Gson()
            val op: OperationList = gson.fromJson(jsonString, type)
            return op.operations
        }

    }
}

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}