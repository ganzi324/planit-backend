package com.planit.service

import com.planit.auth.dto.GoogleUserInfo
import com.planit.auth.dto.KakaoUserInfo
import com.planit.auth.dto.NaverUserInfo
import com.planit.auth.dto.OAuth2UserInfo
import com.planit.domain.entity.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import com.planit.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val registrationId = userRequest.clientRegistration.registrationId
        return processOAuth2User(oAuth2User, registrationId)
    }

    @Transactional
    fun processOAuth2User(oAuth2User: OAuth2User, registrationId: String): OAuth2User {
        val oAuth2UserInfo = getOAuth2UserInfo(registrationId, oAuth2User.attributes)
        val provider = UserProvider.valueOf(registrationId.uppercase())
        val providerId = oAuth2UserInfo.providerId

        val savedUser = userRepository.findByProviderAndProviderId(provider, providerId)

        val user = savedUser ?: userRepository.save(
            User(
                provider = provider,
                providerId = providerId,
                email = oAuth2UserInfo.email,
                nickname = oAuth2UserInfo.nickname,
                role = Role.USER
            )
        )

        return DefaultOAuth2User(
            user.getAuthorities(),
            oAuth2User.attributes,
            "sub" // Google 기준. Naver, Kakao는 다른 값 사용 필요 -> 추후 개선
        )
    }

    private fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return when (registrationId.lowercase()) {
            "google" -> GoogleUserInfo(attributes)
            "naver" -> NaverUserInfo(attributes)
            "kakao" -> KakaoUserInfo(attributes)
            else -> throw IllegalArgumentException("Unsupported provider: $registrationId")
        }
    }
} 