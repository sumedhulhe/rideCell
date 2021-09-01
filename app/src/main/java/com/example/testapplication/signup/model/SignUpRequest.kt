package com.example.testapplication.signup.model

data class SignUpRequest(
    var display_name:String?="",
    var email:String?="",
    var password:String?="",
    var reEnterPassword:String?=""
)


