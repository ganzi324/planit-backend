package com.planit.service

import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import com.planit.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.*

class UserServiceTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val userService = UserService(userRepository)

    Given("존재하는 사용자의 ID가 주어졌을 때") {
        val userId = 1L
        val user = User(
            email = "test@example.com",
            nickname = "testuser",
            provider = UserProvider.GOOGLE,
            providerId = "12345",
            role = Role.USER,
            profileImageUrl = "http://example.com/profile.jpg"
        ).apply { id = userId }

        every { userRepository.findById(userId) } returns Optional.of(user)

        When("사용자 정보 조회를 요청하면") {
            val result = userService.getUserInfo(userId)

            Then("사용자 정보가 담긴 DTO가 반환되어야 한다") {
                result.id shouldBe userId
                result.email shouldBe "test@example.com"
                result.nickname shouldBe "testuser"
            }
        }
    }

    Given("존재하지 않는 사용자의 ID가 주어졌을 때") {
        val userId = 999L
        every { userRepository.findById(userId) } returns Optional.empty()

        When("사용자 정보 조회를 요청하면") {
            Then("IllegalArgumentException 예외가 발생해야 한다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    userService.getUserInfo(userId)
                }
                exception.message shouldBe "User not found with id: $userId"
            }
        }
    }
}) 