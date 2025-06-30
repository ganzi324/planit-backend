package com.planit.repository

import com.planit.config.JpaConfig
import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(JpaConfig::class)
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    @DisplayName("사용자를 저장하고 ID로 조회하면 정상적으로 조회된다")
    fun saveAndFindById() {
        // given
        val newUser = User(
            email = "test@test.com",
            nickname = "testuser",
            provider = UserProvider.GOOGLE,
            providerId = "12345",
            role = Role.USER
        )

        // when
        val savedUser = userRepository.save(newUser)

        // then
        val foundUser = userRepository.findById(savedUser.id!!).orElse(null)
        assertThat(foundUser).isNotNull
        assertThat(foundUser?.email).isEqualTo(newUser.email)
        assertThat(foundUser?.nickname).isEqualTo(newUser.nickname)
    }

    @Test
    @DisplayName("Provider와 ProviderId로 사용자를 조회한다")
    fun findByProviderAndProviderId() {
        // given
        val provider = UserProvider.GOOGLE
        val providerId = "123456789"
        val user = User(
            email = "test@google.com",
            nickname = "googleUser",
            provider = provider,
            providerId = providerId,
            role = Role.USER
        )
        userRepository.save(user)

        // when
        val foundUser = userRepository.findByProviderAndProviderId(provider, providerId).orElse(null)

        // then
        assertThat(foundUser).isNotNull
        assertThat(foundUser?.provider).isEqualTo(provider)
        assertThat(foundUser?.providerId).isEqualTo(providerId)
    }
} 