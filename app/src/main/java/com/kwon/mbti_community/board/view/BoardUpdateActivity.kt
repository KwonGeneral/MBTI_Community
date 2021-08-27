package com.kwon.mbti_community.board.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kwon.mbti_community.R
import com.kwon.mbti_community.board.model.BoardInterface
import com.kwon.mbti_community.board.model.UpdateBoardData
import com.kwon.mbti_community.board.model.UpdateCommentData
import com.kwon.mbti_community.mypage.model.GetUserData
import com.kwon.mbti_community.mypage.model.MypageInterface
import com.kwon.mbti_community.z_common.connect.Connect
import com.kwon.mbti_community.z_common.view.MoveActivity
import kotlinx.android.synthetic.main.activity_board_update.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardUpdateActivity : AppCompatActivity() {

    // 값 전달 변수
    var share_access_token:String? = null
    var share_username:String? = null
    var share_nickname:String? = null
    var share_password:String? = null
    var share_profile:String? = null
    var share_user_type:String? = null
    var share_message:String? = null

    // 게시글 변수
    var share_board_id:String? = null
    var share_board_title:String? = null
    var share_board_content:String? = null

    // 댓글 변수
    var share_comment_id:String? = null
    var share_comment_content:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_update)

        // 값 가져오기
        share_access_token = intent.getStringExtra("access_token").toString()
        share_username = intent.getStringExtra("username").toString()
        share_board_id = intent.getStringExtra("board_id").toString()
        share_board_title = intent.getStringExtra("board_title").toString()
        share_board_content = intent.getStringExtra("board_content").toString()
        share_comment_id = intent.getStringExtra("comment_id").toString()
        share_comment_content = intent.getStringExtra("comment_content").toString()

        Log.d("TEST", "share_access_token : $share_access_token")
        Log.d("TEST", "share_username : $share_username")
        Log.d("TEST", "share_board_id : $share_board_id")
        Log.d("TEST", "share_comment_id : $share_comment_id")
        Log.d("TEST", "share_board_title : $share_board_title")
        Log.d("TEST", "share_board_content : $share_board_content")
        Log.d("TEST", "share_comment_content : $share_comment_content")

        val conn = Connect().connect(share_access_token)
        val mypage_api: MypageInterface = conn.create(MypageInterface::class.java)
        val board_api: BoardInterface = conn.create(BoardInterface::class.java)

        if(share_board_id != "null" && share_board_title != "null" && share_board_content != "null") {
            board_update_layout.visibility = View.VISIBLE
            board_update_title_input.setText(share_board_title)
            board_update_content_input.setText(share_board_content)
        } else {
            board_update_layout.visibility = View.GONE
        }
        if(share_comment_id != "null" && share_comment_content != "null") {
            comment_update_layout.visibility = View.VISIBLE
            comment_update_content_input.setText(share_comment_content)
        } else {
            comment_update_layout.visibility = View.GONE
        }

        // API 통신 : 유저 정보 가져오기
        mypage_api.getUserData(share_username!!).enqueue(object: Callback<GetUserData> {
            override fun onResponse(call: Call<GetUserData>, response: Response<GetUserData>) {
                val body = response.body()
                if(body != null) {
                    share_access_token = body.data.access_token
                    share_username = body.data.user_info[0].username
                    share_nickname = body.data.user_info[0].nickname
                    share_password = body.data.user_info[0].password
                    share_profile = body.data.user_info[0].profile
                    share_user_type = body.data.user_info[0].user_type
                    share_message = body.data.user_info[0].message
                }
                Log.d("TEST", "getUserData 통신성공 바디 -> $body")
            }

            override fun onFailure(call: Call<GetUserData>, t: Throwable) {
                Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
            }
        })

        board_update_close_btn.setOnClickListener {
            var kkk_hash:HashMap<String, String> = HashMap()
            kkk_hash["access_token"] = share_access_token!!
            kkk_hash["username"] = share_username!!
            kkk_hash["nickname"] = share_nickname!!
            kkk_hash["password"] = share_password!!
            kkk_hash["profile"] = share_profile!!
            kkk_hash["user_type"] = share_user_type!!
            kkk_hash["message"] = share_message!!
            kkk_hash["move_status"] = "1"
            MoveActivity().chain_move(this, kkk_hash)
        }

        // 글 수정
        board_update_submit_btn.setOnClickListener {
            var board_parm:HashMap<String, String> = HashMap()
            board_parm["board_title"] = board_update_title_input.text.toString()
            board_parm["board_content"] = board_update_content_input.text.toString()
            // API 통신 : 글 수정
            board_api.updateBoard(share_board_id!!, board_parm).enqueue(object: Callback<UpdateBoardData> {
                override fun onResponse(call: Call<UpdateBoardData>, response: Response<UpdateBoardData>) {
                    val body = response.body()
                    if(body != null) {
                        var ggg_hash:HashMap<String, String> = HashMap()
                        ggg_hash["access_token"] = share_access_token!!
                        ggg_hash["username"] = share_username!!
                        ggg_hash["nickname"] = share_nickname!!
                        ggg_hash["password"] = share_password!!
                        ggg_hash["profile"] = share_profile!!
                        ggg_hash["user_type"] = share_user_type!!
                        ggg_hash["message"] = share_message!!
                        ggg_hash["move_status"] = "1"
                        MoveActivity().chain_move(this@BoardUpdateActivity, ggg_hash)
                    }
                    Log.d("TEST", "getUserData 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<UpdateBoardData>, t: Throwable) {
                    Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
                }
            })
        }

        // 댓글 수정
        comment_update_submit_btn.setOnClickListener {
            var comment_parm:HashMap<String, String> = HashMap()
            comment_parm["comment_content"] = comment_update_content_input.text.toString()
            // API 통신 : 댓글 수정
            board_api.updateComment(share_comment_id!!, comment_parm).enqueue(object: Callback<UpdateCommentData> {
                override fun onResponse(call: Call<UpdateCommentData>, response: Response<UpdateCommentData>) {
                    val body = response.body()
                    if(body != null) {
                        var rrr_hash:HashMap<String, String> = HashMap()
                        rrr_hash["access_token"] = share_access_token!!
                        rrr_hash["username"] = share_username!!
                        rrr_hash["nickname"] = share_nickname!!
                        rrr_hash["password"] = share_password!!
                        rrr_hash["profile"] = share_profile!!
                        rrr_hash["user_type"] = share_user_type!!
                        rrr_hash["message"] = share_message!!
                        rrr_hash["move_status"] = "1"
                        MoveActivity().chain_move(this@BoardUpdateActivity, rrr_hash)
                    }
                    Log.d("TEST", "getUserData 통신성공 바디 -> $body")
                }

                override fun onFailure(call: Call<UpdateCommentData>, t: Throwable) {
                    Log.d("TEST", "getUserData 통신실패 에러 -> " + t.message)
                }
            })
        }

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        Log.d("TEST", "MypageProfileUpdateActivity - onBackPressed")
        var zzz_hash:HashMap<String, String> = HashMap()
        zzz_hash["access_token"] = share_access_token!!
        zzz_hash["username"] = share_username!!
        zzz_hash["nickname"] = share_nickname!!
        zzz_hash["password"] = share_password!!
        zzz_hash["profile"] = share_profile!!
        zzz_hash["user_type"] = share_user_type!!
        zzz_hash["message"] = share_message!!
        zzz_hash["move_status"] = "1"
        MoveActivity().chain_move(this, zzz_hash)
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "MypageProfileUpdateActivity - onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "MypageProfileUpdateActivity - onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "MypageProfileUpdateActivity - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "MypageProfileUpdateActivity - onDestroy")
    }
}