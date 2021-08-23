package com.kwon.mbti_community.board.adapter

class BoardItem constructor(
    val id: Int,
    val board_content: String,
    val board_like_count: String,
    val board_nickname: String,
    val board_profile: String,
    val board_title: String,
    val board_type: String,
    val board_user_type: String,
    val board_username: String,
    val updated_at: String
) {
}