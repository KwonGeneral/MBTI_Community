package com.kwon.mbti_community.board.adapter

class BoardItem constructor(
    val id: Int,
    val board_content: String?,
    val board_like_count: String?,
    val board_nickname: String?,
    val board_profile: String?,
    val board_title: String?,
    val board_type: String?,
    val board_user_type: String?,
    val board_username: String?,
    val updated_at: String?,
    val my_item_count: Int?, // 내 아이템인지 체크
) {
}

/*
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

 */