package com.example.testapplication.di.modules


import com.example.testapplication.dashboard.view.DashboardActivity
import com.example.testapplication.login.view.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
open abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun dashboardActivity(): DashboardActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun logindActivity(): LoginActivity

}