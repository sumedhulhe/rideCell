package com.example.testapplication.signup.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.testapplication.R
import com.example.testapplication.baseclasses.BaseFragment
import com.example.testapplication.dashboard.model.LoginResponse
import com.example.testapplication.dashboard.model.SignUpResponse
import com.example.testapplication.dashboard.view.DashboardActivity
import com.example.testapplication.databinding.ActivityLoginBinding
import com.example.testapplication.databinding.FragmentSignUpBinding
import com.example.testapplication.login.viewmodel.LoginViewModel
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager
import com.example.testapplication.signup.model.SignUpRequest
import com.example.testapplication.utils.Constants
import javax.inject.Inject


class SignUpFragment : BaseFragment() {

    lateinit var binding: FragmentSignUpBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val loginViewModel : LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        return binding?.let { fragmentToDo -> binding!!.root }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpData= SignUpRequest()
        if (!loginViewModel.apiResponse.hasObservers()) observeApiResponse()
        if (!loginViewModel.getErrorResponse().hasObservers()) observeErrorResponse(
            loginViewModel,
            requireView()
        )
        binding.signUpBtn.setOnClickListener {
            showProgressDialog("Loading...")
            loginViewModel.signUpApiCall(binding.signUpData!!)
        }
    }
    override fun onRetry(serviceID: String) {

    }

    fun observeApiResponse(){
        loginViewModel.apiResponse.observe(requireActivity(), Observer { apiResponse ->
            dismissProgressDialog()
            if (!apiResponse.isSuccess) {
                showToast(apiResponse.message)
            } else {
                when (apiResponse.serviceID) {
                    Constants.SIGN_UP_SERVICE_ID -> {
                        if (apiResponse.signUpResponse != null) {
                            var data = apiResponse.signUpResponse as SignUpResponse?
                            if (data != null && data.authentication_token.isNotEmpty()) {
                                SharedPrefrenceManager.writeString(
                                    SharedPrefrenceManager.TOKEN,
                                    data.authentication_token
                                )
                                showToast("Register successful")
                                requireActivity().supportFragmentManager.popBackStack()

                            } else {
                                showToast("Signup failed")
                            }
                        }

                    }

                }
            }
        })
    }
}