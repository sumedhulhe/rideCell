package com.example.testapplication.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.testapplication.utils.Constants
import com.example.testapplication.network.ResponseWrapper
import com.example.testapplication.repository.LoginRepository
import com.example.testapplication.baseclasses.BaseviewModel
import com.example.testapplication.dashboard.model.*
import com.example.testapplication.signup.model.SignUpRequest
import com.example.testapplication.utils.Constants.Companion.SIGN_UP_SERVICE_ID
import javax.inject.Inject

class LoginViewModel  @Inject constructor(private val repository: LoginRepository) :
    BaseviewModel(repository) {

    private val _getResponse = MutableLiveData<ResponseWrapper>()
    val apiResponse = MutableLiveData<LoginApiResponseWrapper>()
    val validateLoginLiveData=MutableLiveData<LoginValidator>()

    fun loginUsersApiCall(loginRequest: LoginRequest){
        if (!_getResponse.hasObservers()) observerApiResponse()
        repository.loginUserApi(_getResponse,
            Constants.LOGIN_SERVICE_ID, loginRequest
        )
    }

    fun signUpApiCall(signUpRequest: SignUpRequest){
        if (!_getResponse.hasObservers()) observerApiResponse()
        repository.signUpApi(_getResponse,
            Constants.SIGN_UP_SERVICE_ID, signUpRequest
        )
    }

    fun validateLogin(loginRequest: LoginRequest):LoginValidator{
        var loginValidator=LoginValidator()
        loginValidator.emailValidation= if(loginRequest.email.isEmpty()) "Please enter email id" else
            if(!Patterns.EMAIL_ADDRESS.matcher(loginRequest.email).matches()) "Please enter valid email id" else ""

        loginValidator.passwordValidation=if(loginRequest.password.isEmpty()) "Please enter password" else ""
        validateLoginLiveData.value=loginValidator
        return loginValidator
    }
    fun observerApiResponse() {
        _getResponse.observeForever(Observer { response ->
            if (response.statusCode != 200){
                var errorResponseMsg = if(response.error != null) response.error else response.message
                sendResponseToView(
                    responseString = errorResponseMsg,
                    isSuccess = false,
                    serviceID = response.serviceID
                )
            }else{
                when(response.serviceID){
                    Constants.LOGIN_SERVICE_ID ->{
                        sendResponseToView(responseString = response.message ,isSuccess = true, serviceID = response.serviceID, errorCode = response.errorCode,result = response.result as LoginResponse)
                    }
                    SIGN_UP_SERVICE_ID->{
                        sendResponseToView(responseString = response.message ,isSuccess = true, serviceID = response.serviceID, errorCode = response.errorCode,signUpResponse = response.result as SignUpResponse)

                    }
                }
            }
        })
    }

    private fun sendResponseToView( responseString: String, isSuccess: Boolean, serviceID: String, errorCode: Int = 0,result: LoginResponse?=null,signUpResponse:SignUpResponse?=null) {
        val responseData =
            LoginApiResponseWrapper(
                isSuccess,
                responseString,
                errorCode,
                serviceID,
                result,
                signUpResponse
            )
        apiResponse.value = responseData
    }
}