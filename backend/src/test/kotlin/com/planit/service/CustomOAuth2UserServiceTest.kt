package com.planit.service

import com.planit.auth.dto.GoogleUserInfo
import com.planit.auth.dto.KakaoUserInfo
import com.planit.auth.dto.NaverUserInfo
import com.planit.auth.dto.OAuth2UserInfo
import com.planit.domain.entity.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import com.planit.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User

@ExtendWith(MockKExtension::class)
class CustomOAuth2UserServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var customOAuth2UserService: CustomOAuth2UserService

    private lateinit var oAuth2User: OAuth2User

    @Nested
    @DisplayName("processOAuth2User 메소드는")
    inner class Describe_processOAuth2User {

        @Nested
        @DisplayName("신규 구글 사용자가 소셜 로그인을 하면")
        inner class Context_with_new_google_user {
            private val providerId = "provider-id-123"
            private val email = "new.user@example.com"
            private val name = "New User"
            private val registrationId = "google"

            @BeforeEach
            fun given() {
                val attributes = mapOf(
                    "sub" to providerId,
                    "email" to email,
                    "name" to name,
                    "picture" to "https://example.com/new_user.jpg"
                )
                oAuth2User = DefaultOAuth2User(
                    listOf(SimpleGrantedAuthority(Role.USER.key)),
                    attributes,
                    "sub"
                )

                every { userRepository.findByProviderAndProviderId(UserProvider.GOOGLE, providerId) } returns null
                every { userRepository.save(any()) } returns User(
                    provider = UserProvider.GOOGLE,
                    providerId = providerId,
                    email = email,
                    nickname = name,
                    role = Role.USER,
                )
            }

            @Test
            @DisplayName("새로운 사용자를 저장하고 유저 정보를 반환한다")
            fun it_saves_new_user_and_returns_oauth2user() {
                // When
                val result = customOAuth2UserService.processOAuth2User(oAuth2User, registrationId)

                // Then
                assertThat(result).isNotNull
                assertThat(result.name).isEqualTo(providerId)
                assertThat(result.attributes["email"]).isEqualTo(email)

                verify(exactly = 1) { userRepository.save(any()) }
            }
        }

        @Nested
        @DisplayName("기존 구글 사용자가 소셜 로그인을 하면")
        inner class Context_with_existing_google_user {
            private val providerId = "provider-id-456"
            private val email = "existing.user@example.com"
            private val name = "Existing User"
            private val registrationId = "google"
            private val existingUser = User(
                provider = UserProvider.GOOGLE,
                providerId = providerId,
                email = email,
                nickname = name,
                role = Role.USER,
            )


            @BeforeEach
            fun given() {
                val attributes = mapOf(
                    "sub" to providerId,
                    "email" to email,
                    "name" to name,
                    "picture" to "https://example.com/existing_user.jpg"
                )
                oAuth2User = DefaultOAuth2User(
                    listOf(SimpleGrantedAuthority(Role.USER.key)),
                    attributes,
                    "sub"
                )
                every { userRepository.findByProviderAndProviderId(UserProvider.GOOGLE, providerId) } returns existingUser
            }

            @Test
            @DisplayName("기존 정보를 반환한다")
            fun it_returns_existing_user_info() {
                // When
                val result = customOAuth2UserService.processOAuth2User(oAuth2User, registrationId)

                // Then
                assertThat(result).isNotNull
                assertThat(result.name).isEqualTo(providerId)
                assertThat(result.attributes["email"]).isEqualTo(email)

                verify(exactly = 0) { userRepository.save(any()) }
            }
        }
    }
} 