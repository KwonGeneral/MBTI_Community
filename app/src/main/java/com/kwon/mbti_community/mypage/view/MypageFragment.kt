package com.kwon.mbti_community.mypage.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kwon.mbti_community.R
import com.kwon.mbti_community.chain.view.ChainActivity
import com.kwon.mbti_community.mypage.adapter.MypageHistoryAdapter
import com.kwon.mbti_community.mypage.adapter.MypageHistoryItem
import com.kwon.mbti_community.mypage.model.*
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MypageFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var recyclerView: RecyclerView
    var items = arrayListOf<MypageHistoryItem>()
    var items_no_list = arrayListOf<String>()

    // 값 전달 변수
    var share_access_token = ""
    var share_username = ""
    var share_nickname = ""
    var share_password = ""
    var share_profile = ""
    var share_user_type = ""
    var share_message = ""

    var app_file_path: String? = null

    companion object{
        fun newInstance() : MypageFragment {
            return MypageFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","MypageFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","MypageFragment - onAttach")
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","MypageFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_mypage, container, false)

        // 값 전달
        val bundle = Bundle()
        val bundle_arguments = arguments
        share_access_token = bundle_arguments?.getString("access_token").toString()
        share_username = bundle_arguments?.getString("username").toString()
        share_nickname = bundle_arguments?.getString("nickname").toString()
        share_password = bundle_arguments?.getString("password").toString()
        share_profile = bundle_arguments?.getString("profile").toString()
        share_user_type = bundle_arguments?.getString("user_type").toString()
        share_message = bundle_arguments?.getString("share_message").toString()

        Log.d("TEST", "share_access_token : $share_access_token")
        Log.d("TEST", "share_username : $share_username")
        Log.d("TEST", "share_nickname : $share_nickname")
        Log.d("TEST", "share_password : $share_password")
        Log.d("TEST", "share_profile : $share_profile")
        Log.d("TEST", "share_user_type : $share_user_type")
        Log.d("TEST", "share_message : $share_message")

        // Glide로 이미지 표시하기
        val user_profile = view.findViewById<ImageView>(R.id.user_profile)
        Glide.with(requireContext())
            .load(share_profile)
            .placeholder(R.drawable.user_default_profile)
            .error(R.drawable.user_default_profile)
            .into(user_profile)

        user_profile.setBackgroundResource(R.drawable.image_background_border)
        user_profile.clipToOutline = true

//        user_profile.outlineProvider = object : ViewOutlineProvider() {
//            override fun getOutline(view: View, outline: Outline) {
//                outline.setRoundRect(0, 0, view.width, view.height, 20f)
//            }
//        }

        // 설정해줘야 하는 값
        view.findViewById<TextView>(R.id.mypage_user_nickname).text = share_nickname
        view.findViewById<TextView>(R.id.mypage_user_message).text = share_message
        view.findViewById<TextView>(R.id.mypage_user_mbti).text = share_user_type

        // API 셋팅
        val access_token = share_access_token
        val conn = Connect().connect(access_token)
        val mypage_api: MypageInterface = conn.create(MypageInterface::class.java)

        // 유저 프로필 클릭 이벤트
        user_profile.setOnClickListener {
//            // 앨범에서 사진을 선택할 수 있는 액티비티를 실행한다.
//            val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것)
//            albumIntent.type = "image/*"
//            // 선택할 파일의 타입을 지정(안드로이드 OS가 사전작업을 할 수 있도록)
//            val mimeType = arrayOf("image/*")
//            albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
//            startActivityForResult(albumIntent, 0)
        }

        // 프로필 수정 버튼 클릭
        view.findViewById<TextView>(R.id.profile_update_btn).setOnClickListener {
            val hash: HashMap<String, String> = HashMap()
            hash["access_token"] = share_access_token
            hash["username"] = share_username
            hash["nickname"] = share_nickname
            hash["password"] = share_password
            hash["profile"] = share_profile
            hash["user_type"] = share_user_type
            hash["message"] = share_message

            MoveActivity().profile_update_move(context as ChainActivity, hash)
            ChainActivity().finish()
        }

        // 유저 프로필 이미지 변환 API
//        app_file_path = requireContext().getExternalFilesDir(null).toString()
//        val file = File("$app_file_path/car_number.jpeg")
//        val filename = "car_number.jpeg"
//        Log.d("TEST", " File : $file")
//        Log.d("TEST", " File Name : $filename")
//
//        var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
//        var part : MultipartBody.Part = MultipartBody.Part.createFormData("profile", filename, requestBody)
//
//        Log.d("TEST", " requestBody : $requestBody")
//        Log.d("TEST", " part : $part")
//        mypage_api.updateUserInfo(share_username, part).enqueue(object: Callback<UpdateUserProfileData> {
//            override fun onResponse(call: Call<UpdateUserProfileData>, response: Response<UpdateUserProfileData>) {
//                val body = response.body()
//                Log.d("TEST", "updateUserInfo 통신성공 바디 -> $body")
//            }
//
//            override fun onFailure(call: Call<UpdateUserProfileData>, t: Throwable) {
//                Log.d("TEST", "updateUserInfo 통신실패 에러 -> " + t.message)
//            }
//        })

        // API 통신 : 글 카운트 가져오기
        mypage_api.getBoardCount().enqueue(object: Callback<GetBoardCountData> {
            override fun onResponse(call: Call<GetBoardCountData>, response: Response<GetBoardCountData>) {
                val bodys = response.body()
                if(bodys != null) {
                    view.findViewById<TextView>(R.id.mypage_user_board_count).text = bodys.data.board_total_count
                }
                Log.d("TEST", "getBoardCount 통신성공 바디 -> $bodys")

                // API 통신 : 유저가 올린 게시글 가져오기
                mypage_api.getUserBoard(share_username).enqueue(object: Callback<GetUserBoardData> {
                    override fun onResponse(call: Call<GetUserBoardData>, response: Response<GetUserBoardData>) {
                        val body = response.body()
                        if(body != null) {
                            if(body.data.isNotEmpty()) {
                                for(nn in body.data) {
                                    Log.d("TEST", "하하하 : $nn")
                                    items.add(
                                        MypageHistoryItem(nn.id, nn.board_content, nn.board_like_count.toString(), nn.board_nickname, nn.board_profile, nn.board_title, nn.board_type, nn.board_user_type, nn.board_username, nn.updated_at)
                                    )
                                }

                                recyclerView=view.findViewById(R.id.mypage_history_recycler) as RecyclerView
                                val reverse_manager = LinearLayoutManager(requireContext())
                                reverse_manager.reverseLayout = true
                                reverse_manager.stackFromEnd = true
//
                                recyclerView.layoutManager = reverse_manager
                                recyclerView.adapter = MypageHistoryAdapter(requireContext(), items)
                            }
                        }
                        Log.d("TEST", "getUserBoard 통신성공 바디 -> $body")
                    }

                    override fun onFailure(call: Call<GetUserBoardData>, t: Throwable) {
                        Log.d("TEST", "getUserBoard 통신실패 에러 -> " + t.message)
                    }
                })
            }

            override fun onFailure(call: Call<GetBoardCountData>, t: Throwable) {
                Log.d("TEST", "getBoardCount 통신실패 에러 -> " + t.message)
            }
        })

        /*
        val select_feel_very_good = view.findViewById<TextView>(R.id.select_feel_very_good)
        val select_feel_good = view.findViewById<TextView>(R.id.select_feel_good)
        val select_feel_so = view.findViewById<TextView>(R.id.select_feel_so)
        val select_feel_bad = view.findViewById<TextView>(R.id.select_feel_bad)
        val select_feel_very_bad = view.findViewById<TextView>(R.id.select_feel_very_bad)

        select_feel_very_good.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_good.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_so.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_bad.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        select_feel_very_bad.setOnClickListener {
            select_feel_very_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_good.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_so.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            select_feel_very_bad.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
        }
         */

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "MypageFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "MypageFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "MypageFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "MypageFragment - onDestroy")
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.board_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.board_update_menu -> Log.d("TEST", "수정하기 버튼 클릭")
//            R.id.board_delete_menu -> Log.d("TEST", "삭제하기 버튼 클릭")
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
}