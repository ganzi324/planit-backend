package com.planit.service

import com.planit.domain.Schedule
import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.SchedulePriority
import com.planit.domain.enums.UserProvider
import com.planit.dto.ScheduleRequest
import com.planit.dto.ScheduleSearchCondition
import com.planit.repository.ScheduleRepository
import com.planit.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.Optional

class ScheduleServiceTest : BehaviorSpec({

    val scheduleRepository: ScheduleRepository = mockk()
    val userRepository: UserRepository = mockk()
    val scheduleService = ScheduleService(scheduleRepository, userRepository)

    Given("일정 목록 조회를 요청하면") {
        val userId = 1L
        val condition = ScheduleSearchCondition(year = 2024, month = 8, priority = null, isCompleted = false)
        val pageable = PageRequest.of(0, 10)
        val testSchedule = createTestSchedule(1L, createTestUser(userId))
        val schedulePage = PageImpl(listOf(testSchedule), pageable, 1)

        every { scheduleRepository.search(userId, condition, pageable) } returns schedulePage

        When("서비스를 호출하면") {
            val result = scheduleService.getSchedules(userId, condition, pageable)

            Then("레포지토리의 search 메소드를 호출하고 결과를 반환해야 한다") {
                result.totalElements shouldBe 1
                result.content[0].id shouldBe 1L
                verify { scheduleRepository.search(userId, condition, pageable) }
            }
        }
    }

    Given("유효한 일정 생성 요청이 들어올 때") {
        val userId = 1L
        val user = createTestUser(userId)
        val scheduleRequest = ScheduleRequest(
            title = "새로운 회의",
            startDate = LocalDateTime.of(2024, 8, 15, 14, 0),
            endDate = LocalDateTime.of(2024, 8, 15, 15, 0),
            priority = SchedulePriority.HIGH
        )

        every { userRepository.findById(userId) } returns Optional.of(user)
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
            val result = scheduleService.createSchedule(userId, scheduleRequest)

            Then("새로운 일정이 생성되고 응답 DTO가 반환되어야 한다") {
                result.id shouldBe 1L
                result.title shouldBe "새로운 회의"
                result.priority shouldBe SchedulePriority.HIGH
                result.isCompleted shouldBe false

                verify { userRepository.findById(userId) }
                verify { scheduleRepository.save(any<Schedule>()) }
            }
        }
    }

    Given("사용자가 자신의 일정을 수정하려고 할 때") {
        val userId = 1L
        val scheduleId = 1L
        val user = createTestUser(userId)
        val originalSchedule = createTestSchedule(scheduleId, user)

        val updateRequest = ScheduleRequest(
            title = "수정된 제목",
            description = "수정된 설명",
            startDate = LocalDateTime.now().plusDays(1),
            endDate = LocalDateTime.now().plusDays(1).plusHours(2),
            priority = SchedulePriority.LOW,
            alarmOffsetMinutes = 15
        )

        every { scheduleRepository.findById(scheduleId) } returns Optional.of(originalSchedule)

        When("유효한 내용으로 수정을 요청하면") {
            val result = scheduleService.updateSchedule(userId, scheduleId, updateRequest)

            Then("일정 정보가 정상적으로 수정되어야 한다") {
                result.title shouldBe "수정된 제목"
                result.priority shouldBe SchedulePriority.LOW
                verify { scheduleRepository.findById(scheduleId) }
            }
        }
    }

    Given("사용자가 다른 사람의 일정을 수정하려고 할 때") {
        val ownerId = 1L
        val attackerId = 2L
        val scheduleId = 1L
        val owner = createTestUser(ownerId)
        val schedule = createTestSchedule(scheduleId, owner)
        val updateRequest = ScheduleRequest(
            title = "해킹 시도",
            description = null,
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().plusHours(1),
            priority = SchedulePriority.HIGH
        )

        every { scheduleRepository.findById(scheduleId) } returns Optional.of(schedule)

        When("수정을 요청하면") {
            Then("IllegalArgumentException 예외가 발생해야 한다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    scheduleService.updateSchedule(attackerId, scheduleId, updateRequest)
                }
                exception.message shouldBe "User has no permission to update this schedule"
            }
        }
    }

    Given("사용자가 자신의 일정을 삭제하려고 할 때") {
        val userId = 1L
        val scheduleId = 1L
        val user = createTestUser(userId)
        val schedule = createTestSchedule(scheduleId, user)

        every { scheduleRepository.findById(scheduleId) } returns Optional.of(schedule)
        every { scheduleRepository.delete(any<Schedule>()) } returns Unit

        When("삭제를 요청하면") {
            scheduleService.deleteSchedule(userId, scheduleId)

            Then("ScheduleRepository의 delete 메서드가 호출되어야 한다") {
                verify { scheduleRepository.delete(schedule) }
            }
        }
    }

    Given("사용자가 일정의 완료 상태를 변경하려고 할 때") {
        val userId = 1L
        val scheduleId = 1L
        val user = createTestUser(userId)

        When("미완료 상태의 일정을 '완료(true)'로 변경을 요청하면") {
            val schedule = createTestSchedule(scheduleId, user, isCompleted = false)
            every { scheduleRepository.findById(scheduleId) } returns Optional.of(schedule)

            val result = scheduleService.updateCompletionStatus(userId, scheduleId, true)

            Then("일정이 완료 상태로 변경되어야 한다") {
                result.isCompleted shouldBe true
                schedule.isCompleted shouldBe true
            }
        }

        When("이미 완료된 상태에서 다시 '완료(true)'로 변경을 요청하면") {
            val schedule = createTestSchedule(scheduleId, user, isCompleted = true)
            every { scheduleRepository.findById(scheduleId) } returns Optional.of(schedule)

            Then("IllegalStateException 예외가 발생해야 한다") {
                val exception = shouldThrow<IllegalStateException> {
                    scheduleService.updateCompletionStatus(userId, scheduleId, true)
                }
                exception.message shouldBe "이미 '완료' 상태입니다."
            }
        }
    }
})

private fun createTestUser(id: Long): User {
    val user = User(
        email = "test@test.com",
        nickname = "testuser",
        provider = UserProvider.GOOGLE,
        providerId = "12345",
        role = Role.USER
    )
    user.id = id
    return user
}

private fun createTestSchedule(id: Long, user: User, isCompleted: Boolean = false): Schedule {
    val schedule = Schedule(
        user = user,
        title = "테스트 일정",
        startDate = LocalDateTime.now(),
        endDate = LocalDateTime.now().plusHours(1),
        priority = SchedulePriority.MEDIUM,
        isCompleted = isCompleted
    )
    schedule.id = id
    return schedule
}