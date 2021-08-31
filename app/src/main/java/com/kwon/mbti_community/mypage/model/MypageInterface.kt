package com.kwon.mbti_community.mypage.model

import com.kwon.mbti_community.board.model.GetBoardData
import com.kwon.mbti_community.board.model.GetCommentData
import com.kwon.mbti_community.login.model.LoginData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MypageInterface {
    // 유저 정보 가져오기
    @GET("/account/user/")
    fun getUserData(
        @Query("username") username: String,
    ): Call<GetUserData>

    // 유저 게시글 카운트
    @GET("/board/user_board_count/")
    fun getBoardCount(
        @Query("username") username: String,
    ): Call<GetBoardCountData>

    // 유저가 올린 게시글만 가져오기
    @GET("/board/")
    fun getUserBoard(
        @Query("board_username") board_username: String,
        @Query("page") page: String
    ): Call<GetUserBoardData>

    // 회원정보 프로필 수정
    @Multipart
    @PUT("/account/user/update/{username}")
    fun updateUserProfile(
        @Path("username") username:String,
        @Part part: MultipartBody.Part?,
    ): Call<UpdateUserProfileData>

    // 회원정보 데이터 수정
    @PUT("/account/user/update/{username}")
    fun updateUserInfo(
        @Path("username") username:String,
        @Body parameters: HashMap<String, String>
    ): Call<UpdateUserInfoData>
}