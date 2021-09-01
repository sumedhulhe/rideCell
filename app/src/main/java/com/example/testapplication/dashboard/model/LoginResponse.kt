package com.example.testapplication.dashboard.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
	val authentication_token : String,
	val person:Person
)
data class Person(
	val key:String,
			val display_name:String,
					val role:Role)
data class Role(
	val key:String,
	val rank:Int
)
data class LoginApiResponseWrapper(
	var isSuccess:Boolean,
	var message:String,
	var error:Int,
	var serviceID:String,
	var result:Any?=null,
	var signUpResponse: SignUpResponse?=null
)

data class SignUpResponse (
	val authentication_token : String
)

