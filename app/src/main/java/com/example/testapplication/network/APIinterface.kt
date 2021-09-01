package com.example.testapplication.network

import com.example.testapplication.utils.Constants.ApiMethod.Companion.GET_METHOD
import com.example.testapplication.utils.Constants.ApiMethod.Companion.POST_METHOD
import io.reactivex.Observable
import retrofit2.http.*

interface APIinterface {

    @POST("{url}")
    fun postMethod(
        @Path(
            value = "url",
            encoded = true
        ) methodUrl: String, @Body requestObj: Any?
    ): Observable<Any>

    @GET
    fun getMethod(
        @Url methodUrl: String
    ): Observable<Any>



    companion object {
        fun callBack(
            apiCallInterface: APIinterface?,
            apiMethod: Int,
            url: String,
            requestObj: Any?
        ): Observable<Any>? {
            var call: Observable<Any>? = null
            when (apiMethod) {
                GET_METHOD -> call = apiCallInterface?.getMethod(url)
                POST_METHOD -> call = apiCallInterface?.postMethod(url, requestObj)
            }
            return call
        }
    }

}