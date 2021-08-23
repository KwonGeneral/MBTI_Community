package com.kwon.mbti_community.login.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginInterface {
    // 로그인
    @POST("/account/user/login/")
    fun login(
        @Body parameters: HashMap<String, String>
    ): Call<LoginData>
}