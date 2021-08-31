package com.kwon.mbti_community.board.adapter

import android.content.Context
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.DeleteBoardData
import com.kwon.mbti_community.board.model.DeleteCommentData
import com.kwon.mbti_community.board.model.LikeCommentData
import com.kwon.mbti_community.board.view.BoardFragment
import com.kwon.mbti_community.chain.view.ChainActivity
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import kotlinx.android.synthetic.main.fragment_board_item.view.*
import kotlinx.android.synthetic.main.fragment_comment_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class CommentAdapter constructor(var context:Context, var items:ArrayList<CommentItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 토큰 확인
    val app_file_path = context.getExternalFilesDir(null).toString()
    val token_file = File("$app_file_path/token.token")
    val access_token = token_file.readText().split("\n")[0]
    val username = token_file.readText().split("\n")[1]
    val conn = Connect().connect(access_token)
    val comment_api: BoardInterface = conn.create(BoardInterface::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.fragment_comment_item, parent, false)

        return VH(itemView)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh: VH =holder as VH

        val item= items[position]

        if(item.comment_nickname != null) {
            vh.itemView.comment_user_nickname.text = item.comment_nickname
            vh.itemView.comment_user_title.text = item.comment_title
            vh.itemView.comment_user_content.text = item.comment_content
            vh.itemView.comment_like_count.text = item.comment_like_count.toString()

            Glide.with(context)
                .load(item.comment_profile)
                .placeholder(R.drawable.user_default_profile)
                .error(R.drawable.user_default_profile)
                .into(vh.itemView.comment_user_profile)

            vh.itemView.comment_user_profile.setBackgroundResource(R.drawable.image_background_border)
            vh.itemView.comment_user_profile.clipToOutline = true

        } else {
            vh.itemView.comment_user_abled_layout.visibility = View.GONE
            vh.itemView.comment_user_disabled_layout.visibility = View.VISIBLE
        }

        Log.d("TEST", "??? : ${item.comment_profile}")

//        Glide.with(context)
//            .load(item.comment_profile)
//            .placeholder(R.drawable.user_default_profile)
//            .error(R.drawable.user_default_profile)
//            .into(vh.itemView.comment_user_profile)
//
//        vh.itemView.comment_user_profile.setBackgroundResource(R.drawable.image_background_border)
//        vh.itemView.comment_user_profile.clipToOutline = true

        if(item.my_item_count == 1) {
            vh.itemView.comment_user_more.visibility = View.VISIBLE
        } else {
            vh.itemView.comment_user_more.visibility = View.GONE

            vh.itemView.comment_user_profile.setOnClickListener {
                var ppp_hash:HashMap<String, String> = HashMap()
                ppp_hash["access_token"] = access_token
                ppp_hash["username"] = username
                ppp_hash["other_username"] = item.comment_username!!
                ppp_hash["other_profile"] = item.comment_profile!!
                MoveActivity().other_profile_move(context as ChainActivity, ppp_hash)
            }
        }

        vh.itemView.comment_like_btn_layout.setOnClickListener {
            val parameter:HashMap<String, Int> = HashMap()
            parameter["comment_id"] = item.id!!
            Log.d("TEST", "??? : ${item.id}")

            comment_api.likeComment(parameter).enqueue(object: Callback<LikeCommentData> {
                override fun onResponse(call: Call<LikeCommentData>, response: Response<LikeCommentData>) {
                    val body = response.body()
                    if(body != null) {
                        Log.d("TEST", "??? : ${body.data.username}")
                        if(body.code == "S0001") {
                            vh.itemView.comment_like_count.text = (vh.itemView.comment_like_count.text.toString().toInt() + 1).toString()
                        }else {
                            vh.itemView.comment_like_count.text = (vh.itemView.comment_like_count.text.toString().toInt() - 1).toString()
                        }
                    }

                    Log.d("TEST", "likeComment 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<LikeCommentData>, t: Throwable) {
                    Log.d("TEST", "likeComment 통신실패 에러 -> " + t.message)
                }
            })
        }

        // 댓글 추가메뉴 클릭이벤트
        vh.itemView.comment_user_more.setOnClickListener {
            val popup = PopupMenu(context, vh.itemView.comment_user_more)

            val inf: MenuInflater = popup.menuInflater
            inf.inflate(R.menu.board_menu, popup.menu)

            popup.setOnMenuItemClickListener { menu_item ->
                when (menu_item.itemId) {
                    R.id.board_update_menu -> {
                        Log.d("TEST", "메뉴 - 수정버튼 클릭")
                        val bo_parm:HashMap<String, String> = HashMap()
                        bo_parm["access_token"] = access_token
                        bo_parm["username"] = item.comment_username!!
                        bo_parm["comment_id"] = item.id!!.toString()
                        bo_parm["comment_content"] = item.comment_content!!
                        MoveActivity().board_update_move(context as ChainActivity, bo_parm)
                        true
                    }
                    R.id.board_delete_menu -> {
                        Log.d("TEST", "메뉴 - 삭제버튼 클릭")
                        val del_parm:HashMap<String, Int> = HashMap()
                        del_parm["comment_id"] = item.id!!
                        comment_api.deleteComment(del_parm).enqueue(object: Callback<DeleteCommentData> {
                            override fun onResponse(call: Call<DeleteCommentData>, response: Response<DeleteCommentData>) {
                                val body = response.body()
                                if(body != null) {
                                    items.removeAt(position)
                                    notifyDataSetChanged()
                                }
                                Log.d("TEST", "deleteComment 통신성공 바디 -> $body")
                            }

                            override fun onFailure(call: Call<DeleteCommentData>, t: Throwable) {
                                Log.d("TEST", "deleteComment 통신실패 에러 -> " + t.message)
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