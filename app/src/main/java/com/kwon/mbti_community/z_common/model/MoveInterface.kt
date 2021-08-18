package com.kwon.mbti_community.z_common.model

import android.app.Activity

interface MoveInterface {
    fun login_move(activity: Activity, key: String? = null, value: String? = null)
    fun chain_move(activity: Activity, key: String? = null, value: String? = null)
    fun signup_move(activity: Activity, key: String? = null, value: String? = null)
}