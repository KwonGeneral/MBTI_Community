package com.kwon.mbti_community.z_common.model

interface PasswordCheckInterface {
    fun check(regex: String, str:String): MatchResult?
    fun password_aes256(password:String): String?
    fun check_aes256(encText:String): String?
}