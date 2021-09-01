package com.example.testapplication.baseclasses

import androidx.room.Room
import com.example.testapplication.di.ApplicationComponent
import com.example.testapplication.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component: ApplicationComponent =
            DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)
        return component
    }
    companion object {
        lateinit var myInstance: BaseApplication

        fun getInstance(): BaseApplication {
            return myInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        myInstance = this
    }
}