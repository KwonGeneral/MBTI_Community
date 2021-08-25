package com.kwon.mbti_community.mypage.model

import com.kwon.mbti_community.board.model.GetBoardData
import com.kwon.mbti_community.board.model.GetCommentData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MypageInterface {
    // 유저 게시글 카운트
    @GET("/board/user_board_count/")
    fun getBoardCount(
    ): Call<GetBoardCountData>

    // 유저가 올린 게시글만 가져오기
    @GET("/board/")
    fun getUserBoard(
        @Query("board_username") board_username: String
    ): Call<GetUserBoardData>
}