package com.kwon.mbti_community.mypage.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.kwon.mbti_community.R
import com.kwon.mbti_community.mypage.model.GetUserData
import com.kwon.mbti_community.mypage.model.MypageInterface
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import kotlinx.android.synthetic.main.activity_mypage_other_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageOtherProfileActivity : AppCompatActivity() {

    // 값 전달 변수
    var share_access_token = ""
    var share_username = ""
    var share_nickname = ""
    var share_password = ""
    var share_profile = ""
    var share_user_type = ""
    var share_message = ""

    var share_other_username = ""
    var share_other_nickname = ""
    var share_other_user_type = ""
    var share_other_message = ""
    var share_other_profile = ""

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
        setContentView(R.layout.activity_mypage_other_profile)

        // ADS 설정
        var mAdView : AdView
        // 1. ADS 초기화
        MobileAds.initialize(
            this
        ) { }
        // 2. 광고 띄우기
        mAdView = findViewById(R.id.mypage_other_profile_adv)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // 프로그레스바 설정
        val mypage_other_profile_progress_layout = findViewById<LinearLayout>(R.id.mypage_other_profile_progress_layout)
        mypage_other_profile_progress_layout.bringToFront()

        // 값 가져오기
        share_access_token = intent.getStringExtra("access_token").toString()
        share_username = intent.getStringExtra("username").toString()
        share_other_username = intent.getStringExtra("other_username").toString()
        share_other_profile = intent.getStringExtra("other_profile").toString()

        // API 셋팅
        val access_token = share_access_token
        val conn = Connect().connect(access_token)
        val mypage_api: MypageInterface = conn.create(MypageInterface::class.java)

        // API 통신 : 다른 유저 정보 가져오기
        mypage_other_profile_progress_layout.visibility = View.VISIBLE
        mypage_api.getUserData(share_other_username).enqueue(object: Callback<GetUserData> {
            override fun onResponse(call: Call<GetUserData>, response: Response<GetUserData>) {
                val body = response.body()
                if(body != null) {
                    mypage_other_profile_nickname.text = body.data.user_info[0].nickname
                    mypage_other_profile_user_type.text = body.data.user_info[0].user_type
                    mypage_other_profile_message.text = body.data.user_info[0].message
                }
                mypage_other_profile_progress_layout.visibility = View.GONE
//                Log.d("TEST", "getUserData 통신성공 바디 -> $body")
            }

            override fun onFailure(call: Call<GetUserData>, t: Throwable) {
                mypage_other_profile_progress_layout.visibility = View.GONE
//                Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
            }
        })

        // API 통신 : 내 유저 정보 가져오기
        mypage_api.getUserData(share_username).enqueue(object: Callback<GetUserData> {
            override fun onResponse(call: Call<GetUserData>, response: Response<GetUserData>) {
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
                }
//                Log.d("TEST", "getUserData 통신성공 바디 -> $bodyss")
            }

            override fun onFailure(call: Call<GetUserData>, t: Throwable) {
//                Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
            }
        })

        // Glide로 이미지 표시하기
        val user_profile = findViewById<ImageView>(R.id.mypage_other_profile_image)
        Glide.with(this)
            .load(share_other_profile)
            .placeholder(R.drawable.user_default_profile)
            .error(R.drawable.user_default_profile)
            .into(user_profile)

        user_profile.setBackgroundResource(R.drawable.other_image_background_border)
        user_profile.clipToOutline = true

        mypage_other_profile_close_btn.setOnClickListener {
            var kkk_hash:HashMap<String, String> = HashMap()
            kkk_hash["access_token"] = share_access_token
            kkk_hash["username"] = share_username
            kkk_hash["nickname"] = share_nickname
            kkk_hash["password"] = share_password
            kkk_hash["profile"] = share_profile
            kkk_hash["user_type"] = share_user_type
            kkk_hash["message"] = share_message
            kkk_hash["move_status"] = "1"
            MoveActivity().chain_move(this, kkk_hash)
        }
    }

    override fun onBackPressed() {
        var uuu_hash:HashMap<String, String> = HashMap()
        uuu_hash["access_token"] = share_access_token
        uuu_hash["username"] = share_username
        uuu_hash["nickname"] = share_nickname
        uuu_hash["password"] = share_password
        uuu_hash["profile"] = share_profile
        uuu_hash["user_type"] = share_user_type
        uuu_hash["message"] = share_message
        uuu_hash["move_status"] = "1"
        MoveActivity().chain_move(this, uuu_hash)
    }
}