package com.kwon.mbti_community.login.view

import android.Manifest
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.kwon.mbti_community.R
import com.kwon.mbti_community.login.model.LoginData
import com.kwon.mbti_community.login.model.LoginInterface
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import com.kwon.mbti_community.z_common.view.PasswordCheck
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class LoginActivity : AppCompatActivity() {
    var temp_login_username:String = ""
    var temp_login_password:String = ""
    var login_username_count = 0
    var login_password_count = 0

    // 권한 체크 : 저장소 읽기, 인터넷, 네트워크, 위치정보, GPS, 카메라, 저장소 읽기, 절전모드 방지
    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.WAKE_LOCK,
    )

    var app_file_path: String? = null

    fun saveTokenFile(access: String) {
        val path = app_file_path
        Log.d("path",path.toString())
//        토큰 저장 경로
        val token_file = File("$path/token.token")
        Log.d("token",token_file.toString())

//        경로에 있는 파일에 토큰 저장
        token_file.bufferedWriter().use {
//            it.write("access_token: $access\nrefresh_token: $refresh\nusername: $username")
            it.write(access)
            Log.d("token/bufferedWriter-->",token_file.toString())
        }
        return
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app_file_path = this.getExternalFilesDir(null).toString()

        // API 셋팅
        val conn = Connect().connect("")
        val login_api: LoginInterface = conn.create(LoginInterface::class.java)

        val get_signup_status = intent.getStringExtra("signup_status")

        // 회원가입 성공
        if(get_signup_status == "success") {
            val snack: Snackbar = Snackbar
                .make(findViewById<ConstraintLayout>(R.id.login_all_layout), "회원가입을 환영합니다", 2000)
                .setBackgroundTint(Color.parseColor("#ffffff"))
                .setTextColor(Color.parseColor("#ba000000"))

            val snack_view = snack.view
            val params = snack_view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            snack_view.layoutParams = params
            snack.show()
        }

        // 텍스트 변환 감지 : 아이디
        login_username_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){ // 입력난에 변화가 있을 시 조치
                temp_login_username = login_username_input.text.toString()

                // 아이디 체크
                val username_reg = PasswordCheck().check("^[0-9a-z].{1,15}\$", temp_login_username)

                if(username_reg != null) {
                    login_username_count = 1
                    login_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                } else {
                    login_username_count = 0
                    login_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                }
            }

            override fun afterTextChanged(arg0: Editable) { // 입력이 끝났을 때 조치
                temp_login_username = login_username_input.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // 입력하기 전에 조치
                temp_login_username = login_username_input.text.toString()
            }
        })

        // 텍스트 변환 감지 : 비밀번호
        login_password_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){ // 입력난에 변화가 있을 시 조치
                temp_login_password = login_password_input.text.toString()

                // 패스워드 체크
                val password_reg = PasswordCheck().check(
                    "^([a-zA-Z!@#\$%^&*0-9]).{1,15}\$",
                    temp_login_password
                )

                if(password_reg != null) {
                    login_password_count = 1
                    login_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                } else {
                    login_password_count = 0
                    login_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                }

            }

            override fun afterTextChanged(arg0: Editable) { // 입력이 끝났을 때 조치
                temp_login_password = login_password_input.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // 입력하기 전에 조치
                temp_login_password = login_password_input.text.toString()
            }
        })

        login_btn.setOnClickListener {
            if(login_username_count == 0) {
                login_status_message.text = "아이디가 올바르지 않습니다."
                login_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                return@setOnClickListener
            }
            if(login_password_count == 0) {
                login_status_message.text = "비밀번호가 올바르지 않습니다."
                login_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                return@setOnClickListener
            }

            // 회원가입 API 통신
            val parameter:HashMap<String, String> = HashMap()
            val password_aes = PasswordCheck().password_aes256(temp_login_password)
            if(password_aes != null) {
                parameter["password"] = password_aes
            } else {
                login_password_count = 0
                login_status_message.text = "비밀번호가 올바르지 않습니다."
                login_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                return@setOnClickListener
            }
            parameter["username"] = temp_login_username

            login_api.login(parameter).enqueue(object: Callback<LoginData> {
                override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                    val body = response.body()

                    Log.d("TEST", "Login - login 통신성공 바디 -> $body")

                    if(body != null) {
                        if(body.code == "E0005") {
                            login_status_message.text = "아이디 또는 비밀번호가 틀렸습니다"
                            login_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                            login_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                        } else {
                            val hash: HashMap<String, String> = HashMap()
                            saveTokenFile(body.data.access_token)
                            hash["access_token"] = body.data.access_token
                            hash["username"] = body.data.user_info.username
                            hash["nickname"] = body.data.user_info.nickname
                            hash["profile"] = body.data.user_info.profile
                            hash["user_type"] = body.data.user_info.user_type
                            hash["message"] = body.data.user_info.message

                            MoveActivity().chain_move(this@LoginActivity, hash)
                        }
                    } else {
                        login_status_message.text = "아이디 또는 비밀번호가 틀렸습니다"
                        login_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                        login_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                    }

                }

                override fun onFailure(call: Call<LoginData>, t: Throwable) {
                    Log.d("TEST", "Login - login 통신실패 에러 -> " + t.message)
                }
            })

//            finish()
        }

        login_signup_btn.setOnClickListener {
            MoveActivity().signup_move(this)
        }
    }
}