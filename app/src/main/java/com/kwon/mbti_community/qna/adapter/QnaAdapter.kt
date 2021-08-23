package com.kwon.mbti_community.qna.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.LikeBoardData
import com.kwon.mbti_community.z_common.connect.Connect
import kotlinx.android.synthetic.main.fragment_qna_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QnaAdapter constructor(var context:Context, var items:ArrayList<QnaItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imt3b250YWV3YW4iLCJwYXNzd29yZCI6IlExSlFJV3BMYWNlTmkzTnppNXEzZXc9PSIsInRva2VuX3R5cGUiOiJhY2Nlc3NfdG9rZW4iLCJ0aW1lIjoiMjAyMS0wOC0yMyAyMDo0Njo1MSJ9.-BLsZru-g7ZdRr5KFiV4Yvt3036uHAWTk38tzaO5JwQ"
    val conn = Connect().connect(access_token)
    val qna_api: BoardInterface = conn.create(BoardInterface::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.fragment_qna_item, parent, false)

        return VH(itemView)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh: VH =holder as VH

        val item= items[position]

//        vh.itemView.qna_user_profile = item.qna_profile
        vh.itemView.qna_user_nickname.text = item.board_nickname
        vh.itemView.qna_user_title.text = item.board_title
        vh.itemView.qna_user_content.text = item.board_content
        vh.itemView.qna_like_count.text = item.board_like_count.toString()

        vh.itemView.qna_like_btn.setOnClickListener {
            val parameter:HashMap<String, Int> = HashMap()
            parameter["board_id"] = item.id

            qna_api.likeBoard(parameter).enqueue(object: Callback<LikeBoardData> {
                override fun onResponse(call: Call<LikeBoardData>, response: Response<LikeBoardData>) {
                    val body = response.body()
                    if(body != null) {
                        if(body.data.username != "kwontaewan") {
                            vh.itemView.qna_like_count.text = (vh.itemView.qna_like_count.text.toString().toInt() + 1).toString()
                        }else {
                            vh.itemView.qna_like_count.text = (vh.itemView.qna_like_count.text.toString().toInt() - 1).toString()
                        }
                    }

                    Log.d("TEST", "likeqna 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<LikeBoardData>, t: Throwable) {
                    Log.d("TEST", "likeqna 통신실패 에러 -> " + t.message)
                }
            })
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