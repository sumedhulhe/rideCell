package com.example.testapplication.baseclasses


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.testapplication.map.model.VehicleList
import com.example.testapplication.map.model.VehicleResponse
import com.example.testapplication.network.APIinterface
import com.example.testapplication.network.ResponseWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import javax.inject.Inject


abstract class BaseRepository {

    @Inject
    lateinit var apIinterface: APIinterface
    open lateinit var apiResponse: MutableLiveData<ResponseWrapper>
    open val _errorResponse = MutableLiveData<com.example.testapplication.network.Error>()

    fun networkCall(
        apiMethod: Int,
        url: String,
        requestObj: Any?,
        serviceID: String,
        resultClass: Class<*>
    ) {
        APIinterface.callBack(
            apIinterface,
            apiMethod,
            url,
            requestObj)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<Any> {
                override fun onComplete() {
                //empty implementation
                }

                override fun onSubscribe(d: Disposable) {
                    //empty implementation
                }

                override fun onError(error: Throwable) {
                    val errors = com.example.testapplication.network.Error(404, serviceID, error, "error")
                    _errorResponse.value = errors
                    handleError(error)
                }

                override fun onNext(response: Any) {
                    if(response!=null) {
                        var result = getResultObj(response, resultClass)
                        handleResponse(
                            ResponseWrapper(
                                error = "",
                                result = result,
                                serviceID = serviceID,
                                message = "Success",
                                statusCode = 200
                            )
                        )
                    }else{
                        handleResponse(
                            ResponseWrapper(
                                error = "Something went worng!!",
                                result = null,
                                serviceID = serviceID,
                                message = ""
                            )
                        )
                    }
                }
            })
    }


    private fun getResultObj(result: Any, resultClass: Class<*>): Any {
        val gson = Gson()
        Log.e("CLASS NAME::", "" + resultClass.canonicalName)
        val str = gson.toJson(result, result.javaClass)
        if (resultClass.canonicalName.contains("VehicleResponse")) {

            val jsonObject = JSONArray(str)

            val listType: Type = object : TypeToken<ArrayList<VehicleList>?>() {}.getType()
            val resultObj: ArrayList<VehicleList> = gson.fromJson(jsonObject.toString(), listType)

            return resultObj
        }
        else if (str.startsWith("[") && str.endsWith("]" )) {
            val jsonArray = JSONArray(str)
            val resultObj: Any = gson.fromJson(jsonArray.toString(), resultClass)
            return resultObj
        } else {
            val jsonObject = JSONObject(str)
            val resultObj: Any = gson.fromJson(jsonObject.toString(), resultClass)
            return resultObj
        }
    }

    abstract fun handleResponse(responseObj: ResponseWrapper)

    abstract fun handleError(error: Throwable)
}