package com.kwon.mbti_community.write.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.CreateBoardData
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.PasswordCheck
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WriteFragment : Fragment(), AdapterView.OnItemSelectedListener {

    // 값 전달 변수
    var share_access_token = ""
    var share_username = ""
    var share_nickname = ""
    var share_password = ""
    var share_profile = ""
    var share_user_type = ""
    var share_message = ""

    var temp_board_type:String = "daily"

    companion object{
        fun newInstance() : WriteFragment {
            return WriteFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST","WriteFragment - onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST","WriteFragment - onAttach")
    }

    @SuppressLint("ResourceType", "CutPasteId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TEST","WriteFragment - onCreateView")
        val view=inflater.inflate(R.layout.fragment_write, container, false)

        // 프로그레스바 설정
        val write_progress_layout = view.findViewById<LinearLayout>(R.id.write_progress_layout)
        write_progress_layout.bringToFront()

        // 값 전달
        val bundle = Bundle()
        val bundle_arguments = arguments
        share_access_token = bundle_arguments?.getString("access_token").toString()
        share_username = bundle_arguments?.getString("username").toString()
        share_nickname = bundle_arguments?.getString("nickname").toString()
        share_password = bundle_arguments?.getString("password").toString()
        share_profile = bundle_arguments?.getString("profile").toString()
        share_user_type = bundle_arguments?.getString("user_type").toString()
        share_message = bundle_arguments?.getString("message").toString()

        Log.d("TEST", "share_access_token : $share_access_token")
        Log.d("TEST", "share_username : $share_username")
        Log.d("TEST", "share_nickname : $share_nickname")
        Log.d("TEST", "share_password : $share_password")
        Log.d("TEST", "share_profile : $share_profile")
        Log.d("TEST", "share_user_type : $share_user_type")
        Log.d("TEST", "share_message : $share_message")

        share_profile = share_profile.replace("http://kwonputer.com/media/", "https://kwonputer.com/media/")

        // 전체 레이아웃 클릭 시, 포커스 해제
        val write_frame_layout = view.findViewById<FrameLayout>(R.id.write_frame_layout)
        write_frame_layout.setOnClickListener {
            Log.d("TEST", "write_frame_layout write_frame_layout write_frame_layout")
            // 키보드 내리기
            val mInputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
        }
        val write_main_layout = view.findViewById<LinearLayout>(R.id.write_main_layout)
        write_main_layout.setOnClickListener {
            Log.d("TEST", "write_main_layout write_main_layout write_main_layout")
            // 키보드 내리기
            val mInputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
        }

        // API 셋팅
        val access_token = share_access_token
        val conn = Connect().connect(access_token)
        val board_api: BoardInterface = conn.create(BoardInterface::class.java)

        val write_submit_btn = view.findViewById<Button>(R.id.write_submit_btn)
        val user_mbti = view.findViewById<TextView>(R.id.user_mbti)
        user_mbti.text = "$share_user_type - 일상"

        // Input 길이 제한
        fun EditText.setMaxLength(maxLength: Int){
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
        view.findViewById<TextInputEditText>(R.id.write_title_input).setMaxLength(50)
        view.findViewById<TextInputEditText>(R.id.write_content_input).setMaxLength(200)
        view.findViewById<TextInputEditText>(R.id.write_content_input).maxLines = 8

        // 작성 완료 버튼 클릭
        write_submit_btn.setOnClickListener {
            val write_title_input = view.findViewById<TextInputEditText>(R.id.write_title_input).text.toString()
            val write_content_input = view.findViewById<TextInputEditText>(R.id.write_content_input).text.toString()
            view.findViewById<TextInputEditText>(R.id.write_title_input).text!!.clear()
            view.findViewById<TextInputEditText>(R.id.write_content_input).text!!.clear()

            // 키보드 내리기
            val mInputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            Log.d("TEST", "write_title_input : $write_title_input")
            Log.d("TEST", "write_content_input : $write_content_input")

            if(write_title_input.replace(" ", "") == "" || write_content_input.replace(" ", "") == "") {
                return@setOnClickListener
            }

            write_progress_layout.visibility = View.VISIBLE

            // 게시글 생성 API
            val parameter:HashMap<String, String> = HashMap()
            parameter["board_profile"] = share_profile
            parameter["board_title"] = write_title_input
            parameter["board_content"] = write_content_input
            parameter["board_type"] = temp_board_type
            parameter["board_username"] = share_username
            parameter["board_like_count"] = "0"
            parameter["board_nickname"] = share_nickname
            parameter["board_user_type"] = share_user_type

            board_api.createBoard(parameter).enqueue(object: Callback<CreateBoardData> {
                override fun onResponse(call: Call<CreateBoardData>, response: Response<CreateBoardData>) {
                    write_progress_layout.visibility = View.GONE
                    val body = response.body()

                    if(body != null) {
                        val snack: Snackbar = Snackbar
                            .make(view.findViewById<FrameLayout>(R.id.write_frame_layout), "게시글이 업로드 되었습니다.", 1000)
                            .setBackgroundTint(Color.parseColor("#ffffff"))
                            .setTextColor(Color.parseColor("#ba000000"))

                        val snack_view = snack.view
                        val params = snack_view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.TOP
                        snack_view.layoutParams = params
                        snack.show()
                        
                    }

                    Log.d("TEST", "createBoard 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<CreateBoardData>, t: Throwable) {
                    write_progress_layout.visibility = View.GONE
                    Log.d("TEST", "createBoard 통신실패 에러 -> " + t.message)
                }
            })
        }

        val write_select_daily = view.findViewById<TextView>(R.id.write_select_daily)
        val write_select_free = view.findViewById<TextView>(R.id.write_select_free)
        val write_select_question = view.findViewById<TextView>(R.id.write_select_question)
        val write_select_hobby = view.findViewById<TextView>(R.id.write_select_hobby)
        val write_select_job = view.findViewById<TextView>(R.id.write_select_job)
        val write_select_support = view.findViewById<TextView>(R.id.write_select_support)

        write_select_daily.setOnClickListener {
            temp_board_type = "daily"
            user_mbti.text = "$share_user_type - 일상"
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
            view.findViewById<EditText>(R.id.write_title_input).setText("")
            view.findViewById<EditText>(R.id.write_content_input).setText("")

            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_free.setOnClickListener {
            temp_board_type = "free"
            user_mbti.text = "$share_user_type - 자유"
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
            view.findViewById<EditText>(R.id.write_title_input).setText("")
            view.findViewById<EditText>(R.id.write_content_input).setText("")

            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_question.setOnClickListener {
            temp_board_type = "question"
            user_mbti.text = "$share_user_type - 질문"
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
            view.findViewById<EditText>(R.id.write_title_input).setText("")
            view.findViewById<EditText>(R.id.write_content_input).setText("")

            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_hobby.setOnClickListener {
            temp_board_type = "hobby"
            user_mbti.text = "$share_user_type - 취미"
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
            view.findViewById<EditText>(R.id.write_title_input).setText("")
            view.findViewById<EditText>(R.id.write_content_input).setText("")

            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_job.setOnClickListener {
            temp_board_type = "job"
            user_mbti.text = "$share_user_type - 직업"
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
            view.findViewById<EditText>(R.id.write_title_input).setText("")
            view.findViewById<EditText>(R.id.write_content_input).setText("")

            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
        write_select_support.setOnClickListener {
            temp_board_type = "support"
            user_mbti.text = "$share_user_type - 응원"
            // 포커스 해제
            view.findViewById<EditText>(R.id.write_title_input).clearFocus()
            view.findViewById<EditText>(R.id.write_content_input).clearFocus()
            view.findViewById<EditText>(R.id.write_title_input).setText("")
            view.findViewById<EditText>(R.id.write_content_input).setText("")

            write_select_daily.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_free.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_question.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_hobby.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_job.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            write_select_support.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C73279"))
        }

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "WriteFragment - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "WriteFragment - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "WriteFragment - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "WriteFragment - onDestroy")
    }
}