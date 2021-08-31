package com.kwon.mbti_community.login.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kwon.mbti_community.R
import com.kwon.mbti_community.mypage.model.GetUserData
import com.kwon.mbti_community.mypage.model.MypageInterface
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val app_file_path = this.getExternalFilesDir(null).toString()

        // 토큰 확인
        val token_file = File("$app_file_path/token.token")
        if (token_file.isFile == true) {
            val file_access_token = token_file.readText().split("\n")[0]
            val file_username = token_file.readText().split("\n")[1]
            val file_auto_login = token_file.readText().split("\n")[2]

            if(file_auto_login == "1") {
                val conn = Connect().connect(file_access_token)
                val mypage_api: MypageInterface = conn.create(MypageInterface::class.java)

                // API 통신 : 유저 정보 가져오기
                mypage_api.getUserData(file_username).enqueue(object: Callback<GetUserData> {
                    override fun onResponse(call: Call<GetUserData>, response: Response<GetUserData>) {
                        val body = response.body()
                        val hash:HashMap<String, String> = HashMap()
                        if(body != null) {
                            hash["access_token"] = body.data.access_token
                            hash["username"] = body.data.user_info[0].username
                            hash["nickname"] = body.data.user_info[0].nickname
                            hash["password"] = body.data.user_info[0].password
                            hash["profile"] = body.data.user_info[0].profile
                            hash["user_type"] = body.data.user_info[0].user_type
                            hash["message"] = body.data.user_info[0].message
                            hash["move_status"] = "1"

                            MoveActivity().chain_move(this@StartActivity, hash)
                            finish()
                        } else {
                            MoveActivity().login_move(this@StartActivity)
                            finish()
                        }
//                        Log.d("TEST", "getUserData 통신성공 바디 -> $body")
                    }

                    override fun onFailure(call: Call<GetUserData>, t: Throwable) {
//                        Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
                        MoveActivity().login_move(this@StartActivity)
                        finish()
                    }
                })
            } else {
                MoveActivity().login_move(this@StartActivity)
                finish()
            }
        } else {
            MoveActivity().login_move(this@StartActivity)
            finish()
        }

    }
}