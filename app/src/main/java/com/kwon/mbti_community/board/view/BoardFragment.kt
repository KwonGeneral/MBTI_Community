package com.kwon.mbti_community.board.view

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
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.mbti_community.board.adapter.BoardAdapter
import com.kwon.mbti_community.board.adapter.BoardItem
import com.kwon.mbti_community.board.model.BoardData
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.z_common.connect.Connect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var recyclerView: RecyclerView
    var items = arrayListOf<BoardItem>()
    var items_no_list = arrayListOf<String>()

    companion object{
        fun newInstance() : BoardFragment {
            return BoardFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","BoardFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","BoardFragment - onAttach")
    }

    fun Int.dp(): Int { //함수 이름도 직관적으로 보이기 위해 dp()로 바꿨습니다.
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","BoardFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_board, container, false)

        val access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imt3b250YWV3YW4iLCJwYXNzd29yZCI6IlExSlFJV3BMYWNlTmkzTnppNXEzZXc9PSIsInRva2VuX3R5cGUiOiJhY2Nlc3NfdG9rZW4iLCJ0aW1lIjoiMjAyMS0wOC0yMCAxNzoyNjo1OSJ9.XTxTH9CwpnKwlDyP2XgZ-fLClN9KqtJFDgLfC0sVGFQ"
        val conn = Connect().connect(access_token)
        val board_api: BoardInterface = conn.create(BoardInterface::class.java)

        board_api.getBoard().enqueue(object: Callback<BoardData> {
            override fun onResponse(call: Call<BoardData>, response: Response<BoardData>) {
                val body = response.body()

                if(body != null){
                    for(nn in body.data) {
                        Log.d("TEST", "하하하 : $nn")
                        items.add(
                            BoardItem(nn.id, nn.board_content, nn.board_like_count.toString(), nn.board_nickname, nn.board_profile, nn.board_title, nn.board_type, nn.board_user_type, nn.board_username, nn.updated_at)
                        )
                    }


                    recyclerView=view.findViewById(R.id.board_recycler) as RecyclerView
                    val reverse_manager = LinearLayoutManager(requireContext())
                    reverse_manager.reverseLayout = true
                    reverse_manager.stackFromEnd = true
//
                    recyclerView.layoutManager = reverse_manager
                    recyclerView.adapter= BoardAdapter(requireContext(), items)
                }
                Log.d("TEST", "왔다~ -> $body")
            }

            override fun onFailure(call: Call<BoardData>, t: Throwable) {
                Log.d("TEST", "Failed : 실패입니다 11 -> " + t.message)
            }
        })

        val bundle = Bundle()
        val bundle_arguments = arguments
        val username = bundle_arguments?.getString("username").toString()
        bundle.putString("username", username)

        val select_text = view.findViewById<TextView>(R.id.select_text)
        val top_select_layout = view.findViewById<LinearLayout>(R.id.top_select_layout)
        val only_select_layout = view.findViewById<LinearLayout>(R.id.only_select_layout)
        val top_mbit_menu_layout = view.findViewById<LinearLayout>(R.id.top_mbit_menu_layout)
        val board_top_layout = view.findViewById<LinearLayout>(R.id.board_top_layout)
        val board_scroll_view = view.findViewById<ScrollView>(R.id.board_scroll_view).layoutParams as ViewGroup.MarginLayoutParams

        top_select_layout.setOnClickListener {
            Log.d("TEST", "???? : ${top_mbit_menu_layout.isVisible}")
            if(top_mbit_menu_layout.isVisible) {
                only_select_layout.visibility = View.VISIBLE
                top_mbit_menu_layout.visibility = View.GONE
                board_top_layout.layoutParams.height = 100.dp()
                board_scroll_view.setMargins(10.dp(), 60.dp(), 10.dp(), 0)
            }else {
                only_select_layout.visibility = View.GONE
                top_mbit_menu_layout.visibility = View.VISIBLE
                board_top_layout.layoutParams.height = 200.dp()
                board_scroll_view.setMargins(10.dp(), 180.dp(), 10.dp(), 0)
            }
        }

        fun change_option(select_item:TextView) {
            only_select_layout.visibility = View.VISIBLE
            top_mbit_menu_layout.visibility = View.GONE
            board_top_layout.layoutParams.height = 100.dp()
            board_scroll_view.setMargins(10.dp(), 60.dp(), 10.dp(), 0)
            select_text.text = select_item.text
        }

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
        Log.d("TEST", "BoardFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "BoardFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "BoardFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "BoardFragment - onDestroy")
    }
}