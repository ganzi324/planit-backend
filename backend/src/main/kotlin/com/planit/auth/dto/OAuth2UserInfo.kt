package com.planit.auth.dto

abstract class OAuth2UserInfo(
    val attributes: Map<String, Any>
) {
    abstract val providerId: String
    abstract val email: String
    abstract val nickname: String
}

class GoogleUserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {
    override val providerId: String
        get() = attributes["sub"] as String
    override val email: String
        get() = attributes["email"] as String
    override val nickname: String
        get() = attributes["name"] as String
}

class NaverUserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {
    private val response = attributes["response"] as Map<String, Any>

    override val providerId: String
        get() = response["id"] as String
    override val email: String
        get() = response["email"] as String
    override val nickname: String
        get() = response["nickname"] as String
}

class KakaoUserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {
    private val kakaoAccount = attributes["kakao_account"] as Map<String, Any>
    private val profile = kakaoAccount["profile"] as Map<String, Any>

    override val providerId: String
        get() = attributes["id"].toString()
    override val email: String
        get() = kakaoAccount["email"] as String
    override val nickname: String
        get() = profile["nickname"] as String
} 