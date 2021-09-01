package com.example.testapplication.myprofile.model

import com.example.testapplication.dashboard.model.Role

data class MyProfileResponse(
    val created_at : String,
    val display_name : String,
    val email : String,
    val key : String,
    val role : Role,
    val updated_at : String
)
data class MyProfileResponseWrapper(
    var isSuccess:Boolean,
    var message:String,
    var error:Int,
    var serviceID:String,
    var result:MyProfileResponse?=null
)