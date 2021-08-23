package com.kwon.mbti_community.z_common.model

import retrofit2.Retrofit

interface ConnectInterface {
    fun connect(access_token:String?): Retrofit
}