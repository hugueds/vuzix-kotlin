package com.scania.vuzixquality.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class OperationResult {

    private var result: Int = 0
    private var operationId: Int = 0
    private var chassi: String = ""
    private var dateTime: LocalDateTime? = null
    private var errorPicture: String = ""

    constructor(){}

    constructor(result:Int, operationId: Int, chassi: String, errorPicture: String? = null) {
        this.result = result
        this.operationId = operationId
        this.chassi = chassi
        this.dateTime = LocalDateTime.now()
        if (errorPicture != null)
            this.errorPicture = errorPicture
    }

    constructor(result:Int, operationId: Int, chassis: String, errorPicture: Bitmap) {
        // Convert a Bitmap Image to a Base64 String
    }

}

data class OperationResultResponse( val error: Boolean,  val message: String )
