package com.kwon.mbti_community.login.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kwon.mbti_community.R
import com.kwon.mbti_community.z_common.view.MoveActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            MoveActivity().chain_move(this)
            finish()
        }

        login_signup_btn.setOnClickListener {
            MoveActivity().signup_move(this)
        }
    }
}