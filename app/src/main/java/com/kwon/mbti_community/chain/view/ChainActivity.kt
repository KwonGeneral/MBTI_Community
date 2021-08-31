package com.kwon.mbti_community.chain.view

import android.Manifest
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.snackbar.Snackbar
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.view.BoardFragment
import com.kwon.mbti_community.mypage.adapter.MypageHistoryItem
import com.kwon.mbti_community.qna.view.QnaFragment
import com.kwon.mbti_community.write.view.WriteFragment
import com.kwon.mbti_community.mypage.view.MypageFragment
import kotlinx.android.synthetic.main.activity_chain.*
import kotlinx.coroutines.delay

class ChainActivity : AppCompatActivity() {
//    private lateinit var home_fragment: HomeFragment
    private lateinit var board_fragment: BoardFragment
    private lateinit var qna_fragment: QnaFragment
    private lateinit var write_fragment: WriteFragment
    private lateinit var mypage_fragment: MypageFragment
    private lateinit var bnv_menu: BottomNavigationMenuView

    var share_access_token = ""
    var share_username = ""
    var share_nickname = ""
    var share_password = ""
    var share_profile = ""
    var share_user_type = ""
    var share_message = ""
    var check_move_status = ""

    // 뒤로가기 연속 클릭 대기 시간
    var mBackWait:Long = 0

    var app_file_path: String? = null
    val bundle = Bundle()

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
        setContentView(R.layout.activity_chain)
        requestPermissions(permission_list, 0)
//        Log.d("TEST", "ChainActivity - onCreate")

        // ADS 설정
        var mAdView : AdView
        // 1. ADS 초기화
        MobileAds.initialize(
            this
        ) { }
        // 2. 광고 띄우기
        mAdView = findViewById(R.id.chain_adv)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // 로그인에서 값 가져오기
        share_access_token = intent.getStringExtra("access_token").toString()
        share_username = intent.getStringExtra("username").toString()
        share_nickname = intent.getStringExtra("nickname").toString()
        share_password = intent.getStringExtra("password").toString()
        share_profile = intent.getStringExtra("profile").toString()
        share_user_type = intent.getStringExtra("user_type").toString()
        share_message = intent.getStringExtra("message").toString()
        check_move_status = intent.getStringExtra("move_status").toString()

        share_profile = share_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

        // 화면 전환 제어
        if(check_move_status == "1") {
            changeFragment(1)
        } else if (check_move_status == "4") {
            changeFragment(4)
        } else {
            changeFragment(1)
        }


//        nav_home_layout.setOnClickListener {
//            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
//            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
//            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
//            nav_qna.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
//            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
//            changeFragment(0)
//        }
        nav_board_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_qna.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            changeFragment(1)
            requestPermissions(permission_list, 0)
//            nav_board_layout.setBackgroundResource(R.drawable.click_effect)
        }
        nav_write_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            nav_qna.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            changeFragment(2)
            requestPermissions(permission_list, 0)
//            nav_hobby_layout.setBackgroundResource(R.drawable.click_effect)
        }
        nav_qna_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_qna.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            changeFragment(3)
            requestPermissions(permission_list, 0)
//            nav_job_layout.setBackgroundResource(R.drawable.click_effect)
        }
        nav_mypage_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_qna.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            changeFragment(4)
            requestPermissions(permission_list, 0)
//            nav_mypage_layout.setBackgroundResource(R.drawable.click_effect)
        }
    }

    fun changeFragment(int: Int) {
        bundle.putString("access_token", share_access_token)
        bundle.putString("username", share_username)
        bundle.putString("nickname", share_nickname)
        bundle.putString("password", share_password)
        bundle.putString("profile", share_profile)
        bundle.putString("user_type", share_user_type)
        bundle.putString("message", share_message)

        if (int == 0) {
//            Log.d("TEST", "홈 프레그먼트 - 삭제됨")
//            home_fragment = HomeFragment.newInstance()
//            home_fragment.arguments = bundle
//            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, home_fragment).commit()
        } else if (int == 1) {
//            Log.d("TEST", "게시판 프레그먼트")
            board_fragment = BoardFragment.newInstance()
            board_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, board_fragment).commit()
        } else if (int == 2) {
//            Log.d("TEST", "글쓰기 프레그먼트")
            write_fragment = WriteFragment.newInstance()
            write_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, write_fragment).commit()
        } else if (int == 3) {
//            Log.d("TEST", "취미 프레그먼트")
            qna_fragment = QnaFragment.newInstance()
            qna_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, qna_fragment).commit()
        } else if (int == 4) {
//            Log.d("TEST", "마이페이지 프레그먼트")
            mypage_fragment = MypageFragment.newInstance()
            mypage_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, mypage_fragment).commit()
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        Log.d("TEST", "ChainActivity - onBackPressed")

        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >= 1000 ) {
            mBackWait = System.currentTimeMillis()
            val snack: Snackbar = Snackbar
                .make(findViewById<RelativeLayout>(R.id.chain_all_layout), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", 1000)
                .setBackgroundTint(Color.parseColor("#ffffff"))
                .setTextColor(Color.parseColor("#ba000000"))

            val snack_view = snack.view
            val params = snack_view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            snack_view.layoutParams = params
            snack.show()
        } else {
            finish() //액티비티 종료
        }
    }

    override fun onPause() {
        super.onPause()
//        Log.d("TEST", "ChainActivity - onPause")
    }

    override fun onResume() {
        super.onResume()
//        Log.d("TEST", "ChainActivity - onResume")
    }

    override fun onStop() {
        super.onStop()
//        Log.d("TEST", "ChainActivity - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.d("TEST", "ChainActivity - onDestroy")
    }
}