package com.planit.service

import com.planit.domain.Schedule
import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.SchedulePriority
import com.planit.domain.enums.UserProvider
import com.planit.dto.ScheduleRequest
import com.planit.repository.ScheduleRepository
import com.planit.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class ScheduleServiceTest : BehaviorSpec({

    val scheduleRepository: ScheduleRepository = mockk()
    val userRepository: UserRepository = mockk()
    val scheduleService = ScheduleService(scheduleRepository, userRepository)

    Given("유효한 일정 생성 요청이 들어올 때") {
        val userEmail = "test@test.com"
        val user = createTestUser(userEmail)
        val scheduleRequest = ScheduleRequest(
            title = "새로운 회의",
            startDate = LocalDateTime.of(2024, 8, 15, 14, 0),
            endDate = LocalDateTime.of(2024, 8, 15, 15, 0),
            priority = SchedulePriority.HIGH
        )

        every { userRepository.findByEmail(userEmail) } returns user
        every { scheduleRepository.save(any<Schedule>()) } answers {
            val schedule = firstArg<Schedule>()
            schedule.apply {
                // BaseEntity의 id 필드 설정을 위한 리플렉션 사용 (테스트용)
                val idField = Schedule::class.java.superclass.getDeclaredField("id")
                idField.isAccessible = true
                idField.set(this, 1L)
            }
        }

        When("일정 생성 서비스를 호출하면") {
            val result = scheduleService.createSchedule(userEmail, scheduleRequest)

            Then("새로운 일정이 생성되고 응답 DTO가 반환되어야 한다") {
                result.id shouldBe 1L
                result.title shouldBe "새로운 회의"
                result.priority shouldBe SchedulePriority.HIGH
                result.isCompleted shouldBe false

                verify { userRepository.findByEmail(userEmail) }
                verify { scheduleRepository.save(any<Schedule>()) }
            }
        }
    }
})

private fun createTestUser(email: String): User = User(
    email = email,
    nickname = "testuser",
    provider = UserProvider.GOOGLE,
    providerId = "12345",
    role = Role.USER
) 