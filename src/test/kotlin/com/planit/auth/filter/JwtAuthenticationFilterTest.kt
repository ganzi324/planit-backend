package com.planit.auth.filter

import com.planit.auth.support.JwtProvider
import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import jakarta.servlet.FilterChain

class JwtAuthenticationFilterTest : BehaviorSpec({

    val jwtProvider: JwtProvider = mockk()
    val filter = JwtAuthenticationFilter(jwtProvider)

    beforeEach {
        SecurityContextHolder.clearContext()
    }

    given("유효한 JWT 토큰이 'Authorization' 헤더에 담겨 요청이 들어온 상황에서") {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain: FilterChain = mockk(relaxed = true)
        val token = "valid-token"
        val user = createTestUser()
        val authentication = UsernamePasswordAuthenticationToken(user.email, null, listOf(SimpleGrantedAuthority(user.role.key)))

        request.addHeader("Authorization", "Bearer $token")

        every { jwtProvider.validateToken(token) } returns true
        every { jwtProvider.getAuthentication(token) } returns authentication

        `when`("JwtAuthenticationFilter가 요청을 처리하면") {
            then("SecurityContext에 사용자의 인증 정보가 저장되고 다음 필터 체인이 호출되어야 한다") {
                filter.doFilter(request, response, filterChain)
                
                val storedAuthentication = SecurityContextHolder.getContext().authentication
                storedAuthentication.name shouldBe user.email
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }
    }
    
    given("유효하지 않은 토큰으로 요청이 들어오면") {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain: FilterChain = mockk(relaxed = true)
        val invalidToken = "invalid-token"
        request.addHeader("Authorization", "Bearer $invalidToken")
        
        every { jwtProvider.validateToken(invalidToken) } returns false

        `when`("필터가 요청을 처리하면") {
            then("SecurityContext는 비어있고 다음 필터 체인이 호출되어야 한다") {
                filter.doFilter(request, response, filterChain)
                
                SecurityContextHolder.getContext().authentication shouldBe null
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }
    }
    
    given("토큰 없이 요청이 들어오면") {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain: FilterChain = mockk(relaxed = true)

        `when`("필터가 요청을 처리하면") {
            then("SecurityContext는 비어있고 다음 필터 체인이 호출되어야 한다") {
                filter.doFilter(request, response, filterChain)
                
                SecurityContextHolder.getContext().authentication shouldBe null
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }
    }
})

private fun createTestUser(): User = User(
    email = "test@test.com",
    nickname = "testuser",
    provider = UserProvider.GOOGLE,
    providerId = "12345",
    role = Role.USER
) 