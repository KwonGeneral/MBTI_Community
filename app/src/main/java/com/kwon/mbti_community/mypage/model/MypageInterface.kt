package com.kwon.mbti_community.mypage.model

import com.kwon.mbti_community.board.model.GetBoardData
import com.kwon.mbti_community.board.model.GetCommentData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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

    // 회원정보 수정
    @Multipart
    @PUT("/account/user/update/{username}")
    fun updateUserInfo(
        @Path("username") username:String,
//        @Part("profile") profile: RequestBody
        @Part part: MultipartBody.Part
//        @Part("nickname") nickname:String,
//        @Part("profile") profile:String,
//        @Part("comment") comment:String,
//        @Part("user_type") user_type:String,
//        @Part("password") password:String,
    ): Call<UpdateUserProfileData>
}