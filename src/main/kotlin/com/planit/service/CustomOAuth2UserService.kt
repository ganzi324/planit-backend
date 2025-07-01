package com.planit.service

import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import com.planit.repository.UserRepository
import org.springframework.context.annotation.Primary
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Primary
@Transactional(readOnly = true)
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
    private val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User>
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = delegate.loadUser(userRequest)

        val provider = UserProvider.valueOf(userRequest.clientRegistration.registrationId.uppercase())
        val providerId = oAuth2User.name
        val attributes = oAuth2User.attributes
        val email = attributes["email"] as String
        val nickname = attributes["name"] as String
        val picture = attributes["picture"] as String

        val user = userRepository.findByProviderAndProviderId(provider, providerId)
            ?.apply {
                this.nickname = nickname
                this.profileImageUrl = picture
            }
            ?: userRepository.save(
                User(
                    email = email,
                    nickname = nickname,
                    profileImageUrl = picture,
                    role = Role.USER,
                    provider = provider,
                    providerId = providerId
                )
            )

        return DefaultOAuth2User(
            listOf(SimpleGrantedAuthority(user.role.key)),
            attributes,
            userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
        )
    }
} 