package com.kwon.mbti_community.board.model

data class BoardData(
    val code: String,
    val data: List<BoardArrayData>,
    val message: String
)

data class BoardArrayData(
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