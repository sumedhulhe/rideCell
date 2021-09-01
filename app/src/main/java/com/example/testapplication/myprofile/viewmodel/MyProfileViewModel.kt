package com.example.testapplication.myprofile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.testapplication.baseclasses.BaseviewModel
import com.example.testapplication.myprofile.model.MyProfileResponse
import com.example.testapplication.myprofile.model.MyProfileResponseWrapper
import com.example.testapplication.network.ResponseWrapper
import com.example.testapplication.repository.MyProfileRepository
import com.example.testapplication.utils.Constants
import javax.inject.Inject

class MyProfileViewModel @Inject constructor(private val repository: MyProfileRepository) :
    BaseviewModel(repository){
    private val _getResponse = MutableLiveData<ResponseWrapper>()
    val apiResponse = MutableLiveData<MyProfileResponseWrapper>()


    fun fetchProfileDetailsApiCall(){
        if (!_getResponse.hasObservers()) observerApiResponse()
        repository.fetchProfileDetailsApi(_getResponse,
            Constants.FETCH_PROFILE_DETAILS_SERVICE_ID
        )
    }

    fun observerApiResponse() {
        _getResponse.observeForever(Observer { response ->
            if (response.statusCode != 200){
                var errorResponseMsg = if(response.error != null) response.error else response.message
                sendResponseToView(
                    responseString = errorResponseMsg,
                    isSuccess = false,
                    serviceID = response.serviceID,
                    result = null
                )
            }else{
                when(response.serviceID){
                    Constants.FETCH_PROFILE_DETAILS_SERVICE_ID ->{
                        sendResponseToView(responseString = response.message ,isSuccess = true, serviceID = response.serviceID, errorCode = response.errorCode,
                            result = response.result as MyProfileResponse)
                    }

                }
            }
        })
    }
    private fun sendResponseToView( responseString: String, isSuccess: Boolean, serviceID: String, errorCode: Int = 0,result: MyProfileResponse?) {
        val responseData =
            MyProfileResponseWrapper(
                isSuccess,
                responseString,
                errorCode,
                serviceID,
                result
            )
        apiResponse.value = responseData
    }
}