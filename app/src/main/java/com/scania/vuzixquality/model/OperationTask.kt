package com.scania.vuzixquality.model

import com.google.gson.annotations.SerializedName

data class OperationList(val chassi: String, val operations: List<OperationTask>)

data class OperationTask(val id: Int, val name: String, val description: String, val picture: String) {
    var status: Int = 0
}
