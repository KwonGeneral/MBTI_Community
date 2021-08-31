package com.kwon.mbti_community.z_common.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.kwon.mbti_community.board.view.BoardUpdateActivity
import com.kwon.mbti_community.chain.view.ChainActivity
import com.kwon.mbti_community.login.view.LoginActivity
import com.kwon.mbti_community.mypage.view.MypageOtherProfileActivity
import com.kwon.mbti_community.mypage.view.MypageProfileUpdateActivity
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
        intent.putExtra("password", hash["password"])
        intent.putExtra("profile", hash["profile"])
        intent.putExtra("user_type", hash["user_type"])
        intent.putExtra("message", hash["message"])
        intent.putExtra("move_status", hash["move_status"])

        activity.startActivity(intent)
        activity.finish()
        return
    }

    override fun profile_update_move(activity: Activity, hash:HashMap<String, String>) {
        val intent = Intent(activity, MypageProfileUpdateActivity::class.java)
        intent.putExtra("access_token", hash["access_token"])
        intent.putExtra("username", hash["username"])
        intent.putExtra("nickname", hash["nickname"])
        intent.putExtra("password", hash["password"])
        intent.putExtra("profile", hash["profile"])
        intent.putExtra("user_type", hash["user_type"])
        intent.putExtra("message", hash["message"])
        intent.putExtra("move_status", hash["move_status"])

        activity.startActivity(intent)
        activity.finish()
        return
    }

    override fun board_update_move(activity: Activity, hash:HashMap<String, String>) {
        val intent = Intent(activity, BoardUpdateActivity::class.java)
        intent.putExtra("access_token", hash["access_token"])
        intent.putExtra("username", hash["username"])
        intent.putExtra("board_id", hash["board_id"])
        intent.putExtra("board_title", hash["board_title"])
        intent.putExtra("board_content", hash["board_content"])
        intent.putExtra("comment_id", hash["comment_id"])
        intent.putExtra("comment_content", hash["comment_content"])

        activity.startActivity(intent)
        activity.finish()
        return
    }

    override fun other_profile_move(activity: Activity, hash:HashMap<String, String>) {
        val intent = Intent(activity, MypageOtherProfileActivity::class.java)
        intent.putExtra("access_token", hash["access_token"])
        intent.putExtra("username", hash["username"])
        intent.putExtra("other_username", hash["other_username"])
        intent.putExtra("other_profile", hash["other_profile"])

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
        activity.finish()
        return
    }

}