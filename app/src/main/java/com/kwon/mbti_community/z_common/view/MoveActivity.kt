package com.kwon.mbti_community.z_common.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.kwon.mbti_community.chain.view.ChainActivity
import com.kwon.mbti_community.login.view.LoginActivity
import com.kwon.mbti_community.signup.view.SignupActivity
import com.kwon.mbti_community.z_common.model.MoveInterface

class MoveActivity : MoveInterface, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun chain_move(activity: Activity, hash:HashMap<String, String>) {
        val intent = Intent(activity, ChainActivity::class.java)
        intent.putExtra("access_token", hash["access_token"])
        intent.putExtra("username", hash["username"])
        intent.putExtra("nickname", hash["nickname"])
        intent.putExtra("profile", hash["profile"])
        intent.putExtra("user_type", hash["user_type"])
        intent.putExtra("message", hash["message"])

        activity.startActivity(intent)
        activity.finish()
        return
    }

    override fun login_move(activity: Activity, key: String?, value: String?) {
        val intent = Intent(activity, LoginActivity::class.java)
        if(key != null && value != null){
            intent.putExtra(key, value)
        }
        activity.startActivity(intent)
        activity.finish()
        return
    }

    override fun signup_move(activity: Activity, key: String?, value: String?) {
        val intent = Intent(activity, SignupActivity::class.java)
        if(key != null && value != null){
            intent.putExtra(key, value)
        }
        activity.startActivity(intent)
//        activity.finish()
        return
    }

}