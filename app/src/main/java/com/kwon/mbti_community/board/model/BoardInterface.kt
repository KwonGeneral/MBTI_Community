package com.kwon.mbti_community.board.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BoardInterface {
    // 일상 게시판 조회 -> 페이지 넘버로 수정할 필요 있음
    @GET("/board/")
    fun getBoard(
        @Query("board_type") board_type: String
    ): Call<GetBoardData>

    // 게시글 생성
    @POST("/board/")
    fun createBoard(
        @Body parameters: HashMap<String, String>
    ): Call<CreateBoardData>

    // 게시판 좋아요 클릭
    @POST("/like/board/")
    fun likeBoard(
        @Body parameters: HashMap<String, Int>
    ): Call<LikeBoardData>

    // 코멘트 조회
    @GET("/board/comment/")
    fun getComment(
        @Query("board_id") board_id: Int
    ): Call<GetCommentData>
}