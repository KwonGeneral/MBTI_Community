package com.kwon.mbti_community.signup.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupInterface {
    // 회원가입
    @POST("/account/user/")
    fun createUser(
        @Body parameters: HashMap<String, String>
    ): Call<SignupData>
}