package com.scania.vuzixquality.model

data class OperationList(val chassi: String, val operations: List<OperationTask>)

data class OperationTask(val id: Int, val name: String, val description: String, val picture: String) {
    var status: Int = 0
}
