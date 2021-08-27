package com.kwon.mbti_community.board.model

data class GetBoardData(
    val code: String,
    val data: List<GetBoardArrayData>,
    val message: String
)

data class GetBoardArrayData(
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

data class GetBoardUserTypeData(
    val code: String,
    val data: List<GetBoardUserTypeArrayData>,
    val message: String
)

data class GetBoardUserTypeArrayData(
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

data class CreateBoardData(
    val code: String,
    val data: CreateBoardArrayData,
    val message: String
)

data class CreateBoardArrayData(
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

data class LikeBoardData(
    val code: String,
    val data: LikeBoardArrayData,
    val message: String
)

data class LikeBoardArrayData(
    val id: Int,
    val username: String,
    val board: Int,
)

data class LikeCommentData(
    val code: String,
    val data: LikeCommentArrayData,
    val message: String
)

data class LikeCommentArrayData(
    val id: Int,
    val username: String,
    val comment: Int,
)

data class GetCommentData(
    val code: String,
    val data: List<ArrayGetCommentData>,
    val message: String
)

data class ArrayGetCommentData(
    val board_id: Int,
    val comment_content: String,
    val comment_like_count: Int,
    val comment_nickname: String,
    val comment_profile: String,
    val comment_title: String,
    val comment_user_id: Int,
    val comment_user_type: String,
    val comment_username: String,
    val created_at: String,
    val id: Int,
    val updated_at: String
)

data class CreateCommentData (
    val code: String,
    val data: ArrayCreateCommentData,
    val message: String
)

data class ArrayCreateCommentData (
    val board_id: Int,
    val comment_content: String,
    val comment_like_count: Int,
    val comment_nickname: String,
    val comment_profile: String,
    val comment_title: String,
    val comment_user_id: Int,
    val comment_user_type: String,
    val comment_username: String,
    val created_at: String,
    val id: Int,
    val updated_at: String
)

data class DeleteBoardData(
    val code: String,
    val data: DeleteArrayBoardData,
    val message: String
)

data class DeleteArrayBoardData(
    val board_title: String,
    val id: Int
)

data class DeleteCommentData(
    val code: String,
    val data: DeleteArrayCommentData,
    val message: String
)

data class DeleteArrayCommentData(
    val comment_title: String,
    val id: Int
)

data class UpdateBoardData(
    val code: String,
    val data: UpdateArrayBoardData,
    val message: String
)

data class UpdateArrayBoardData(
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

data class UpdateCommentData(
    val code: String,
    val data: UpdateArrayCommentData,
    val message: String
)

data class UpdateArrayCommentData(
    val board_id: Int,
    val comment_content: String,
    val comment_like_count: Int,
    val comment_nickname: String,
    val comment_profile: String,
    val comment_title: String,
    val comment_user_id: Int,
    val comment_user_type: String,
    val comment_username: String,
    val created_at: String,
    val id: Int,
    val updated_at: String
)