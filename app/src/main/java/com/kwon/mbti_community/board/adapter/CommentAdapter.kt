package com.kwon.mbti_community.board.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.view.BoardFragment
import com.kwon.mbti_community.z_common.connect.Connect
import kotlinx.android.synthetic.main.fragment_comment_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class CommentAdapter constructor(var context:Context, var items:ArrayList<CommentItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 토큰 확인
    val app_file_path = context.getExternalFilesDir(null).toString()
    val token_file = File("$app_file_path/token.token")
    val access_token = token_file.readText()
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

//        vh.itemView.comment_user_profile = item.comment_profile
        if(item.comment_nickname != null) {
            vh.itemView.comment_user_nickname.text = item.comment_nickname
            vh.itemView.comment_user_title.text = item.comment_title
            vh.itemView.comment_user_content.text = item.comment_content
            vh.itemView.comment_like_count.text = item.comment_like_count.toString()
        } else {
            vh.itemView.comment_user_abled_layout.visibility = View.GONE
            vh.itemView.comment_user_disabled_layout.visibility = View.VISIBLE
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