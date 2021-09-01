package com.example.testapplication.baseclasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

open class BaseviewModel(private val respository: BaseRepository): ViewModel() {

    init {
        if (!respository._errorResponse.hasObservers()) observeErrorResponse()
    }

    private val errorResponse = MutableLiveData<com.example.testapplication.network.Error>()
    open fun observeErrorResponse(){
        respository._errorResponse.observeForever(Observer { response ->
            errorResponse.value = response
        })
    }

    fun getErrorResponse():MutableLiveData<com.example.testapplication.network.Error>{
        return errorResponse
    }
}
