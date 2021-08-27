package com.kwon.mbti_community.qna.view

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
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.GetBoardData
import com.kwon.mbti_community.qna.adapter.QnaAdapter
import com.kwon.mbti_community.qna.adapter.QnaItem
import com.kwon.mbti_community.z_common.connect.Connect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnaFragment : Fragment(), AdapterView.OnItemSelectedListener {

    // 값 전달 변수
    var share_access_token = ""
    var share_username = ""
    var share_nickname = ""
    var share_password = ""
    var share_profile = ""
    var share_user_type = ""
    var share_message = ""

    lateinit var recyclerView: RecyclerView
    var items = arrayListOf<QnaItem>()

    companion object{
        fun newInstance() : QnaFragment {
            return QnaFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","QnaFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","QnaFragment - onAttach")
    }

    fun Int.dp(): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","QnaFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_qna, container, false)

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

        // API 셋팅
        val access_token = share_access_token
        val conn = Connect().connect(access_token)
        val board_api: BoardInterface = conn.create(BoardInterface::class.java)

        val qna_select_free = view.findViewById<TextView>(R.id.qna_select_free)
        val qna_select_question = view.findViewById<TextView>(R.id.qna_select_question)
        val qna_select_hobby = view.findViewById<TextView>(R.id.qna_select_hobby)
        val qna_select_job = view.findViewById<TextView>(R.id.qna_select_job)
        val qna_select_support = view.findViewById<TextView>(R.id.qna_select_support)

        fun getBoardApi(board_type:String) {
            items.clear()
            board_api.getBoard(board_type).enqueue(object: Callback<GetBoardData> {
                override fun onResponse(call: Call<GetBoardData>, response: Response<GetBoardData>) {
                    val body = response.body()

                    if(body != null){
                        for(nn in body.data) {
                            Log.d("TEST", "하하하 : $nn")
                            var my_item_count_check:Int
                            if(nn.board_username == share_username) { my_item_count_check = 1 } else { my_item_count_check = 0 }
                            items.add(
                                QnaItem(nn.id, nn.board_content, nn.board_like_count.toString(), nn.board_nickname, nn.board_profile, nn.board_title, nn.board_type, nn.board_user_type, nn.board_username, nn.updated_at, my_item_count_check)
                            )
                        }


                        recyclerView=view.findViewById(R.id.qna_recycler) as RecyclerView
                        val reverse_manager = LinearLayoutManager(requireContext())
                        reverse_manager.reverseLayout = true
                        reverse_manager.stackFromEnd = true
//
                        recyclerView.layoutManager = reverse_manager
                        recyclerView.adapter= QnaAdapter(requireContext(), items)
                    }

                    Log.d("TEST", "Qna - getBoard 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<GetBoardData>, t: Throwable) {
                    Log.d("TEST", "Qna - getBoard 통신실패 에러 -> " + t.message)
                }
            })
        }

        getBoardApi("free")

        qna_select_free.setOnClickListener {
            getBoardApi("free")
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_question.setOnClickListener {
            getBoardApi("question")
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_hobby.setOnClickListener {
            getBoardApi("hobby")
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_job.setOnClickListener {
            getBoardApi("job")
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        qna_select_support.setOnClickListener {
            getBoardApi("support")
            qna_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            qna_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
        }

        val select_text = view.findViewById<TextView>(R.id.select_text)
        val top_select_layout = view.findViewById<LinearLayout>(R.id.top_select_layout)
        val only_select_layout = view.findViewById<LinearLayout>(R.id.only_select_layout)
        val top_mbit_menu_layout = view.findViewById<LinearLayout>(R.id.top_mbit_menu_layout)
        val qna_top_layout = view.findViewById<LinearLayout>(R.id.qna_top_layout)
        val qna_top_rel_layout = view.findViewById<RelativeLayout>(R.id.qna_top_rel_layout)
        val qna_select_scroll_view = view.findViewById<ScrollView>(R.id.qna_select_scroll_view).layoutParams as ViewGroup.MarginLayoutParams
        val qna_main_scroll_view = view.findViewById<ScrollView>(R.id.qna_main_scroll_view).layoutParams as ViewGroup.MarginLayoutParams

        val menu_istp = view.findViewById<TextView>(R.id.menu_istp)
        val menu_isfp = view.findViewById<TextView>(R.id.menu_isfp)
        val menu_istj = view.findViewById<TextView>(R.id.menu_istj)
        val menu_isfj = view.findViewById<TextView>(R.id.menu_isfj)

        val menu_intp = view.findViewById<TextView>(R.id.menu_intp)
        val menu_infp = view.findViewById<TextView>(R.id.menu_infp)
        val menu_intj = view.findViewById<TextView>(R.id.menu_intj)
        val menu_infj = view.findViewById<TextView>(R.id.menu_infj)

        val menu_estp = view.findViewById<TextView>(R.id.menu_estp)
        val menu_esfp = view.findViewById<TextView>(R.id.menu_esfp)
        val menu_estj = view.findViewById<TextView>(R.id.menu_estj)
        val menu_esfj = view.findViewById<TextView>(R.id.menu_esfj)

        val menu_entp = view.findViewById<TextView>(R.id.menu_entp)
        val menu_enfp = view.findViewById<TextView>(R.id.menu_enfp)
        val menu_entj = view.findViewById<TextView>(R.id.menu_entj)
        val menu_enfj = view.findViewById<TextView>(R.id.menu_enfj)

        fun change_option(select_item:TextView) {
            only_select_layout.visibility = View.VISIBLE
            top_mbit_menu_layout.visibility = View.GONE
            qna_top_layout.layoutParams.height = 100.dp()
            qna_top_rel_layout.layoutParams.height = 120.dp()
//            qna_select_scroll_view.setMargins(10.dp(), 60.dp(), 10.dp(), 0)
            qna_main_scroll_view.setMargins(10.dp(), 120.dp(), 10.dp(), 0)
            select_text.text = select_item.text
        }

        change_option(menu_entp)

        top_select_layout.setOnClickListener {
            if(top_mbit_menu_layout.isVisible) {
                only_select_layout.visibility = View.VISIBLE
                top_mbit_menu_layout.visibility = View.GONE
                qna_top_layout.layoutParams.height = 100.dp()
                qna_top_rel_layout.layoutParams.height = 120.dp()
//                qna_select_scroll_view.setMargins(10.dp(), 60.dp(), 10.dp(), 0)
                qna_main_scroll_view.setMargins(10.dp(), 100.dp(), 10.dp(), 0)
                
            }else {
                only_select_layout.visibility = View.GONE
                top_mbit_menu_layout.visibility = View.VISIBLE
                qna_top_layout.layoutParams.height = 220.dp()
                qna_top_rel_layout.layoutParams.height = 240.dp()
//                qna_select_scroll_view.setMargins(10.dp(), 180.dp(), 10.dp(), 0)
                qna_main_scroll_view.setMargins(10.dp(), 240.dp(), 10.dp(), 0)
            }
        }

        menu_istp.setOnClickListener { change_option(menu_istp) }
        menu_isfp.setOnClickListener { change_option(menu_isfp) }
        menu_istj.setOnClickListener { change_option(menu_istj) }
        menu_isfj.setOnClickListener { change_option(menu_isfj) }

        menu_intp.setOnClickListener { change_option(menu_intp) }
        menu_infp.setOnClickListener { change_option(menu_infp) }
        menu_intj.setOnClickListener { change_option(menu_intj) }
        menu_infj.setOnClickListener { change_option(menu_infj) }

        menu_estp.setOnClickListener { change_option(menu_estp) }
        menu_esfp.setOnClickListener { change_option(menu_esfp) }
        menu_estj.setOnClickListener { change_option(menu_estj) }
        menu_esfj.setOnClickListener { change_option(menu_esfj) }

        menu_entp.setOnClickListener { change_option(menu_entp) }
        menu_enfp.setOnClickListener { change_option(menu_enfp) }
        menu_entj.setOnClickListener { change_option(menu_entj) }
        menu_enfj.setOnClickListener { change_option(menu_enfj) }

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "QnaFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "QnaFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "QnaFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "QnaFragment - onDestroy")
    }
}