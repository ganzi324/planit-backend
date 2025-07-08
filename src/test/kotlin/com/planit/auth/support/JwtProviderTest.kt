package com.planit.auth.support

import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [JwtProvider::class])
class JwtProviderTest(
    private val jwtProvider: JwtProvider
) : BehaviorSpec({

    Given("JwtProvider가 주어졌을 때") {
        val user = User(
            email = "test@example.com",
            nickname = "testUser",
            provider = UserProvider.KAKAO,
            providerId = "testProviderId",
            role = Role.USER
        )
        // 테스트를 위해 리플렉션으로 ID를 설정합니다. BaseEntity의 ID setter가 protected이기 때문입니다.
        user.id = 1L

        When("토큰을 생성하면") {
            val token = jwtProvider.createToken(user)

            Then("토큰이 정상적으로 생성된다") {
                token shouldNotBe null
            }

            Then("생성된 토큰을 파싱하면 원래의 사용자 ID가 나온다") {
                val parsedUserId = jwtProvider.getUserId(token)
                parsedUserId shouldBe user.id
            }
        }
    }
}) 