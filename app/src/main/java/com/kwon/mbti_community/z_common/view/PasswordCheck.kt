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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return encText
    }

    override fun check_aes256(encText:String): String? {
        var decText: String? = null
        try {
            val key = "kwonputer7777777"
            decText = decByKey(key, encText)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return decText
    }
}