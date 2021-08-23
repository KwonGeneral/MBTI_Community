package com.kwon.mbti_community.board.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.R
import kotlinx.android.synthetic.main.fragment_board_item.view.*


class BoardAdapter constructor(var context:Context, var items:ArrayList<BoardItem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items_no_list = mutableSetOf<String>()
    var item_hash_map:HashMap<String, HashMap<String, String>> = HashMap()

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