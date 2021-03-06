package com.kwon.mbti_community.mypage.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.kwon.mbti_community.R
import com.kwon.mbti_community.mypage.model.GetUserData
import com.kwon.mbti_community.mypage.model.MypageInterface
import com.kwon.mbti_community.mypage.model.UpdateUserInfoData
import com.kwon.mbti_community.mypage.model.UpdateUserProfileData
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import com.kwon.mbti_community.z_common.view.PasswordCheck
import kotlinx.android.synthetic.main.activity_mypage_profile_update.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime

class MypageProfileUpdateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // 값 전달 변수
    var share_access_token = ""
    var share_username = ""
    var share_nickname = ""
    var share_password = ""
    var share_profile = ""
    var share_user_type = ""
    var share_message = ""

    // 전역변수
    var temp_select_mbti:String = ""
    var mbti_count = 1
    var app_file_path: String? = null
    var temp_profile_file:File? = null
    var temp_profile_file_name:String = ""
    var profile_image_change_count = 0

    // 뒤로가기 연속 클릭 대기 시간
    var mBackWait:Long = 0

    // 권한 체크 : 저장소 읽기, 인터넷, 네트워크, 위치정보, GPS, 카메라, 저장소 읽기, 절전모드 방지
    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.WAKE_LOCK,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_profile_update)
        requestPermissions(permission_list, 0)
//        Log.d("TEST", "MypageProfileUpdateActivity - onCreate")
        app_file_path = this.getExternalFilesDir(null).toString()

        // ADS 설정
        var mAdView : AdView
        // 1. ADS 초기화
        MobileAds.initialize(
            this
        ) { }
        // 2. 광고 띄우기
        mAdView = findViewById(R.id.mypage_profile_adv)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // 프로그레스바 설정
        val mypage_update_progress_layout = findViewById<LinearLayout>(R.id.mypage_update_progress_layout)
        mypage_update_progress_layout.bringToFront()

        // 값 가져오기
        share_access_token = intent.getStringExtra("access_token").toString()
        share_username = intent.getStringExtra("username").toString()
        share_nickname = intent.getStringExtra("nickname").toString()
        share_password = intent.getStringExtra("password").toString()
        share_profile = intent.getStringExtra("profile").toString()
        share_user_type = intent.getStringExtra("user_type").toString()
        share_message = intent.getStringExtra("message").toString()

        mypage_profile_nickname_input.setText(share_nickname)
        mypage_profile_message_input.setText(share_message)
        mypage_profile_username.text = share_username

        // Input 길이 제한
        fun EditText.setMaxLength(maxLength: Int){
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
        mypage_profile_nickname_input.setMaxLength(20)
        mypage_profile_message_input.setMaxLength(20)
        mypage_profile_password_input.setMaxLength(20)
        mypage_profile_password_check_input.setMaxLength(20)
        mypage_profile_nickname_input.maxLines = 8
        mypage_profile_message_input.maxLines = 8
        mypage_profile_password_input.maxLines = 8
        mypage_profile_password_check_input.maxLines = 8

        // Glide로 이미지 표시하기
        val user_profile = findViewById<ImageView>(R.id.mypage_profile_image)
        Glide.with(this)
            .load(share_profile)
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)
            .into(user_profile)

        user_profile.setBackgroundResource(R.drawable.image_background_border)
        user_profile.clipToOutline = true

//        user_profile.outlineProvider = object : ViewOutlineProvider() {
//            override fun getOutline(view: View, outline: Outline) {
//                outline.setRoundRect(0, 0, view.width, view.height, 20f)
//            }
//        }

        // 전체 레이아웃 클릭 시, 포커스 해제
        mypage_profile_all_layout.setOnClickListener {
            // 키보드 내리기
            val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스 해제
            mypage_profile_nickname_input.clearFocus()
            mypage_profile_message_input.clearFocus()
            mypage_profile_password_input.clearFocus()
            mypage_profile_password_check_input.clearFocus()
        }

        mypage_profile_close_btn.setOnClickListener {
            var kkk_hash:HashMap<String, String> = HashMap()
            kkk_hash["access_token"] = share_access_token
            kkk_hash["username"] = share_username
            kkk_hash["nickname"] = share_nickname
            kkk_hash["password"] = share_password
            kkk_hash["profile"] = share_profile
            kkk_hash["user_type"] = share_user_type
            kkk_hash["message"] = share_message
            kkk_hash["move_status"] = "4"
            MoveActivity().chain_move(this, kkk_hash)
        }

        fun saveTokenFile(access: String, username: String, temp_auto_check_count: Int, nickname:String, user_type:String, profile:String) {
            val path = app_file_path
            // 토큰 저장 경로
            val token_file = File("$path/token.token")

            // 경로에 있는 파일에 토큰 저장
            token_file.bufferedWriter().use {
                it.write("$access\n$username\n$temp_auto_check_count\n$nickname\n$user_type\n$profile")
            }
            return
        }

        // API 셋팅
        val access_token = share_access_token
        val conn = Connect().connect(access_token)
        val mypage_api: MypageInterface = conn.create(MypageInterface::class.java)

        // 프로필 수정 버튼 클릭 이벤트 - 수정 완료
        profile_update_submit_btn.setOnClickListener {
            // 키보드 내리기
            val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스 해제
            mypage_profile_nickname_input.clearFocus()
            mypage_profile_message_input.clearFocus()
            mypage_profile_password_input.clearFocus()
            mypage_profile_password_check_input.clearFocus()

            var temp_password:String = mypage_profile_password_input.text.toString()
            var temp_password_change:String = mypage_profile_password_check_input.text.toString()
            val temp_nickname = mypage_profile_nickname_input.text.toString()
            val temp_user_type = temp_select_mbti
            val temp_message = mypage_profile_message_input.text.toString()

            if(temp_password != "") {
                // 현재 비밀번호 체크
                val password_reg = PasswordCheck().check(
                    "^([a-zA-Z!@#\$%^&*0-9]).{1,15}\$",
                    temp_password
                )
                if(password_reg != null) {
//                    Log.d("TEST", "password_reg 정규표현식 통과")
                } else {
//                    Log.d("TEST", "password_reg 정규표현식 통과 실패")
                    mypage_profile_status_message.text = "변경할 비밀번호가 올바르지 않습니다"
                    mypage_profile_password_input.setText("")
                    mypage_profile_password_check_input.setText("")
                    return@setOnClickListener
                }

                // 변경할 비밀번호 체크
                val password_change_reg = PasswordCheck().check(
                    "^([a-zA-Z!@#\$%^&*0-9]).{1,15}\$",
                    temp_password_change
                )
                if(password_change_reg != null) {
//                    Log.d("TEST", "password_change_reg 정규표현식 통과")
                } else {
//                    Log.d("TEST", "password_change_reg 정규표현식 통과 실패")
                    mypage_profile_status_message.text = "변경할 비밀번호가 올바르지 않습니다"
                    mypage_profile_password_input.setText("")
                    mypage_profile_password_check_input.setText("")
                    return@setOnClickListener
                }

                if(temp_password != PasswordCheck().check_aes256(share_password)) {
                    if(temp_password_change == "") {
                        mypage_profile_status_message.text = "변경할 비밀번호를 입력해주세요"
                        mypage_profile_password_input.setText("")
                        mypage_profile_password_check_input.setText("")
                        return@setOnClickListener
                    } else if (temp_password_change == PasswordCheck().check_aes256(share_password)) {
                        mypage_profile_status_message.text = "변경할 비밀번호가 현재 비밀번호와 동일합니다"
                        mypage_profile_password_input.setText("")
                        mypage_profile_password_check_input.setText("")
                        return@setOnClickListener
                    } else {
                        mypage_profile_status_message.text = "현재 비밀번호가 일치하지 않습니다"
                        mypage_profile_password_input.setText("")
                        mypage_profile_password_check_input.setText("")
                        return@setOnClickListener
                    }
                } else {
                    if(temp_password_change == "") {
                        mypage_profile_status_message.text = "변경할 비밀번호를 입력해주세요"
                        mypage_profile_password_input.setText("")
                        mypage_profile_password_check_input.setText("")
                        return@setOnClickListener
                    } else if (temp_password_change == PasswordCheck().check_aes256(share_password)) {
                        mypage_profile_status_message.text = "변경할 비밀번호가 현재 비밀번호와 동일합니다"
                        mypage_profile_password_input.setText("")
                        mypage_profile_password_check_input.setText("")
                        return@setOnClickListener
                    }

                    mypage_profile_status_message.text = ""
                    temp_password = PasswordCheck().password_aes256(temp_password).toString()
                    temp_password_change = PasswordCheck().password_aes256(temp_password_change).toString()
                }
            }

            if(temp_nickname != share_nickname) {
                // 닉네임 체크
                val nickname_reg = PasswordCheck().check("^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,12}\$", temp_nickname)
                if(nickname_reg != null) {
//                    Log.d("TEST", "nickname_reg 정규표현식 통과")
                }else {
//                    Log.d("TEST", "nickname_reg 정규표현식 통과 실패")
                    mypage_profile_status_message.text = "변경할 닉네임이 올바르지 않습니다"
                    return@setOnClickListener
                }
            }
            if(temp_message != share_message) {
                // 차후에 메세지 길이 제한할것.
            }

            if(mbti_count == 0) {
                mypage_profile_status_message.text = "MBTI를 선택해주세요"
                return@setOnClickListener
            }

            mypage_profile_status_message.text = ""
            mypage_profile_password_input.setText("")
            mypage_profile_password_check_input.setText("")

            // 유저 정보 수정 API
            mypage_update_progress_layout.visibility = View.VISIBLE
            var part: MultipartBody.Part? = null
            if(profile_image_change_count == 1) {
                var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"), temp_profile_file!!)
                part = MultipartBody.Part.createFormData("profile", temp_profile_file_name, requestBody)

                mypage_api.updateUserProfile(share_username, part).enqueue(object:
                    Callback<UpdateUserProfileData> {
                    override fun onResponse(call: Call<UpdateUserProfileData>, response: Response<UpdateUserProfileData>) {
                        val body = response.body()
//                        Log.d("TEST", "updateUserProfile 통신성공 바디 -> $body")

                        var kkk_parameters:HashMap<String, String> = HashMap()
                        kkk_parameters["nickname"] = temp_nickname
                        kkk_parameters["user_type"] = temp_user_type
                        kkk_parameters["message"] = temp_message

                        if(temp_password != "" && temp_password_change != "") {
                            kkk_parameters["password"] = temp_password_change
                        }

                        mypage_api.updateUserInfo(share_username, kkk_parameters).enqueue(object:
                            Callback<UpdateUserInfoData> {
                            override fun onResponse(call: Call<UpdateUserInfoData>, response: Response<UpdateUserInfoData>) {
                                val bodys = response.body()
//                                Log.d("TEST", "updateUserInfo 통신성공 바디 -> $bodys")

                                // API 통신 : 유저 정보 가져오기
                                mypage_api.getUserData(share_username).enqueue(object: Callback<GetUserData> {
                                    override fun onResponse(call: Call<GetUserData>, response: Response<GetUserData>) {
                                        mypage_update_progress_layout.visibility = View.GONE
                                        val bodyss = response.body()
                                        if(bodyss != null) {
                                            share_access_token = bodyss.data.access_token
                                            share_username = bodyss.data.user_info[0].username
                                            share_nickname = bodyss.data.user_info[0].nickname
                                            share_password = bodyss.data.user_info[0].password
                                            share_profile = bodyss.data.user_info[0].profile
                                            share_user_type = bodyss.data.user_info[0].user_type
                                            share_message = bodyss.data.user_info[0].message

                                            share_profile = share_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

                                            // 토큰 확인
                                            val app_file_path = this@MypageProfileUpdateActivity.getExternalFilesDir(null).toString()
                                            val token_file = File("$app_file_path/token.token")
                                            val auto_check_count = token_file.readText().split("\n")[2]
                                            saveTokenFile(share_access_token, share_username, auto_check_count.toInt(), share_nickname, share_user_type, share_profile)

                                            val snack: Snackbar = Snackbar
                                                .make(findViewById<ConstraintLayout>(R.id.mypage_profile_all_layout), "수정된 프로필을 반영했습니다.", 2000)
                                                .setBackgroundTint(Color.parseColor("#ffffff"))
                                                .setTextColor(Color.parseColor("#ba000000"))

                                            val snack_view = snack.view
                                            val params = snack_view.layoutParams as FrameLayout.LayoutParams
                                            params.gravity = Gravity.TOP
                                            snack_view.layoutParams = params
                                            snack.show()
                                        }
//                                        Log.d("TEST", "getUserData 통신성공 바디 -> $bodyss")
                                    }

                                    override fun onFailure(call: Call<GetUserData>, t: Throwable) {
                                        mypage_update_progress_layout.visibility = View.GONE
//                                        Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
                                    }
                                })
                            }

                            override fun onFailure(call: Call<UpdateUserInfoData>, t: Throwable) {
//                                Log.d("TEST", "updateUserInfo 통신실패 에러 -> " + t.message)
                            }
                        })
                    }

                    override fun onFailure(call: Call<UpdateUserProfileData>, t: Throwable) {
//                        Log.d("TEST", "updateUserProfile 통신실패 에러 -> " + t.message)
                    }
                })
            } else if (profile_image_change_count == 0) {
                var parameters:HashMap<String, String> = HashMap()
                parameters["nickname"] = temp_nickname
                parameters["user_type"] = temp_user_type
                parameters["message"] = temp_message

                if(temp_password != "" && temp_password_change != "") {
                    parameters["password"] = temp_password_change
                }

                mypage_api.updateUserInfo(share_username, parameters).enqueue(object:
                    Callback<UpdateUserInfoData> {
                    override fun onResponse(call: Call<UpdateUserInfoData>, response: Response<UpdateUserInfoData>) {
                        val bodyx = response.body()
//                        Log.d("TEST", "updateUserInfo 통신성공 바디 -> $bodyx")

                        // API 통신 : 유저 정보 가져오기
                        mypage_api.getUserData(share_username).enqueue(object: Callback<GetUserData> {
                            override fun onResponse(call: Call<GetUserData>, response: Response<GetUserData>) {
                                mypage_update_progress_layout.visibility = View.GONE
                                val bodyxx = response.body()
                                if(bodyxx != null) {
                                    share_access_token = bodyxx.data.access_token
                                    share_username = bodyxx.data.user_info[0].username
                                    share_nickname = bodyxx.data.user_info[0].nickname
                                    share_password = bodyxx.data.user_info[0].password
                                    share_profile = bodyxx.data.user_info[0].profile
                                    share_user_type = bodyxx.data.user_info[0].user_type
                                    share_message = bodyxx.data.user_info[0].message

                                    share_profile = share_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

                                    val snack: Snackbar = Snackbar
                                        .make(findViewById<ConstraintLayout>(R.id.mypage_profile_all_layout), "수정된 프로필을 반영했습니다.", 2000)
                                        .setBackgroundTint(Color.parseColor("#ffffff"))
                                        .setTextColor(Color.parseColor("#ba000000"))

                                    val snack_view = snack.view
                                    val params = snack_view.layoutParams as FrameLayout.LayoutParams
                                    params.gravity = Gravity.TOP
                                    snack_view.layoutParams = params
                                    snack.show()
                                }
//                                Log.d("TEST", "getUserData 통신성공 바디 -> $bodyxx")
                            }

                            override fun onFailure(call: Call<GetUserData>, t: Throwable) {
                                mypage_update_progress_layout.visibility = View.GONE
//                                Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
                            }
                        })
                    }

                    override fun onFailure(call: Call<UpdateUserInfoData>, t: Throwable) {
//                        Log.d("TEST", "updateUserInfo 통신실패 에러 -> " + t.message)
                    }
                })
            }
        }

        // 프로필 이미지 클릭 이벤트 - 프로필 변경
        user_profile.setOnClickListener {
            // 앨범에서 사진을 선택할 수 있는 액티비티를 실행한다.
            val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것)
            albumIntent.type = "image/*"
            // 선택할 파일의 타입을 지정(안드로이드 OS가 사전작업을 할 수 있도록)
            val mimeType = arrayOf("image/*")
            albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
            startActivityForResult(albumIntent, 0)
        }

        // ================================  스피너 ================================
        // 스피너 Array 불러오기
        val spinner: Spinner = findViewById(R.id.mypage_profile_mbti_spinner)

        // 문자열 배열과 기본 스피너 레이아웃을 사용하여 ArrayAdapter 만들기
        ArrayAdapter.createFromResource(
            this,
            R.array.mbti_select_array,
            R.layout.mypage_profile_mbit_spinner_select
        ).also { adapter ->
            // 선택 목록이 나타날 때 사용할 레이아웃 지정
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // 스피너에 어댑터 적용
            spinner.adapter = adapter
        }

        // 인터페이스 구현 지정, 아래 오버라이딩 가능
        fun mbti_default_position(mbti:String): Int {
            var temp_position = 0

            when(mbti) {
                "ISTJ" -> temp_position = 1
                "ISTP" -> temp_position = 2
                "ISFJ" -> temp_position = 3
                "ISFP" -> temp_position = 4
                "INTJ" -> temp_position = 5
                "INTP" -> temp_position = 6
                "INFJ" -> temp_position = 7
                "INFP" -> temp_position = 8
                "ESTJ" -> temp_position = 9
                "ESTP" -> temp_position = 10
                "ESFJ" -> temp_position = 11
                "ESFP" -> temp_position = 12
                "ENTJ" -> temp_position = 13
                "ENTP" -> temp_position = 14
                "ENFJ" -> temp_position = 15
                "ENFP" -> temp_position = 16
            }

            return temp_position
        }
        spinner.setSelection(mbti_default_position(share_user_type))
        spinner.onItemSelectedListener = this
        // ========================================================================
    }

    // 비트맵 -> JPG 변환
    fun saveBitmapToJpg(bitmap: Bitmap, name: String): String {
        val storage: File = File(app_file_path!!) //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        val fileName = "$name.jpg"
        val imgFile = File(storage, fileName)
        try {
            imgFile.createNewFile()
            val out = FileOutputStream(imgFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out) //썸네일로 사용하므로 퀄리티를 낮게설정
            out.close()
        } catch (e: FileNotFoundException) {
            Log.e("saveBitmapToJpg", "FileNotFoundException : " + e.message)
        } catch (e: IOException) {
            Log.e("saveBitmapToJpg", "IOException : " + e.message)
        }

        profile_image_change_count = 1
        return getCacheDir().toString() + "/" + fileName
    }

    // 갤러리에서 프로필 이미지 선택 시, 이벤트 제어
    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체를 추출한다.
            val uri = data?.data

            if(uri != null){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    // 안드로이드 10버전 부터
                    val source = ImageDecoder.createSource(contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    val temp_now_datetime = LocalDateTime.now().toString()
                    saveBitmapToJpg(bitmap, temp_now_datetime)
                    temp_profile_file = File("$app_file_path/$temp_now_datetime.jpg")
                    temp_profile_file_name = "$temp_now_datetime.jpg"
                    mypage_profile_image.setImageBitmap(bitmap)
                } else {
                    // 안드로이드 9버전 까지
                    val cursor = contentResolver.query(uri, null, null, null, null)
                    if(cursor != null){
                        cursor.moveToNext()
                        // 이미지 경로를 가져온다.
                        val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                        val source = cursor.getString(index)
                        // 이미지를 생성한다.
                        val bitmap = BitmapFactory.decodeFile(source)
                        mypage_profile_image.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        var zzz_hash:HashMap<String, String> = HashMap()
        zzz_hash["access_token"] = share_access_token
        zzz_hash["username"] = share_username
        zzz_hash["nickname"] = share_nickname
        zzz_hash["password"] = share_password
        zzz_hash["profile"] = share_profile
        zzz_hash["user_type"] = share_user_type
        zzz_hash["message"] = share_message
        zzz_hash["move_status"] = "4"
        MoveActivity().chain_move(this, zzz_hash)
    }

    // ========================== 스피너 제어 ==========================
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
    // ==============================================================

    override fun onPause() {
        super.onPause()
//        Log.d("TEST", "MypageProfileUpdateActivity - onPause")
    }

    override fun onResume() {
        super.onResume()
//        Log.d("TEST", "MypageProfileUpdateActivity - onResume")
    }

    override fun onStop() {
        super.onStop()
//        Log.d("TEST", "MypageProfileUpdateActivity - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.d("TEST", "MypageProfileUpdateActivity - onDestroy")
    }
}