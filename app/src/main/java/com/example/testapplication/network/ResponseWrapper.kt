package com.example.testapplication.network

data class ResponseWrapper(var error:String, var result: Any?, var serviceID: String , var errorCode: Int=0, var statusCode:Int?=0, val message:String)

data class Error(val statusCode: Int, val  serviceID: String, val errorType: Throwable, val errorMessages: String)

