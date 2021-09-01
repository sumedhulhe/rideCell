package com.example.testapplication.dashboard.view

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.testapplication.R
import com.example.testapplication.baseclasses.BaseActivity
import com.example.testapplication.map.view.MapsFragment
import com.example.testapplication.myprofile.view.MyProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class DashboardActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    override fun onRetry(serviceID: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.setSelectedItemId(R.id.map_nav)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when (item.itemId){
           R.id.map_nav->{
               addFragmentToDashboard(MapsFragment(),"MapsFragment")
               return true
           }
            R.id.profile_nav->{
                addFragmentToDashboard(MyProfileFragment(),"MyProfileFragment")
                return true
            }
       }
        return false
    }

}