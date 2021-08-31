package com.kwon.mbti_community.mypage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.adapter.CommentAdapter
import com.kwon.mbti_community.board.adapter.CommentItem
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.CreateCommentData
import com.kwon.mbti_community.board.model.DeleteBoardData
import com.kwon.mbti_community.board.model.GetCommentData
import com.kwon.mbti_community.mypage.model.MypageInterface
import com.kwon.mbti_community.z_common.connect.Connect
import kotlinx.android.synthetic.main.fragment_board_item.view.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import kotlinx.android.synthetic.main.fragment_mypage_history_item.view.*
import kotlinx.android.synthetic.main.fragment_qna_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime


class MypageHistoryAdapter constructor(var context: Context, var items:ArrayList<MypageHistoryItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 토큰 확인
    val app_file_path = context.getExternalFilesDir(null).toString()
    val token_file = File("$app_file_path/token.token")
    val access_token = token_file.readText().split("\n")[0]
    val username = token_file.readText().split("\n")[1]
    val conn = Connect().connect(access_token)
    val mypage_api: MypageInterface = conn.create(MypageInterface::class.java)
    val board_api: BoardInterface = conn.create(BoardInterface::class.java)

    lateinit var recyclerView: RecyclerView
//    var board_items = arrayListOf<BoardItem>()
    var comment_items = arrayListOf<CommentItem>()

    // 페이지 넘버
    var page = "1"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.fragment_mypage_history_item, parent, false)

        return VH(itemView)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh: VH =holder as VH

        val item= items[position]

        vh.itemView.mypage_history_board_title.text = item.board_title
        vh.itemView.mypage_history_board_like_count.text = item.board_like_count.toString()

        vh.itemView.mypage_history_user_nickname.text = item.board_nickname
        vh.itemView.mypage_history_user_title.text = item.board_title
        vh.itemView.mypage_history_user_content.text = item.board_content
        vh.itemView.mypage_history_like_count.text = item.board_like_count.toString()

        Glide.with(context)
            .load(item.board_profile)
            .placeholder(R.drawable.user_default_profile)
            .error(R.drawable.user_default_profile)
            .into(vh.itemView.mypage_history_user_profile)

        vh.itemView.mypage_history_user_profile.setBackgroundResource(R.drawable.image_background_border)
        vh.itemView.mypage_history_user_profile.clipToOutline = true

        val temp_now_datetime = LocalDateTime.now()
        val now_date: LocalDate = LocalDate.now()
        val temp_updated_at = item.updated_at
        val now_hour = temp_now_datetime.toString().split("T")[1].split(":")[0].toInt()
        val now_min = temp_now_datetime.toString().split("T")[1].split(":")[1].toInt()

        if (temp_updated_at != null) {
            val temp_updated_date = temp_updated_at.split("T")[0]
            val temp_updated_hour = temp_updated_at.split("T")[1].split(":")[0].toInt()
            val temp_updated_min = temp_updated_at.split("T")[1].split(":")[1].toInt()

            if(temp_updated_at.split("T")[0] == now_date.toString()) {
                Log.d("TEST", "날짜 같음!!! -> $now_min --> $temp_updated_min")
                if(temp_updated_hour == now_hour) {
                    if((now_min - temp_updated_min) < 3) {
                        vh.itemView.mypage_history_datetime.text = "방금"
                    } else {
                        vh.itemView.mypage_history_datetime.text = "${(now_min - temp_updated_min)} 분 전"
                    }
                } else {
                    vh.itemView.mypage_history_datetime.text = (now_hour - temp_updated_hour).toString() + " 시간 전"
                }
            }else {
                Log.d("TEST", "날짜 다름!!!!! : ${item.updated_at}")
                vh.itemView.mypage_history_datetime.text = "$temp_updated_date ${temp_updated_hour}시 ${temp_updated_min}분"
            }
        }

        vh.itemView.mypage_history_board_more_close_btn.setOnClickListener {
            vh.itemView.mypage_history_board_more_close_btn.visibility = View.GONE
            vh.itemView.mypage_history_board_more_btn.visibility = View.VISIBLE
            vh.itemView.mypage_history_board_default_layout.visibility = View.VISIBLE

            vh.itemView.mypage_history_more_click_layout.visibility = View.GONE
        }

        vh.itemView.mypage_history_board_more_btn.setOnClickListener {
            vh.itemView.mypage_history_board_more_close_btn.visibility = View.VISIBLE
            vh.itemView.mypage_history_board_more_btn.visibility = View.GONE
            vh.itemView.mypage_history_board_default_layout.visibility = View.GONE

            vh.itemView.mypage_history_more_click_layout.visibility = View.VISIBLE
        }

        // 구분선
        vh.itemView.mypage_history_comment_more_close_btn.setOnClickListener {
            vh.itemView.mypage_history_comment_recycler.visibility = View.GONE
            vh.itemView.mypage_history_comment_more_btn.visibility = View.VISIBLE
            vh.itemView.mypage_history_comment_more_close_btn.visibility = View.GONE
            vh.itemView.mypage_history_comment_input_layout.visibility = View.GONE
        }

        vh.itemView.mypage_history_comment_more_btn.setOnClickListener {
            vh.itemView.mypage_history_comment_recycler.visibility = View.VISIBLE
            vh.itemView.mypage_history_comment_more_btn.visibility = View.GONE
            vh.itemView.mypage_history_comment_more_close_btn.visibility = View.VISIBLE
            vh.itemView.mypage_history_comment_input_layout.visibility = View.VISIBLE
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(vh.itemView.windowToken, 0)

            board_api.getComment(item.id, page).enqueue(object: Callback<GetCommentData> {
                override fun onResponse(call: Call<GetCommentData>, response: Response<GetCommentData>) {
                    val body = response.body()
                    if(body != null) {
                        comment_items.clear()
                        if(body.data.isNotEmpty()) {
                            for(nn in body.data) {
                                Log.d("TEST", "getComment 데이터 확인 : $nn")
                                var comment_my_item_count:Int
                                if(nn.comment_username == username) { comment_my_item_count = 1 } else { comment_my_item_count = 0 }
                                comment_items.add(
                                    CommentItem(nn.id, nn.comment_content, nn.comment_like_count.toString(), nn.comment_nickname, nn.comment_profile, nn.comment_title, nn.comment_user_type, nn.comment_username, nn.updated_at, comment_my_item_count)
                                )
                            }
                        } else {
                            comment_items.add(
                                CommentItem(null, null, null, null, null, null, null, null, null, null)
                            )
                        }

                        recyclerView = vh.itemView.findViewById(R.id.mypage_history_comment_recycler) as RecyclerView
                        recyclerView.layoutManager = LinearLayoutManager(context)
//                        val reverse_manager = LinearLayoutManager(context)
//                        reverse_manager.reverseLayout = true
//                        reverse_manager.stackFromEnd = true
//                        recyclerView.layoutManager = reverse_manager
                        recyclerView.adapter = CommentAdapter(context, comment_items)
                    }
                    Log.d("TEST", "getComment 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<GetCommentData>, t: Throwable) {
                    Log.d("TEST", "getComment 통신실패 에러 -> " + t.message)
                }
            })
        }

        vh.itemView.mypage_history_comment_submit_btn.setOnClickListener {
            val temp_comment_text = vh.itemView.mypage_history_comment_input.text
            if(temp_comment_text.toString() != "") {
                vh.itemView.mypage_history_comment_input.clearFocus()
                vh.itemView.mypage_history_comment_input.setText("")
                val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                mInputMethodManager.hideSoftInputFromWindow(vh.itemView.windowToken, 0)

                val paramter:HashMap<String, String> = HashMap()
                paramter["comment_user_type"] = item.board_user_type.toString()
                paramter["comment_content"] = temp_comment_text.toString()
                paramter["board_id"] = item.id.toString()

                board_api.createComment(paramter).enqueue(object: Callback<CreateCommentData> {
                    override fun onResponse(call: Call<CreateCommentData>, response: Response<CreateCommentData>) {
                        val body = response.body()
                        if(body != null) {
                            Log.d("TEST", "createComment 데이터확인 : ${body.data}")
                            var comment_my_item_count:Int
                            if(body.data.comment_username == username) { comment_my_item_count = 1 } else { comment_my_item_count = 0 }
                            comment_items.add(
                                CommentItem(body.data.id, body.data.comment_content, body.data.comment_like_count.toString(), body.data.comment_nickname, body.data.comment_profile, body.data.comment_title, body.data.comment_user_type, body.data.comment_username, body.data.updated_at, comment_my_item_count)
                            )
                            recyclerView=vh.itemView.findViewById(R.id.mypage_history_comment_recycler) as RecyclerView
                            recyclerView.layoutManager = LinearLayoutManager(context)
//                            val reverse_manager = LinearLayoutManager(context)
//                            reverse_manager.reverseLayout = true
//                            reverse_manager.stackFromEnd = true
//                            recyclerView.layoutManager = reverse_manager
                            recyclerView.adapter = CommentAdapter(context, comment_items)
                        }

                        Log.d("TEST", "createComment 통신성공 바디 -> $body")
                    }

                    override fun onFailure(call: Call<CreateCommentData>, t: Throwable) {
                        Log.d("TEST", "createComment 통신실패 에러 -> " + t.message)
                    }
                })
            }
        }

        // 게시글 추가메뉴 클릭이벤트
        vh.itemView.mypage_history_user_more.setOnClickListener {
            val popup = PopupMenu(context, vh.itemView.mypage_history_user_more)

            val inf: MenuInflater = popup.menuInflater
            inf.inflate(R.menu.board_menu, popup.menu)

            popup.setOnMenuItemClickListener { menu_item ->
                when (menu_item.itemId) {
                    R.id.board_update_menu -> {
                        Log.d("TEST", "메뉴 - 수정버튼 클릭")
                        true
                    }
                    R.id.board_delete_menu -> {
                        Log.d("TEST", "메뉴 - 삭제버튼 클릭")
                        val del_parm:HashMap<String, Int> = HashMap()
                        del_parm["board_id"] = item.id
                        board_api.deleteBoard(del_parm).enqueue(object: Callback<DeleteBoardData> {
                            override fun onResponse(call: Call<DeleteBoardData>,response: Response<DeleteBoardData>) {
                                val body = response.body()
                                if(body != null) {
                                    items.removeAt(position)
                                    notifyDataSetChanged()
                                }
                                Log.d("TEST", "deleteBoard 통신성공 바디 -> $body")
                            }

                            override fun onFailure(call: Call<DeleteBoardData>, t: Throwable) {
                                Log.d("TEST", "deleteBoard 통신실패 에러 -> " + t.message)
                            }
                        })
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        init{
        }
    }
}