package com.planit.service

import com.planit.dto.UserResponse
import com.planit.exception.UserNotFoundException
import com.planit.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getUserInfo(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException(userId)
        return UserResponse.from(user)
    }
} 