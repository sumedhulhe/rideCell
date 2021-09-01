package com.example.testapplication.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.testapplication.utils.Constants
import com.example.testapplication.dashboard.model.LoginResponse
import com.example.testapplication.network.APIinterface
import com.example.testapplication.network.ResponseWrapper
import com.example.testapplication.baseclasses.BaseRepository
import com.example.testapplication.dashboard.model.SignUpResponse
import com.example.testapplication.login.viewmodel.LoginViewModel
import com.example.testapplication.signup.model.SignUpRequest
import javax.inject.Inject

class LoginRepository  @Inject constructor(val service: APIinterface): BaseRepository() {
    fun loginUserApi(
        apiResponse: MutableLiveData<ResponseWrapper>,
        serviceID: String,
        request:Any?
    ) {
        this.apiResponse = apiResponse

        networkCall(
            Constants.ApiMethod.POST_METHOD,
            Constants.LOGIN_URL, request, serviceID, LoginResponse::class.java)
    }

    fun signUpApi(
        apiResponse: MutableLiveData<ResponseWrapper>,
        serviceID: String,
        signUpRequest: SignUpRequest
    ) {
        this.apiResponse = apiResponse

        networkCall(
            Constants.ApiMethod.POST_METHOD,
            Constants.SIGN_UP_URL, signUpRequest, serviceID, SignUpResponse::class.java)
    }




    override fun handleResponse(responseObj: ResponseWrapper) {
        apiResponse.value = responseObj
    }

    override fun handleError(error: Throwable) {
        Log.e("Error", error.message!!)
    }

}