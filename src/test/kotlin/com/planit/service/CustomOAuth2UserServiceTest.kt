package com.planit.service

import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import com.planit.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2UserServiceTest : BehaviorSpec({

    val userRepository: UserRepository = mockk(relaxed = true)
    val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User> = mockk()

    val customOAuth2UserService = CustomOAuth2UserService(userRepository, delegate)

    val providerId = "123456789"
    val clientRegistration = ClientRegistration.withRegistrationId("google")
        .clientId("test-client-id")
        .clientSecret("test-client-secret")
        .clientAuthenticationMethod(org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
        .scope("profile", "email")
        .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
        .tokenUri("https://oauth2.googleapis.com/token")
        .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
        .userNameAttributeName("sub")
        .build()
    val userRequest = OAuth2UserRequest(clientRegistration, mockk(relaxed = true))

    Given("새로운 사용자가 로그인할 때") {
        val oauth2User: OAuth2User = DefaultOAuth2User(
            listOf(),
            mapOf("sub" to providerId, "name" to "테스트 유저", "email" to "test@gmail.com", "picture" to "https://example.com/profile.jpg"),
            "sub"
        )
        every { delegate.loadUser(userRequest) } returns oauth2User
        every { userRepository.findByProviderAndProviderId(any(), any()) } returns null
        every { userRepository.save(any()) } returnsArgument 0

        When("loadUser를 호출하면") {
            val result = customOAuth2UserService.loadUser(userRequest)

            Then("새로운 사용자가 저장되고, 반환된 OAuth2User의 이름은 providerId와 같다") {
                verify { userRepository.save(any()) }
                result.name shouldBe providerId
            }
        }
    }

    Given("기존 사용자가 로그인할 때") {
        val oauth2User = DefaultOAuth2User(
            emptyList(),
            mapOf(
                "sub" to providerId,
                "name" to "업데이트된 유저",
                "email" to "test@example.com",
                "picture" to "https://example.com/new_profile.jpg"
            ),
            "sub"
        )
        val existingUser = mockk<User>(relaxed = true) {
            every { role } returns Role.USER
        }
        every { delegate.loadUser(userRequest) } returns oauth2User
        every { userRepository.findByProviderAndProviderId(UserProvider.GOOGLE, providerId) } returns existingUser

        When("loadUser를 호출하면") {
            val result = customOAuth2UserService.loadUser(userRequest)

            Then("기존 사용자의 role으로 OAuth2User 객체를 생성하여 반환한다") {
                result.authorities.first().authority shouldBe Role.USER.key
            }
        }
    }
}) 