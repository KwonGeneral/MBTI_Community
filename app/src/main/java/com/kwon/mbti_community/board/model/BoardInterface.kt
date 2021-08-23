package com.kwon.mbti_community.board.model

import retrofit2.Call
import retrofit2.http.GET

interface BoardInterface {
    @GET("/board/")
    fun getBoard(
//        @Query("get_email") get_email: CharSequence,
    ): Call<BoardData>
}