package com.example.testapplication

import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testapplication.dashboard.model.LoginRequest
import com.example.testapplication.dashboard.model.LoginValidator
import com.example.testapplication.login.viewmodel.LoginViewModel
import com.example.testapplication.network.APIinterface
import com.example.testapplication.repository.LoginRepository
import dagger.android.AndroidInjection
import io.reactivex.Observable

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
