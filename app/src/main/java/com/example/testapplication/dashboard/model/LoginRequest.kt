package com.example.testapplication.dashboard.model

data class LoginRequest(
    var email:String="",
    var password:String=""
)

data class LoginValidator(
    var emailValidation:String="",
    var passwordValidation:String=""
)