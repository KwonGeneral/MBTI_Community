package com.kwon.mbti_community.mypage.model

data class GetBoardCountData(
    val code: String,
    val data: GetArrayBoardCountData,
    val message: String
)

data class GetArrayBoardCountData(
    val board_total_count: String
)

data class GetUserBoardData(
    val code: String,
    val data: List<GetArrayUserBoardData>,
    val message: String
)

data class GetArrayUserBoardData(
    val board_content: String,
    val board_like_count: Int,
    val board_nickname: String,
    val board_profile: String,
    val board_title: String,
    val board_type: String,
    val board_user_id: Int,
    val board_user_type: String,
    val board_username: String,
    val created_at: String,
    val id: Int,
    val updated_at: String
)

data class UpdateUserProfileData(
    val code: String,
    val data: String,
    val message: String
)

data class UpdateArrayUserData(
    val board_content: String,
    val board_like_count: Int,
    val board_nickname: String,
    val board_profile: String,
    val board_title: String,
    val board_type: String,
    val board_user_id: Int,
    val board_user_type: String,
    val board_username: String,
    val created_at: String,
    val id: Int,
    val updated_at: String
)