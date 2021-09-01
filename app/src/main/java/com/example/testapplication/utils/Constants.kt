package com.example.testapplication.utils

import com.example.testapplication.sharedprefrence.SharedPrefrenceManager
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager.Companion.USER_KEY

interface Constants {
    interface ApiMethod {
        companion object {
            val GET_METHOD = 0
            val POST_METHOD = 1

        }
    }

    companion object {
        val BASEURL = "https://blooming-stream-45371.herokuapp.com/"
        val LOGIN_URL="api/v2/people/authenticate"
        val SIGN_UP_URL="api/v2/people/create"
        val FETCH_VEHICLES_URL="api/v2/vehicles"
        val FETCH_PROFILE_DETAILS_URL="/api/v2/people/${SharedPrefrenceManager.readString(USER_KEY)}"

        val LOGIN_SERVICE_ID="LOGIN_SERVICE_ID"
        val SIGN_UP_SERVICE_ID="SIGN_UP_SERVICE_ID"
        val FETCH_VEHICLES_SERVICE_ID="FETCH_VEHICLES_SERVICE_ID"
        val FETCH_PROFILE_DETAILS_SERVICE_ID="FETCH_PROFILE_DETAILS_SERVICE_ID"
        val ToDoEntityKey="ToDoEntityKey"


    }
}