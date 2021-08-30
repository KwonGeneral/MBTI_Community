package com.kwon.mbti_community.login.model

data class LoginData(
    val code: String,
    val data: ArrayLoginData,
    val message: String
)

data class ArrayLoginData(
    val access_token: String,
    val user_info: UserInfo
)

data class UserInfo(
    val created_at: String,
    val id: Int,
    val is_active: Boolean,
    val message: String,
    val fcm_token: String,
    val nickname: String,
    val password: String,
    val profile: String,
    val updated_at: String,
    val user_type: String,
    val username: String
)