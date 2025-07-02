package com.planit.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@TestConfiguration
@EnableWebSecurity
class TestSecurityConfig {
    @Bean
    fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/api/**").authenticated() // /api/로 시작하는 모든 경로는 인증이 필요하도록 설정
                    .anyRequest().permitAll()
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { request: HttpServletRequest, response: HttpServletResponse, authException ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                }
            }
        return http.build()
    }
} 