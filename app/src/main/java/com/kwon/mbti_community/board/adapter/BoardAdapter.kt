package com.kwon.mbti_community.board.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.model.*
import com.kwon.mbti_community.chain.view.ChainActivity
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import kotlinx.android.synthetic.main.fragment_board_item.view.*
import kotlinx.android.synthetic.main.fragment_board_item.view.comment_more_icon
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

        vh.itemView.board_user_nickname.text = item.board_nickname
        vh.itemView.board_user_title.text = item.board_title
        vh.itemView.board_user_content.text = item.board_content
        vh.itemView.board_like_count.text = item.board_like_count.toString()

        Glide.with(context)
            .load(item.board_profile)
            .placeholder(R.drawable.user_default_profile)
            .error(R.drawable.user_default_profile)
            .into(vh.itemView.board_user_profile)

        vh.itemView.board_user_profile.setBackgroundResource(R.drawable.image_background_border)
        vh.itemView.board_user_profile.clipToOutline = true

        if(item.my_item_count == 1) {
            vh.itemView.board_user_more.visibility = View.VISIBLE
        } else {
            vh.itemView.board_user_more.visibility = View.GONE
        }

        vh.itemView.board_like_btn.setOnClickListener {
            val parameter:HashMap<String, Int> = HashMap()
            parameter["board_id"] = item.id

            board_api.likeBoard(parameter).enqueue(object: Callback<LikeBoardData> {
                override fun onResponse(call: Call<LikeBoardData>, response: Response<LikeBoardData>) {
                    val body = response.body()
                    if(body != null) {
                        if(body.code == "S0001") {
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
            vh.itemView.comment_more_icon.setBackgroundResource(R.drawable.arrow_down_icon)
            vh.itemView.comment_recycler.visibility = View.GONE
            vh.itemView.comment_more_btn.visibility = View.VISIBLE
            vh.itemView.comment_more_close_btn.visibility = View.GONE
            vh.itemView.comment_input_layout.visibility = View.GONE
        }

        vh.itemView.comment_more_btn.setOnClickListener {
            vh.itemView.comment_more_icon.setBackgroundResource(R.drawable.arrow_up_icon)
            vh.itemView.comment_recycler.visibility = View.VISIBLE
            vh.itemView.comment_more_btn.visibility = View.GONE
            vh.itemView.comment_more_close_btn.visibility = View.VISIBLE
            vh.itemView.comment_input_layout.visibility = View.VISIBLE
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(vh.itemView.windowToken, 0)

            board_api.getComment(item.id).enqueue(object: Callback<GetCommentData> {
                override fun onResponse(call: Call<GetCommentData>, response: Response<GetCommentData>) {
                    val body = response.body()
                    if(body != null) {
                        comment_items.clear()
                        if(body.data.isNotEmpty()) {
                            for(nn in body.data) {
                                Log.d("TEST", "getComment 데이터 확인 : $nn")
                                var comment_my_item_count:Int
                                if(nn.comment_username == item.board_username) { comment_my_item_count = 1 } else { comment_my_item_count = 0 }

                                var check_comment_profile = nn.comment_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

                                comment_items.add(
                                    CommentItem(nn.id, nn.comment_content, nn.comment_like_count.toString(), nn.comment_nickname, nn.comment_profile, nn.comment_title, nn.comment_user_type, nn.comment_username, nn.updated_at, comment_my_item_count)
                                )
                            }
                        } else {
                            comment_items.add(
                                CommentItem(null, null, null, null, null, null, null, null, null, null)
                            )
                        }

                        recyclerView = vh.itemView.findViewById(R.id.comment_recycler) as RecyclerView
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
                            if(body.data.comment_username == item.board_username) { comment_my_item_count = 1 } else { comment_my_item_count = 0 }

                            var check_comment_profile2 = body.data.comment_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

                            comment_items.add(
                                CommentItem(body.data.id, body.data.comment_content, body.data.comment_like_count.toString(), body.data.comment_nickname, check_comment_profile2, body.data.comment_title, body.data.comment_user_type, body.data.comment_username, body.data.updated_at, comment_my_item_count)
                            )
                            recyclerView=vh.itemView.findViewById(R.id.comment_recycler) as RecyclerView
                            val reverse_manager = LinearLayoutManager(context)
                            reverse_manager.reverseLayout = true
                            reverse_manager.stackFromEnd = true
                            recyclerView.layoutManager = reverse_manager
                            recyclerView.adapter = CommentAdapter(context, comment_items)
                        }

                        Log.d("TEST", "createComment 통신성공 바디 -> $body")
                        Log.d("TEST", "createComment 코드확인 -> ${response.code()}")
                    }

                    override fun onFailure(call: Call<CreateCommentData>, t: Throwable) {
                        Log.d("TEST", "createComment 통신실패 에러 -> " + t.message)
                    }
                })
            }
        }

        // 게시글 추가메뉴 클릭이벤트
        vh.itemView.board_user_more.setOnClickListener {
            val popup = PopupMenu(context, vh.itemView.board_user_more)

            val inf: MenuInflater = popup.menuInflater
            inf.inflate(R.menu.board_menu, popup.menu)

            popup.setOnMenuItemClickListener { menu_item ->
                when (menu_item.itemId) {
                    R.id.board_update_menu -> {
                        Log.d("TEST", "메뉴 - 수정버튼 클릭")
                        val bo_parm:HashMap<String, String> = HashMap()
                        bo_parm["access_token"] = access_token
                        bo_parm["username"] = item.board_username!!
                        bo_parm["board_id"] = item.id!!.toString()
                        bo_parm["board_title"] = item.board_title!!
                        bo_parm["board_content"] = item.board_content!!
                        MoveActivity().board_update_move(context as ChainActivity, bo_parm)
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