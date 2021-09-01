package com.example.testapplication.di.modules

import androidx.annotation.NonNull
import com.example.testapplication.utils.Constants.Companion.BASEURL
import com.example.testapplication.network.APIinterface
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager.Companion.TOKEN
import com.example.testapplication.sharedprefrence.SharedPrefrenceManager.Companion.readString

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()


        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request: Request.Builder = original.newBuilder()
            request.header("Content-Type", "application/json")
            request.header("Authorization", readString(TOKEN)!!)


            chain.proceed(request.build())
        }
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)

        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASEURL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIinterface(@NonNull retrofit: Retrofit): APIinterface {
        return retrofit.create(APIinterface::class.java)
    }
}