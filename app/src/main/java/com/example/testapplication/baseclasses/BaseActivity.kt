package com.example.testapplication.baseclasses


import android.content.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.testapplication.utils.Constants.Companion.LOGIN_SERVICE_ID
import com.example.testapplication.R
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity

import java.net.UnknownHostException



abstract class BaseActivity : DaggerAppCompatActivity() {

    var progressDialog: BaseDialog? = null



    fun startNewActivity(className: Class<*>) {
        val intent = Intent(this, className)
        startActivity(intent)
    }



    fun showProgressDialog(message: String) {
        getProgressDialogInstance()?.let {
            var progressDialog = it
            progressDialog.show()
        }
        progressDialog?.findViewById<TextView>(R.id.progress_msg).apply { this?.text = message }

    }

    private fun getProgressDialogInstance(): BaseDialog? {
        progressDialog?.let {
            return it
        }

        progressDialog = BaseDialog(this, false)
        return progressDialog
    }

    fun dismissProgressDialog() {
        getProgressDialogInstance()?.let {
            var progressDialog = it
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }



    fun observeErrorResponse(
        viewModel: BaseviewModel,
        view: View
    ) {
        viewModel.getErrorResponse().observe(this, androidx.lifecycle.Observer { response ->
            dismissProgressDialog()
            if (response.errorType is UnknownHostException) {
                val snackbar = Snackbar.make(
                    view,
                    getString(R.string.no_internet_connection),
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar.setAction(getString(R.string.retry), View.OnClickListener { view ->
                    onRetry(response.serviceID)
                })
                snackbar.show()
            }
            else if(response.serviceID.equals(LOGIN_SERVICE_ID)){
                showToast("Login Failed")
            }
        })
    }

    abstract fun onRetry(serviceID: String)



    fun addFragmentToDashboard(fragment: Fragment, tag: String) {

        this.supportFragmentManager.beginTransaction()
            .replace(
                R.id.dashboard_fl,
                fragment, tag
            )
            .commit()
    }
    fun addFragment(fragment: Fragment, tag: String) {

        this.supportFragmentManager.beginTransaction()
            .add(
                android.R.id.content,
                fragment, tag
            ).addToBackStack(tag)
            .commit()
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
            .setTitle(context!!.resources.getString(R.string.app_name))
            .setMessage(message)
            .setCancelable(cancelable)
            .setPositiveButton(positiveButtonLabel, positiveOnClickListener)
            .setNegativeButton(negativeButtonLabel, negativeOnClickListener)
            .show()
    }
}