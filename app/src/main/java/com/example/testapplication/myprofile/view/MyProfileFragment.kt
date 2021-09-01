package com.example.testapplication.myprofile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.testapplication.R
import com.example.testapplication.baseclasses.BaseFragment
import com.example.testapplication.myprofile.viewmodel.MyProfileViewModel
import com.example.testapplication.utils.Constants
import kotlinx.android.synthetic.main.fragment_my_profile.*
import java.text.SimpleDateFormat
import javax.inject.Inject


class MyProfileFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val myProfileViewModel: MyProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MyProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onRetry(serviceID: String) {
        myProfileViewModel.fetchProfileDetailsApiCall()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!myProfileViewModel.apiResponse.hasObservers()) observeApiResponse()
        if (!myProfileViewModel.getErrorResponse().hasObservers()) observeErrorResponse(
            myProfileViewModel,
            requireView()
        )
        showProgressDialog("Loading...")
        myProfileViewModel.fetchProfileDetailsApiCall()
    }

    private fun observeApiResponse() {
        myProfileViewModel.apiResponse.observe(requireActivity(), Observer { apiResponse ->
            dismissProgressDialog()
            if (!apiResponse.isSuccess) {
                showToast(apiResponse.message)
            } else {
                when (apiResponse.serviceID) {
                    Constants.FETCH_PROFILE_DETAILS_SERVICE_ID -> {
                       nameTv.text=apiResponse.result?.display_name
                        dateTv.text="Created at "+convertDateFormat(apiResponse.result?.created_at.toString(),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd/MM/yyyy hh:mm aa")
                    }
                }
            }
        })
    }

    fun convertDateFormat(date: String, inputFormat: String, outputFormat: String): String {
        var _date = ""
        if (!date.equals("null", ignoreCase = true)) {
            try {
                val inputFormatter = SimpleDateFormat(inputFormat)//"yyyy-MM-dd"
                val date1 = inputFormatter.parse(date)

                val outputFormatter = SimpleDateFormat(outputFormat)//"dd-MM-yyyy"
                _date = outputFormatter.format(date1)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return _date
        } else {
            return _date
        }
    }
}