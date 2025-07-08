package com.planit.auth.handler

import com.planit.auth.support.JwtProvider
import com.planit.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2AuthenticationSuccessHandler(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as OAuth2User
        val email = oAuth2User.attributes["email"] as String

        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found with email: $email")

        val token = jwtProvider.createToken(user)
        response.addHeader("Authorization", "Bearer $token")
        response.sendRedirect("/") // 로그인 성공 후 리다이렉트
    }
} 