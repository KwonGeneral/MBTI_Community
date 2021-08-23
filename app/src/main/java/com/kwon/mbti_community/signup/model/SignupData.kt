package com.kwon.mbti_community.signup.model

data class SignupData(
    val code: String,
    val data: ArraySignupData?,
    val message: String
)

data class ArraySignupData(
    val created_at: String,
    val id: Int,
    val is_active: Boolean,
    val message: Any,
    val nickname: String,
    val password: String,
    val profile: String,
    val updated_at: String,
    val user_type: Any,
    val username: String
)