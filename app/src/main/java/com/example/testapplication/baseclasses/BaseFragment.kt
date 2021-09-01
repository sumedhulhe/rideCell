package com.example.testapplication.baseclasses

import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.testapplication.R
import com.google.android.material.snackbar.Snackbar
import java.net.UnknownHostException


open abstract class BaseFragment : dagger.android.support.DaggerFragment() {

    var progressDialog: ProgressDialog? = null
    var baseActivity: BaseActivity? = null
    var snackbar: Snackbar? = null
    var mIsTablet = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        baseActivity = activity as BaseActivity


    }



    fun showProgressDialog(message: String) {
        getProgressDialogInstance()?.let {
            var progressDialog = it
            progressDialog.setMessage(message)
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

    }




    fun getProgressDialogInstance(): ProgressDialog? {
        progressDialog?.let {
            return it
        }

        progressDialog = ProgressDialog(requireContext())
        return progressDialog
    }

    fun dismissProgressDialog() {
        getProgressDialogInstance()?.let {
            var progressDialog = it
            if (progressDialog.isShowing)
                progressDialog.dismiss()
            hideNavigationBar()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showAlertDialog(
        context: Context,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String,
        positiveOnClickListener: DialogInterface.OnClickListener,
        negativeOnClickListener: DialogInterface.OnClickListener,
        cancelable: Boolean
    ) {

        AlertDialog.Builder(context)
            .setTitle(requireContext().resources.getString(R.string.app_name))
            .setMessage(message)
            .setCancelable(cancelable)
            .setPositiveButton(positiveButtonLabel, positiveOnClickListener)
            .setNegativeButton(negativeButtonLabel, negativeOnClickListener)
            .show()
    }

    fun hideNavigationBar() {
        activity?.window?.decorView?.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
        }




    fun dismissSnackBar() {
        if (snackbar != null && snackbar?.isShown!!) snackbar?.dismiss()
    }

    abstract fun onRetry(serviceID: String)

    open fun observeErrorResponse(
        viewModel: BaseviewModel,
        view: View
    ) {
        viewModel.getErrorResponse().observe(this, androidx.lifecycle.Observer { response ->
            dismissProgressDialog()
            if (response.errorType is UnknownHostException) {
                snackbar =
                    Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE)
                snackbar?.setAction(getString(R.string.retry), View.OnClickListener { view ->
                    onRetry(response.serviceID)
                })
                snackbar?.show()
            }else if(response.statusCode==500){
                showToast("Session expires")
            }else{
                showToast(response.errorMessages)
            }
        })
    }

    fun replaceFragmentToContainer(fragment: Fragment, container: Int, tag: String = "") {
        requireActivity()!!.supportFragmentManager.beginTransaction()
            .replace(
                container,
                fragment,
                tag
            ).disallowAddToBackStack()
            .commit()
    }

    fun addFragmentToContainer(fragment: Fragment,container:Int,tag:String=""){

        requireActivity()!!.supportFragmentManager.beginTransaction()
                ?.add(
                        container,
                        fragment,
                    tag
                )?.addToBackStack(tag)
                ?.commit()
    }

    fun hideKeyboard(view: View,context: Context) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



}