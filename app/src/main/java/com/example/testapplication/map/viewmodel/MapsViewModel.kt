package com.example.testapplication.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.testapplication.baseclasses.BaseviewModel
import com.example.testapplication.map.model.VehicleList
import com.example.testapplication.map.model.VehicleResponse
import com.example.testapplication.map.model.VehicleResponseWrapper
import com.example.testapplication.network.ResponseWrapper
import com.example.testapplication.repository.MapsRepository
import com.example.testapplication.utils.Constants
import javax.inject.Inject

class MapsViewModel @Inject constructor(private val repository: MapsRepository) :
    BaseviewModel(repository)  {
    private val _getResponse = MutableLiveData<ResponseWrapper>()
    val apiResponse = MutableLiveData<VehicleResponseWrapper>()

    fun fetchVehiclesApiCall(){
        if (!_getResponse.hasObservers()) observerApiResponse()
        repository.fetchVehicleListApi(_getResponse,
            Constants.FETCH_VEHICLES_SERVICE_ID
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
                    vehicleResponse = null
                )
            }else{
                when(response.serviceID){
                    Constants.FETCH_VEHICLES_SERVICE_ID ->{
                        sendResponseToView(responseString = response.message ,isSuccess = true, serviceID = response.serviceID, errorCode = response.errorCode,
                            vehicleResponse = response.result as ArrayList<VehicleList>)
                    }

                }
            }
        })
    }
    private fun sendResponseToView( responseString: String, isSuccess: Boolean, serviceID: String, errorCode: Int = 0,vehicleResponse: ArrayList<VehicleList>?) {
        val responseData =
            VehicleResponseWrapper(
                isSuccess,
                responseString,
                errorCode,
                serviceID,
                vehicleResponse
            )
        apiResponse.value = responseData
    }
}