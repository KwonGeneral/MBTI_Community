package com.kwon.mbti_community.z_common.view

import android.util.Log
import com.kwon.mbti_community.z_common.model.PasswordCheckInterface

class PasswordCheck: PasswordCheckInterface {
    override fun check(regex: String, str: String): MatchResult? {
        val reg = Regex(regex)
        return reg.matchEntire(str)
    }

    override fun password_aes256(password:String): String? {
        var encText: String? = null
        try {
            val key = "kwonputer7777777"
            encText = encByKey(key, password).toString()
            val decText: String? = decByKey(key, encText)
            Log.d("TEST", "암호화 결과 : $encText")
            Log.d("TEST", "복호화 결과 : $decText")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return encText
    }
}