package com.planit.auth.support

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [JwtProvider::class])
class JwtProviderTest(
    private val jwtProvider: JwtProvider
) : BehaviorSpec({

    Given("JwtProvider가 주어졌을 때") {
        val userId = "testUser"
        val userEmail = "test@example.com"
        val userRole = "ROLE_USER"

        When("토큰을 생성하면") {
            val token = jwtProvider.createToken(userId, userEmail, userRole)

            Then("토큰이 정상적으로 생성된다") {
                token shouldNotBe null
            }

            Then("생성된 토큰을 파싱하면 원래의 이메일이 나온다") {
                val parsedEmail = jwtProvider.getEmail(token)
                parsedEmail shouldBe userEmail
            }
        }
    }
}) 