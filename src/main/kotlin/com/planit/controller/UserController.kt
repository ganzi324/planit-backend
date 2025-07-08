package com.planit.controller

import com.planit.dto.UserResponse
import com.planit.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMe(@AuthenticationPrincipal userId: Long): ResponseEntity<UserResponse> {
        val user = userService.getUserInfo(userId)
        return ResponseEntity.ok(user)
    }
} 