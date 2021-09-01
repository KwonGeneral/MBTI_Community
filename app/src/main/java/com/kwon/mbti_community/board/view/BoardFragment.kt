package com.kwon.mbti_community.board.view

import android.Manifest
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
import android.os.Build
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.board.adapter.BoardAdapter
import com.kwon.mbti_community.board.adapter.BoardItem
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.GetBoardData
import com.kwon.mbti_community.z_common.connect.Connect
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime

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

    // 페이지 넘버
    var page = "1"

    companion object {
        fun newInstance() : BoardFragment {
            return BoardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d("TEST","BoardFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        Log.d("TEST","BoardFragment - onAttach")
    }

    fun Int.dp(): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        Log.d("TEST","BoardFragment - onCreateView")
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
        share_message = bundle_arguments?.getString("message").toString()

        share_profile = share_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

        // 프로그레스바 설정
        val board_progress_layout = view.findViewById<LinearLayout>(R.id.board_progress_layout)
        board_progress_layout.bringToFront()

        // API 셋팅
        val access_token = share_access_token
        val conn = Connect().connect(access_token)
        val board_api: BoardInterface = conn.create(BoardInterface::class.java)

        board_api.getBoard("daily", page).enqueue(object: Callback<GetBoardData> {
            override fun onResponse(call: Call<GetBoardData>, response: Response<GetBoardData>) {
                val body = response.body()

                if(body != null){
                    for(nn in body.data) {
                        var my_item_count_check:Int
                        if(nn.board_username == share_username) { my_item_count_check = 1 } else { my_item_count_check = 0 }

                        var check_board_profile = ""
                        if(nn.board_profile != null && nn.board_profile != "" && nn.board_profile != "null") {
                            check_board_profile = nn.board_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")
                        }

                        items.add(
                            BoardItem(nn.id, nn.board_content, nn.board_like_count.toString(), nn.board_nickname, check_board_profile, nn.board_title, nn.board_type, nn.board_user_type, nn.board_username, nn.created_at, my_item_count_check)
                        )
                    }

                    recyclerView=view.findViewById(R.id.board_recycler) as RecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter= BoardAdapter(requireContext(), items)

                    board_progress_layout.visibility = View.GONE
                }

//                Log.d("TEST", "Board - getBoard 통신성공 바디 -> $body")
            }

            override fun onFailure(call: Call<GetBoardData>, t: Throwable) {
//                Log.d("TEST", "Board - getBoard 통신실패 에러 -> " + t.message)
            }
        })

        val board_loading_progress = view.findViewById<ProgressBar>(R.id.board_loading_progress)
        val board_recycler = view.findViewById(R.id.board_recycler) as RecyclerView
        board_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!board_recycler.canScrollVertically(1)) {
                    page = (page.toInt() + 1).toString()

                    board_loading_progress.visibility = View.VISIBLE
                    board_api.getBoard("daily", page).enqueue(object: Callback<GetBoardData> {
                        override fun onResponse(call: Call<GetBoardData>, response: Response<GetBoardData>) {
                            val body = response.body()
                            val temp_y = items.lastIndex - 2

                            if(body != null){
                                for(nn in body.data) {
                                    var my_item_count_check:Int
                                    if(nn.board_username == share_username) { my_item_count_check = 1 } else { my_item_count_check = 0 }

                                    var check_board_profile = ""
                                    if(nn.board_profile != null && nn.board_profile != "" && nn.board_profile != "null") {
                                        check_board_profile = nn.board_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")
                                    }

                                    items.add(
                                        BoardItem(nn.id, nn.board_content, nn.board_like_count.toString(), nn.board_nickname, check_board_profile, nn.board_title, nn.board_type, nn.board_user_type, nn.board_username, nn.created_at, my_item_count_check)
                                    )
                                }

                                Thread.sleep(200)
                                this@BoardFragment.recyclerView =view.findViewById(R.id.board_recycler) as RecyclerView
                                recyclerView.layoutManager = LinearLayoutManager(context)
                                recyclerView.adapter= BoardAdapter(requireContext(), items)
                                recyclerView.scrollToPosition(temp_y)
                            }

                            board_loading_progress.visibility = View.GONE
//                            Log.d("TEST", "Board - getBoard 통신성공 바디 -> $body")
                        }

                        override fun onFailure(call: Call<GetBoardData>, t: Throwable) {
//                            Log.d("TEST", "Board - getBoard 통신실패 에러 -> " + t.message)
                            board_loading_progress.visibility = View.GONE
                        }
                    })
                }
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
//        Log.d("TEST", "BoardFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
//        Log.d("TEST", "BoardFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
//        Log.d("TEST", "BoardFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.d("TEST", "BoardFragment - onDestroy")
    }
}