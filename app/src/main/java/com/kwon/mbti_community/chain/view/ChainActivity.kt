package com.kwon.mbti_community.chain.view

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.snackbar.Snackbar
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.view.BoardFragment
import com.kwon.mbti_community.hobby.view.HobbyFragment
import com.kwon.mbti_community.home.view.HomeFragment
import com.kwon.mbti_community.write.view.WriteFragment
import com.kwon.mbti_community.mypage.view.MypageFragment
import kotlinx.android.synthetic.main.activity_chain.*

class ChainActivity : AppCompatActivity() {
    private lateinit var home_fragment: HomeFragment
    private lateinit var board_fragment: BoardFragment
    private lateinit var hobby_fragment: HobbyFragment
    private lateinit var write_fragment: WriteFragment
    private lateinit var mypage_fragment: MypageFragment
    private lateinit var bnv_menu: BottomNavigationMenuView

    //뒤로가기 연속 클릭 대기 시간
    var mBackWait:Long = 0

    var app_file_path: String? = null
    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chain)
        Log.d("TEST", "ChainActivity - onCreate")

        changeFragment(0)

        nav_home_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            changeFragment(0)
//            nav_home_layout.setBackgroundResource(R.drawable.click_effect)
        }
        nav_board_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            changeFragment(1)
//            nav_board_layout.setBackgroundResource(R.drawable.click_effect)
        }
        nav_write_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            nav_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            changeFragment(2)
//            nav_hobby_layout.setBackgroundResource(R.drawable.click_effect)
        }
        nav_hobby_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            changeFragment(3)
//            nav_job_layout.setBackgroundResource(R.drawable.click_effect)
        }
        nav_mypage_layout.setOnClickListener {
            nav_home.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_board.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_write.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dfffffff"))
            nav_mypage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3C1969"))
            changeFragment(4)
//            nav_mypage_layout.setBackgroundResource(R.drawable.click_effect)
        }
    }

    fun changeFragment(int: Int) {
        if (int == 0) {
            Log.d("TEST", "홈 프레그먼트")
            home_fragment = HomeFragment.newInstance()
            home_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, home_fragment).commit()
        } else if (int == 1) {
            Log.d("TEST", "게시판 프레그먼트")
            board_fragment = BoardFragment.newInstance()
            board_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, board_fragment).commit()
        } else if (int == 2) {
            Log.d("TEST", "글쓰기 프레그먼트")
            write_fragment = WriteFragment.newInstance()
            write_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, write_fragment).commit()
        } else if (int == 3) {
            Log.d("TEST", "취미 프레그먼트")
            hobby_fragment = HobbyFragment.newInstance()
            hobby_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, hobby_fragment).commit()
        } else if (int == 4) {
            Log.d("TEST", "마이페이지 프레그먼트")
            mypage_fragment = MypageFragment.newInstance()
            mypage_fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.chain_frag, mypage_fragment).commit()
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        Log.d("TEST", "ChainActivity - onBackPressed")

        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >= 2000 ) {
            mBackWait = System.currentTimeMillis()
            Snackbar
                .make(findViewById<FrameLayout>(R.id.chain_frag), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.parseColor("#6B58FF"))
                .setTextColor(Color.parseColor("#aaffffff"))
                .show()
        } else {
            finish() //액티비티 종료
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "ChainActivity - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "ChainActivity - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "ChainActivity - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "ChainActivity - onDestroy")
    }
}