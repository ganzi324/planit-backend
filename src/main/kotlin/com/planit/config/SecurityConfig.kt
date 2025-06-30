package com.planit.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // CSRF 보호 비활성화 (Stateless API)
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() } // H2 Console 접근을 위한 설정
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/favicon.ico").permitAll()
                    .requestMatchers("/login", "/oauth2/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/login") // 사용자 정의 로그인 페이지 (나중에 만들 수 있음)
            }

        return http.build()
    }
} 