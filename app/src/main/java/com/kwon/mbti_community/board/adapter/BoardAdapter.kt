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
import com.kwon.mbti_community.board.model.*
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
                        recyclerView.layoutManager = reverse_manager
                        recyclerView.adapter = CommentAdapter(context, comment_items)

                    }
                    Log.d("TEST", "getComment 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<GetCommentData>, t: Throwable) {
                    Log.d("TEST", "getComment 통신실패 에러 -> " + t.message)
                }
            })
        }

        vh.itemView.comment_submit_btn.setOnClickListener {
            val temp_comment_text = vh.itemView.comment_input.text
            if(temp_comment_text.toString() != "") {
                vh.itemView.comment_input.clearFocus()
                vh.itemView.comment_input.setText("")
                // 키보드 내리기
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
//                            comment_items.clear()
                            Log.d("TEST", "createComment 하하하 : $body.data")
                            comment_items.add(
                                CommentItem(body.data.id, body.data.comment_content, body.data.comment_like_count.toString(), body.data.comment_nickname, body.data.comment_profile, body.data.comment_title, body.data.comment_user_type, body.data.comment_username, body.data.updated_at)
                            )

                            recyclerView=vh.itemView.findViewById(R.id.comment_recycler) as RecyclerView
                            val reverse_manager = LinearLayoutManager(context)
                            reverse_manager.reverseLayout = true
                            reverse_manager.stackFromEnd = true
                            recyclerView.layoutManager = reverse_manager
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
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        init{
        }
    }
}