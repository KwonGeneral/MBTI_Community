package com.kwon.mbti_community.mypage.model

data class GetBoardCountData(
    val code: String,
    val data: GetArrayBoardCountData,
    val message: String
)

data class GetArrayBoardCountData(
    val board_total_count: Int
)