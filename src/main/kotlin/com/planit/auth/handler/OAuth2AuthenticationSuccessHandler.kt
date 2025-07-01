package com.planit.auth.handler

import com.planit.auth.support.JwtProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2AuthenticationSuccessHandler(
    private val jwtProvider: JwtProvider
) : AuthenticationSuccessHandler {

    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as OAuth2User
        val userId = authentication.name
        val email = principal.attributes["email"] as String
        // 안전한 단일 권한 추출 (시스템에서 단일 권한만 사용)
        val role = authentication.authorities
            .singleOrNull()?.authority
            ?: throw IllegalStateException("User must have exactly one authority, but found: ${authentication.authorities.size}")

        val token = jwtProvider.createToken(userId, email, role)
        val redirectUrl = "/?token=$token"

        redirectStrategy.sendRedirect(request, response, redirectUrl)
    }
} 