package com.example.testapplication.login.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.testapplication.utils.Constants
import com.example.testapplication.R
import com.example.testapplication.dashboard.model.LoginRequest
import com.example.testapplication.dashboard.model.LoginResponse
import com.example.testapplication.databinding.ActivityLoginBinding
import com.example.testapplication.login.viewmodel.LoginViewModel
import com.example.testapplication.baseclasses.BaseActivity
import javax.inject.Inject
import androidx.lifecycle.ViewModelProviders
import com.example.testapplication.dashboard.view.DashboardActivity
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager.Companion.TOKEN
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager.Companion.USER_KEY
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager.Companion.readString
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager.Companion.writeString
import com.example.testapplication.signup.view.SignUpFragment
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val loginViewModel :LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onRetry(serviceID: String) {
        when (serviceID) {
            Constants.LOGIN_SERVICE_ID ->
                loginViewModel.validateLogin(binding.loginData!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginData= LoginRequest("eve.holt@reqres.in","cityslicka")
        init()
    }

    fun init() {
        if(readString(TOKEN)?.isNotEmpty()!!){
            startNewActivity(DashboardActivity::class.java)
            finish()

        }
        if (!loginViewModel.apiResponse.hasObservers()) observeApiResponse()
        if (!loginViewModel.getErrorResponse().hasObservers()) observeErrorResponse(
            loginViewModel,
            binding.emailIdEdt
        )

        binding.loginBtn.setOnClickListener({
           loginViewModel.validateLogin( binding.loginData!!)
        })

        binding.signUpTv.setOnClickListener{
            addFragment(SignUpFragment(),"SignUpFragment")
        }
    }

    private fun observeApiResponse() {
        loginViewModel.apiResponse.observe(this, Observer { apiResponse ->
            dismissProgressDialog()
            if (!apiResponse.isSuccess) {
                showToast(apiResponse.message)
            } else {
                when (apiResponse.serviceID) {
                    Constants.LOGIN_SERVICE_ID -> {
                        if (apiResponse.result != null) {
                            var data = apiResponse.result as LoginResponse?
                            if (data != null && data.authentication_token.isNotEmpty()) {
                                writeString(TOKEN,data.authentication_token)
                                writeString(USER_KEY,data.person.key)
                                showToast("Login successful")
                                startNewActivity(DashboardActivity::class.java)
                                finish()
                            } else {
                                showToast("Login failed")
                            }
                        }

                    }

                }
            }
        })
        loginViewModel.validateLoginLiveData.observe(this, Observer { response ->
                checkIfDataEmpty(response.emailValidation,binding.emailInputLayout)
            checkIfDataEmpty(response.passwordValidation,binding.passwordInputLayout)
            if(!binding.emailInputLayout.isErrorEnabled && !binding.passwordInputLayout.isErrorEnabled){
                showProgressDialog("Loading...")
                loginViewModel.loginUsersApiCall(binding.loginData!!)
            }

        })
    }
    private fun checkIfDataEmpty(validationError:String,textInputLayout: TextInputLayout){
        if(validationError.isNotEmpty()) textInputLayout.error=validationError
        else
            textInputLayout.isErrorEnabled=false
    }
}