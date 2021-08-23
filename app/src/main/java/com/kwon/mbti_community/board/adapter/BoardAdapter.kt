package com.kwon.mbti_community.board.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.LikeBoardData
import com.kwon.mbti_community.board.model.GetBoardData
import com.kwon.mbti_community.board.model.GetCommentData
import com.kwon.mbti_community.board.view.BoardFragment
import com.kwon.mbti_community.chain.view.ChainActivity
import com.kwon.mbti_community.z_common.connect.Connect
import kotlinx.android.synthetic.main.fragment_board_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class BoardAdapter constructor(var context:Context, var items:ArrayList<BoardItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 토큰 확인
    val app_file_path = context.getExternalFilesDir(null).toString()
    val token_file = File("$app_file_path/token.token")
    val access_token = token_file.readText()
    val conn = Connect().connect(access_token)
    val board_api: BoardInterface = conn.create(BoardInterface::class.java)

    lateinit var recyclerView: RecyclerView
    var comment_items = arrayListOf<CommentItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.fragment_board_item, parent, false)

        return VH(itemView)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh: VH =holder as VH

        val item= items[position]

//        vh.itemView.board_user_profile = item.board_profile
        vh.itemView.board_user_nickname.text = item.board_nickname
        vh.itemView.board_user_title.text = item.board_title
        vh.itemView.board_user_content.text = item.board_content
        vh.itemView.board_like_count.text = item.board_like_count.toString()

        vh.itemView.board_like_btn.setOnClickListener {
            val parameter:HashMap<String, Int> = HashMap()
            parameter["board_id"] = item.id

            board_api.likeBoard(parameter).enqueue(object: Callback<LikeBoardData> {
                override fun onResponse(call: Call<LikeBoardData>, response: Response<LikeBoardData>) {
                    val body = response.body()
                    if(body != null) {
                        if(body.data.username != "kwontaewan") {
                            vh.itemView.board_like_count.text = (vh.itemView.board_like_count.text.toString().toInt() + 1).toString()
                        }else {
                            vh.itemView.board_like_count.text = (vh.itemView.board_like_count.text.toString().toInt() - 1).toString()
                        }
                    }

                    Log.d("TEST", "likeBoard 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<LikeBoardData>, t: Throwable) {
                    Log.d("TEST", "likeBoard 통신실패 에러 -> " + t.message)
                }
            })
        }

        vh.itemView.comment_more_close_btn.setOnClickListener {
            vh.itemView.comment_recycler.visibility = View.GONE
            vh.itemView.comment_more_btn.visibility = View.VISIBLE
            vh.itemView.comment_more_close_btn.visibility = View.GONE
            vh.itemView.comment_input_layout.visibility = View.GONE
        }

        vh.itemView.comment_more_btn.setOnClickListener {
            vh.itemView.comment_recycler.visibility = View.VISIBLE
            vh.itemView.comment_more_btn.visibility = View.GONE
            vh.itemView.comment_more_close_btn.visibility = View.VISIBLE
            vh.itemView.comment_input_layout.visibility = View.VISIBLE
            // comment_user_disabled_layout
            // 키보드 내리기
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(vh.itemView.windowToken, 0)

            board_api.getComment(item.id).enqueue(object: Callback<GetCommentData> {
                override fun onResponse(call: Call<GetCommentData>, response: Response<GetCommentData>) {
                    val body = response.body()
                    if(body != null) {
                        comment_items.clear()
                        if(body.data.isNotEmpty()) {
                            for(nn in body.data) {
                                Log.d("TEST", "하하하 : $nn")
                                comment_items.add(
                                    CommentItem(nn.id, nn.comment_content, nn.comment_like_count.toString(), nn.comment_nickname, nn.comment_profile, nn.comment_title, nn.comment_user_type, nn.comment_username, nn.updated_at)
                                )
                            }
                        } else {
                            comment_items.add(
                                CommentItem(null, null, null, null, null, null, null, null, null)
                            )
                        }


                        recyclerView=vh.itemView.findViewById(R.id.comment_recycler) as RecyclerView
                        val reverse_manager = LinearLayoutManager(context)
                        reverse_manager.reverseLayout = true
                        reverse_manager.stackFromEnd = true
//
                        recyclerView.layoutManager = reverse_manager
                        recyclerView.adapter = CommentAdapter(context, comment_items)
//                        if(body.data.isNotEmpty()) {
//                            for(nn in body.data) {
//                                Log.d("TEST", "하하하 : $nn")
//                                comment_items.add(
//                                    CommentItem(nn.id, nn.comment_content, nn.comment_like_count.toString(), nn.comment_nickname, nn.comment_profile, nn.comment_title, nn.comment_user_type, nn.comment_username, nn.updated_at)
//                                )
//                            }
//
//                            recyclerView=vh.itemView.findViewById(R.id.comment_recycler) as RecyclerView
//                            val reverse_manager = LinearLayoutManager(context)
//                            reverse_manager.reverseLayout = true
//                            reverse_manager.stackFromEnd = true
////
//                            recyclerView.layoutManager = reverse_manager
//                            recyclerView.adapter = CommentAdapter(context, comment_items)
//                        }

                    }

                    Log.d("TEST", "getComment 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<GetCommentData>, t: Throwable) {
                    Log.d("TEST", "getComment 통신실패 에러 -> " + t.message)
                }
            })
        }

        /*
        vh.itemView.title_text.text = item.notice_data["notice_title"]
        vh.itemView.content_text.text = item.notice_data["notice_content"]
        vh.itemView.date_text.text = item.notice_data["notice_date"]

        vh.itemView.notice_top_layout.setOnClickListener {
            Log.d("TEST", "제목 : ${item.notice_data["notice_title"]}")
            Log.d("TEST", "내용 : ${item.notice_data["notice_content"]}")
            Log.d("TEST", "날짜 : ${item.notice_data["notice_date"]}")
            Log.d("TEST", "알림음 위치 : $position")
            vh.itemView.notice_top_layout.setBackgroundColor(Color.parseColor("#14E73754"))
            vh.itemView.notice_bottom_layout.visibility = View.VISIBLE

            clear()
            for(kk in 0 until item_hash_map.size) {
                if(items_no_list.toList().sorted()[kk] == item.notice_no) {
                    item_add(item.notice_no, item.notice_data, 1)
                }else {
                    item_add((kk+1).toString(), item_hash_map[(kk+1).toString()]!!, 0)
                }
            }

        }

        if(items[position].select_status == 0) {
            vh.itemView.notice_top_layout.setBackgroundColor(Color.parseColor("#ffffff"))
            vh.itemView.notice_bottom_layout.visibility = View.GONE
        }else if(items[position].select_status == 1) {
            vh.itemView.notice_top_layout.setBackgroundColor(Color.parseColor("#14E73754"))
            vh.itemView.notice_bottom_layout.visibility = View.VISIBLE
        }
         */
    }

    override fun getItemCount(): Int {
        /*
        for(position in 0 until items.size) {
            val item= items[position]
            items_no_list.add(item.notice_no)
            item_hash_map[item.notice_no] = item.notice_data
        }
         */

        return items.size
    }

    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        init{
        }
    }
}