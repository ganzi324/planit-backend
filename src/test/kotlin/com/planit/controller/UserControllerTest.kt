package com.planit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.planit.config.TestSecurityConfig
import com.planit.dto.UserResponse
import com.planit.service.UserService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
@Import(TestSecurityConfig::class)
@ContextConfiguration(classes = [UserControllerTest.TestConfig::class])
class UserControllerTest(
    private val mockMvc: MockMvc,
    private val userService: UserService
) : BehaviorSpec({

    val userId = 1L
    val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
    val testAuthentication = UsernamePasswordAuthenticationToken(userId, null, authorities)

    Given("인증된 사용자가 내 정보 조회를 요청하면") {
        val userResponse = UserResponse(
            id = userId,
            email = "test@example.com",
            nickname = "testuser",
            profileImageUrl = "http://example.com/profile.jpg"
        )

        every { userService.getUserInfo(userId) } returns userResponse

        When("GET /api/users/me를 호출하면") {
            Then("200 OK 상태 코드와 함께 사용자 정보가 반환된다") {
                mockMvc.perform(
                    get("/api/users/me")
                        .with(authentication(testAuthentication))
                        .accept(MediaType.APPLICATION_JSON)
                )
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(userId))
                    .andExpect(jsonPath("$.email").value("test@example.com"))
                    .andExpect(jsonPath("$.nickname").value("testuser"))
            }
        }
    }
}) {
    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun userService(): UserService = mockk(relaxed = true)
    }
} 