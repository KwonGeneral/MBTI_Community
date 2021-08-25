package com.kwon.mbti_community.board.adapter

class CommentItem constructor(
    val id: Int?,
    val comment_content: String?,
    val comment_like_count: String?,
    val comment_nickname: String?,
    val comment_profile: String?,
    val comment_title: String?,
    val comment_user_type: String?,
    val comment_username: String?,
    val updated_at: String?,
    val my_item_count: Int?, // 내 아이템인지 체크
) {
}