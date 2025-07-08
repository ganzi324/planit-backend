package com.planit.dto

import com.planit.domain.User

data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                email = user.email,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl
            )
        }
    }
} 