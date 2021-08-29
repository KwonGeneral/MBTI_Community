package com.kwon.mbti_community.board.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kwon.mbti_community.R
import android.widget.AdapterView
import android.util.Log
import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.board.adapter.BoardAdapter
import com.kwon.mbti_community.board.adapter.BoardItem
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.GetBoardData
import com.kwon.mbti_community.z_common.connect.Connect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var recyclerView: RecyclerView
    var items = arrayListOf<BoardItem>()
    var items_no_list = arrayListOf<String>()

    // 값 전달 변수
    var share_access_token = ""
    var share_username = ""
    var share_nickname = ""
    var share_password = ""
    var share_profile = ""
    var share_user_type = ""
    var share_message = ""

    companion object {
        fun newInstance() : BoardFragment {
            return BoardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","BoardFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","BoardFragment - onAttach")
    }

    fun Int.dp(): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","BoardFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_board, container, false)

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

        share_profile = share_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

        // 최상단 MBTI 표기 변경 -> 게시판도 변경해서 불러와야할 필요가 있음.
//        select_text.text = share_user_type

        // API 셋팅
        val access_token = share_access_token
        val conn = Connect().connect(access_token)
        val board_api: BoardInterface = conn.create(BoardInterface::class.java)

        board_api.getBoard("daily").enqueue(object: Callback<GetBoardData> {
            override fun onResponse(call: Call<GetBoardData>, response: Response<GetBoardData>) {
                val body = response.body()

                if(body != null){
                    for(nn in body.data) {
                        Log.d("TEST", "하하하 : $nn")
                        var my_item_count_check:Int
                        if(nn.board_username == share_username) { my_item_count_check = 1 } else { my_item_count_check = 0 }

                        var check_board_profile = ""
                        if(nn.board_profile != null && nn.board_profile != "" && nn.board_profile != "null") {
                            check_board_profile = nn.board_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")
                        }

                        items.add(
                            BoardItem(nn.id, nn.board_content, nn.board_like_count.toString(), nn.board_nickname, check_board_profile, nn.board_title, nn.board_type, nn.board_user_type, nn.board_username, nn.updated_at, my_item_count_check)
                        )
                    }

                    recyclerView=view.findViewById(R.id.board_recycler) as RecyclerView
                    val reverse_manager = LinearLayoutManager(requireContext())
                    reverse_manager.reverseLayout = true
                    reverse_manager.stackFromEnd = true
//
                    recyclerView.layoutManager = reverse_manager
                    recyclerView.adapter= BoardAdapter(requireContext(), items)
                }

                Log.d("TEST", "Board - getBoard 통신성공 바디 -> $body")
            }

            override fun onFailure(call: Call<GetBoardData>, t: Throwable) {
                Log.d("TEST", "Board - getBoard 통신실패 에러 -> " + t.message)
            }
        })

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "BoardFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "BoardFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "BoardFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "BoardFragment - onDestroy")
    }
}