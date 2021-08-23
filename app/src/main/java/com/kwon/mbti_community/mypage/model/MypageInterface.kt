package com.kwon.mbti_community.mypage.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MypageInterface {
    // 게시판 조회 -> 페이지 넘버로 수정할 필요 있음
    @GET("/board/user_board_count/")
    fun getBoardCount(
    ): Call<GetBoardCountData>
}