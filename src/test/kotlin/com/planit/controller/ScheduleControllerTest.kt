package com.planit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.planit.config.TestSecurityConfig
import com.planit.dto.ScheduleRequest
import com.planit.dto.ScheduleResponse
import com.planit.domain.enums.SchedulePriority
import com.planit.service.ScheduleService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(ScheduleController::class)
@Import(TestSecurityConfig::class)
@ContextConfiguration(classes = [ScheduleControllerTest.TestConfig::class])
class ScheduleControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val scheduleService: ScheduleService
) : BehaviorSpec({

    Given("유효한 일정 생성 요청이 들어올 때") {
        val scheduleRequest = ScheduleRequest(
            title = "새로운 회의",
            startDate = LocalDateTime.of(2024, 8, 15, 14, 0),
            endDate = LocalDateTime.of(2024, 8, 15, 15, 0),
            priority = SchedulePriority.HIGH,
            alarmOffsetMinutes = 30
        )
        
        val expectedResponse = ScheduleResponse(
            id = 1L,
            title = "새로운 회의",
            startDate = LocalDateTime.of(2024, 8, 15, 14, 0),
            endDate = LocalDateTime.of(2024, 8, 15, 15, 0),
            priority = SchedulePriority.HIGH,
            isCompleted = false,
            alarmOffsetMinutes = 30
        )

        every { scheduleService.createSchedule(any(), any()) } returns expectedResponse

        When("POST /api/schedules로 요청을 보내면") {
            Then("201 Created 상태코드와 생성된 일정 정보를 반환해야 한다") {
                mockMvc.perform(
                    post("/api/schedules")
                        .with(user("test@example.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest))
                )
                    .andExpect(status().isCreated)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("새로운 회의"))
                    .andExpect(jsonPath("$.priority").value("HIGH"))
                    .andExpect(jsonPath("$.isCompleted").value(false))
            }
        }
    }

    Given("잘못된 일정 생성 요청이 들어올 때") {
        When("제목이 없는 요청을 보내면") {
            val invalidRequest = ScheduleRequest(
                title = "",
                startDate = LocalDateTime.of(2024, 8, 15, 14, 0),
                endDate = LocalDateTime.of(2024, 8, 15, 15, 0),
                priority = SchedulePriority.HIGH,
                alarmOffsetMinutes = 30
            )

            Then("400 Bad Request 상태코드를 반환해야 한다") {
                mockMvc.perform(
                    post("/api/schedules")
                        .with(user("test@example.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                    .andExpect(status().isBadRequest)
            }
        }

        When("종료시간이 시작시간보다 빠른 요청을 보내면") {
            val invalidRequest = ScheduleRequest(
                title = "잘못된 일정",
                startDate = LocalDateTime.of(2024, 8, 15, 15, 0),
                endDate = LocalDateTime.of(2024, 8, 15, 14, 0), // 시작시간보다 빠름
                priority = SchedulePriority.HIGH,
                alarmOffsetMinutes = 30
            )

            Then("400 Bad Request 상태코드를 반환해야 한다") {
                mockMvc.perform(
                    post("/api/schedules")
                        .with(user("test@example.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                    .andExpect(status().isBadRequest)
            }
        }
    }

    Given("인증되지 않은 사용자가 요청할 때") {
        When("JWT 토큰 없이 요청을 보내면") {
            val scheduleRequest = ScheduleRequest(
                title = "새로운 회의",
                startDate = LocalDateTime.of(2024, 8, 15, 14, 0),
                endDate = LocalDateTime.of(2024, 8, 15, 15, 0),
                priority = SchedulePriority.HIGH,
                alarmOffsetMinutes = 30
            )

            Then("401 Unauthorized 상태코드를 반환해야 한다") {
                mockMvc.perform(
                    post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest))
                )
                    .andExpect(status().isUnauthorized)
            }
        }
    }
}) {
    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun scheduleService(): ScheduleService = mockk()
    }
} 