package com.kwon.mbti_community.z_common.connect

import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kwon.mbti_community.z_common.model.ConnectInterface
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception


class Connect : ConnectInterface {
    override fun connect(access_token:String?): Retrofit {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val request:Request
            if(access_token != null) {
                request = chain.request().newBuilder().addHeader("PrivatKey", "Kwonputer").addHeader("AccessToken", access_token).build()
            }else {
                request = chain.request().newBuilder().addHeader("PrivatKey", "Kwonputer").build()
            }
            chain.proceed(request)
        }

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
//            .baseUrl("http://192.168.0.38:3333")
            .baseUrl("https://kwonputer.com")
            .client(httpClient.build())
            .build()

        return retrofit
    }
}