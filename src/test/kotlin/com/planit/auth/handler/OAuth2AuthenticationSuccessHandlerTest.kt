package com.planit.auth.handler

import com.planit.auth.support.JwtProvider
import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import com.planit.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2AuthenticationSuccessHandlerTest : BehaviorSpec({

    val jwtProvider: JwtProvider = mockk()
    val userRepository: UserRepository = mockk()
    val successHandler = OAuth2AuthenticationSuccessHandler(jwtProvider, userRepository)

    Given("인증된 사용자와 HttpServletRequest, HttpServletResponse가 주어졌을 때") {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val attributes = mapOf("email" to "test@example.com")
        val principal: OAuth2User = mockk {
            every { getAttributes() } returns attributes
        }
        val authentication: Authentication = mockk {
            every { name } returns "testUser"
            every { getPrincipal() } returns principal
            every { authorities } returns listOf(SimpleGrantedAuthority("ROLE_USER"))
        }
        val generatedToken = "generated-jwt-token"
        val user = createTestUser(1L)

        every { jwtProvider.createToken(any()) } returns generatedToken
        every { userRepository.findByEmail(any()) } returns user

        When("onAuthenticationSuccess 핸들러가 호출되면") {
            successHandler.onAuthenticationSuccess(request, response, authentication)

            Then("응답 상태는 302 Found여야 한다") {
                response.status shouldBe 302
            }

            Then("응답 헤더는 토큰을 포함해야 한다") {
                response.headerNames.contains("Authorization") shouldBe true
            }
        }
    }
}) 

private fun createTestUser(id: Long): User {
    val user = User(
        email = "test@test.com",
        nickname = "testuser",
        provider = UserProvider.GOOGLE,
        providerId = "12345",
        role = Role.USER
    )
    user.id = id
    return user
}