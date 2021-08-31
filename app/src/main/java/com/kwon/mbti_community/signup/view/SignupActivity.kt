package com.kwon.mbti_community.signup.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.kwon.mbti_community.R
import com.kwon.mbti_community.signup.model.SignupData
import com.kwon.mbti_community.signup.model.SignupInterface
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import com.kwon.mbti_community.z_common.view.PasswordCheck
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var temp_select_mbti:String = ""
    var temp_username:String = ""
    var temp_password:String = ""
    var temp_password_check:String = ""
    var temp_nickname:String = ""
    var username_count = 0
    var password_count = 0
    var mbti_count = 0
    var nickname_count = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // ADS 설정
        var mAdView : AdView
        // 1. ADS 초기화
        MobileAds.initialize(
            this
        ) { }
        // 2. 광고 띄우기
        mAdView = findViewById(R.id.signup_adv)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Input 길이 제한
        fun EditText.setMaxLength(maxLength: Int){
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
        signup_nickname_input.setMaxLength(20)
        signup_username_input.setMaxLength(20)
        signup_password_input.setMaxLength(20)
        signup_password_check_input.setMaxLength(20)
        signup_nickname_input.maxLines = 8
        signup_username_input.maxLines = 8
        signup_password_input.maxLines = 8
        signup_password_check_input.maxLines = 8

        // 프로그레스바 설정
        val signup_progress_layout = findViewById<RelativeLayout>(R.id.signup_progress_layout)
        signup_progress_layout.bringToFront()

        // API 셋팅
        val conn = Connect().connect("")
        val signup_api: SignupInterface = conn.create(SignupInterface::class.java)

        // ================================  스피너 ================================
        // 스피너 Array 불러오기
        val spinner: Spinner = findViewById(R.id.mbti_spinner)

        // 문자열 배열과 기본 스피너 레이아웃을 사용하여 ArrayAdapter 만들기
        ArrayAdapter.createFromResource(
            this,
            R.array.mbti_select_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // 선택 목록이 나타날 때 사용할 레이아웃 지정
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // 스피너에 어댑터 적용
            spinner.adapter = adapter
        }

        // 인터페이스 구현 지정, 아래 오버라이딩 가능
        spinner.onItemSelectedListener = this
        // ========================================================================

        // 화면 어디든 클릭 시, 키보드 내리기
        signup_all_layout.setOnClickListener {
            // 키보드 내리기
            val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스 해제
            signup_username_input.clearFocus()
            signup_nickname_input.clearFocus()
            signup_password_input.clearFocus()
            signup_password_check_input.clearFocus()
        }
        mbti_spinner.setOnTouchListener { _, _ ->
            // 키보드 내리기
            val mInputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            false
        }

        // 텍스트 변환 감지 : 아이디
        signup_username_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){ // 입력난에 변화가 있을 시 조치
                temp_username = signup_username_input.text.toString()

                // 아이디 체크
                val username_reg = PasswordCheck().check("^[0-9a-z].{1,15}\$", temp_username)

                if(username_reg != null) {
                    username_count = 1
                    signup_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                } else {
                    username_count = 0
                    signup_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                }
            }

            override fun afterTextChanged(arg0: Editable) { // 입력이 끝났을 때 조치
                temp_username = signup_username_input.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // 입력하기 전에 조치
                temp_username = signup_username_input.text.toString()
            }
        })
        // 텍스트 변환 감지 : 비밀번호
        signup_password_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){ // 입력난에 변화가 있을 시 조치
                temp_password = signup_password_input.text.toString()

                // 패스워드 체크
                val password_reg = PasswordCheck().check(
                    "^([a-zA-Z!@#\$%^&*0-9]).{1,15}\$",
                    temp_password
                )

                if(password_reg != null) {
//                    Log.d("TEST", "정규표현식 통과")
                    signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                } else {
//                    Log.d("TEST", "정규표현식 통과 X")
                    signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                }

                if(temp_password_check != "" && temp_password != "") {
                    if(temp_password != temp_password_check) {
                        password_count = 0
                        signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                        signup_password_check_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                    } else {
                        signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                        signup_password_check_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                        password_count = 1
                    }
                }

            }

            override fun afterTextChanged(arg0: Editable) { // 입력이 끝났을 때 조치
                temp_password = signup_password_input.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // 입력하기 전에 조치
                temp_password = signup_password_input.text.toString()
            }
        })
        // 텍스트 변환 감지 : 비밀번호 확인
        signup_password_check_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){ // 입력난에 변화가 있을 시 조치
                temp_password_check = signup_password_check_input.text.toString()

                if(temp_password_check != "" && temp_password != "") {
                    if(temp_password != temp_password_check) {
                        password_count = 0
                        signup_password_check_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))

                        // 패스워드 체크
                        val password_reg = PasswordCheck().check(
                            "^([a-zA-Z!@#\$%^&*0-9]).{1,15}\$",
                            temp_password
                        )

                        if(password_reg != null) {
//                            Log.d("TEST", "정규표현식 통과")
                            signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                        } else {
//                            Log.d("TEST", "정규표현식 통과 X")
                            signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                        }
                    } else {
                        signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                        signup_password_check_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                        password_count = 1
                    }
                }
            }

            override fun afterTextChanged(arg0: Editable) { // 입력이 끝났을 때 조치
                temp_password_check = signup_password_check_input.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // 입력하기 전에 조치
                temp_password_check = signup_password_check_input.text.toString()
            }
        })
        // 텍스트 변환 감지 : 닉네임
        signup_nickname_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){ // 입력난에 변화가 있을 시 조치
                temp_nickname = signup_nickname_input.text.toString()

                // 닉네임 체크
                val nickname_reg = PasswordCheck().check("^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,12}\$", temp_nickname)

                if(nickname_reg != null) {
                    nickname_count = 1
                    signup_nickname_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#53AF6D"))
                } else {
                    nickname_count = 0
                    signup_nickname_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#66CDCDCD"))
                }
            }

            override fun afterTextChanged(arg0: Editable) { // 입력이 끝났을 때 조치
                temp_nickname = signup_nickname_input.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // 입력하기 전에 조치
                temp_nickname = signup_nickname_input.text.toString()
            }
        })

        // 회원가입 버튼 클릭
        signup_submit_btn.setOnClickListener {
            // 키보드 내리기
            val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if(username_count == 0) {
                val snack: Snackbar = Snackbar
                    .make(findViewById<ConstraintLayout>(R.id.signup_all_layout), "아이디가 올바르지 않습니다", 2000)
                    .setBackgroundTint(Color.parseColor("#ffffff"))
                    .setTextColor(Color.parseColor("#ba000000"))

                val snack_view = snack.view
                val params = snack_view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                snack_view.layoutParams = params
                snack.show()

                signup_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))

                return@setOnClickListener
            }
            if(password_count == 0) {
                val snack: Snackbar = Snackbar
                    .make(findViewById<ConstraintLayout>(R.id.signup_all_layout), "비밀번호를 확인해주세요", 2000)
                    .setBackgroundTint(Color.parseColor("#ffffff"))
                    .setTextColor(Color.parseColor("#ba000000"))

                val snack_view = snack.view
                val params = snack_view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                snack_view.layoutParams = params
                snack.show()
                
                signup_password_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                signup_password_check_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                
                return@setOnClickListener
            }
            if(nickname_count == 0) {
                val snack: Snackbar = Snackbar
                    .make(findViewById<ConstraintLayout>(R.id.signup_all_layout), "닉네임을 확인해주세요", 2000)
                    .setBackgroundTint(Color.parseColor("#ffffff"))
                    .setTextColor(Color.parseColor("#ba000000"))

                val snack_view = snack.view
                val params = snack_view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                snack_view.layoutParams = params
                snack.show()

                signup_nickname_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))

                return@setOnClickListener
            }
            if(mbti_count == 0) {
                val snack: Snackbar = Snackbar
                    .make(findViewById<ConstraintLayout>(R.id.signup_all_layout), "MBTI를 선택해주세요", 2000)
                    .setBackgroundTint(Color.parseColor("#ffffff"))
                    .setTextColor(Color.parseColor("#ba000000"))

                val snack_view = snack.view
                val params = snack_view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                snack_view.layoutParams = params
                snack.show()

                return@setOnClickListener
            }

            val password_aes = PasswordCheck().password_aes256(temp_password)

            // 회원가입 API 통신
            val parameter:HashMap<String, String> = HashMap()
            if(password_aes != null) {
                parameter["password"] = password_aes
            } else {
                return@setOnClickListener
            }

            signup_progress_layout.visibility = View.VISIBLE

            parameter["username"] = temp_username
            parameter["nickname"] = temp_nickname
            parameter["user_type"] = temp_select_mbti
            parameter["message"] = "상태 메세지를 입력해주세요."

            signup_api.createUser(parameter).enqueue(object: Callback<SignupData> {
                override fun onResponse(call: Call<SignupData>, response: Response<SignupData>) {
                    signup_progress_layout.visibility = View.GONE
                    val body = response.body()

                    if(body != null) {
                        if(body.code == "E0003") {
                            // 닉네임 중복 오류
                            val snack: Snackbar = Snackbar
                                .make(findViewById<ConstraintLayout>(R.id.signup_all_layout), "중복된 닉네임이 존재합니다.", 2000)
                                .setBackgroundTint(Color.parseColor("#ffffff"))
                                .setTextColor(Color.parseColor("#ba000000"))

                            val snack_view = snack.view
                            val params = snack_view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.TOP
                            snack_view.layoutParams = params
                            snack.show()

                            signup_nickname_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))

                        } else if(body.code == "E0004") {
                            // 아이디 중복 오류
                            val snack: Snackbar = Snackbar
                                .make(findViewById<ConstraintLayout>(R.id.signup_all_layout), "중복된 아이디가 존재합니다.", 2000)
                                .setBackgroundTint(Color.parseColor("#ffffff"))
                                .setTextColor(Color.parseColor("#ba000000"))

                            val snack_view = snack.view
                            val params = snack_view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.TOP
                            snack_view.layoutParams = params
                            snack.show()

                            signup_username_circle.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                        } else if(body.code == "S0001") {
                            MoveActivity().login_move(this@SignupActivity, "signup_status", "success")
                        }
                    }

//                    Log.d("TEST", "Signup - createUser 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<SignupData>, t: Throwable) {
                    signup_progress_layout.visibility = View.GONE
//                    Log.d("TEST", "Signup - createUser 통신실패 에러 -> " + t.message)
                }
            })

        }
    }

    // 스피너 제어
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // 항목이 선택되었습니다. 다음을 사용하여 선택한 항목을 검색할 수 있습니다.
        // parent.getItemAtPosition(pos)
        val position = parent.getItemAtPosition(pos)

        temp_select_mbti = position.toString()

        if(temp_select_mbti == "MBTI를 선택해주세요") {
            mbti_count = 0
        } else {
            mbti_count = 1
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // 다른 인터페이스 콜백
    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        Log.d("TEST", "ChainActivity - onBackPressed")
        MoveActivity().login_move(this)
        finish()
    }
}