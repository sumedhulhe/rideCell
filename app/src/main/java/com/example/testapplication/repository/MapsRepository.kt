package com.example.testapplication.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.testapplication.baseclasses.BaseRepository
import com.example.testapplication.map.model.VehicleResponse
import com.example.testapplication.network.APIinterface
import com.example.testapplication.network.ResponseWrapper
import com.example.testapplication.utils.Constants
import javax.inject.Inject

class MapsRepository @Inject constructor(val service: APIinterface): BaseRepository() {

    fun fetchVehicleListApi(
        apiResponse: MutableLiveData<ResponseWrapper>,
        serviceID: String
    ) {
        this.apiResponse = apiResponse

        networkCall(
            Constants.ApiMethod.GET_METHOD,
            Constants.FETCH_VEHICLES_URL, Any(), serviceID, VehicleResponse::class.java)
    }




    override fun handleResponse(responseObj: ResponseWrapper) {
        apiResponse.value = responseObj
    }

    override fun handleError(error: Throwable) {
        Log.e("Error", error.message!!)
    }

}