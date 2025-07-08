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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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

    val userId = 1L
    val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
    val testAuthentication = UsernamePasswordAuthenticationToken(userId, null, authorities)

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

        When("POST /api/schedules로 요청을 보내면") {
            every { scheduleService.createSchedule(userId, scheduleRequest) } returns expectedResponse
            
            Then("201 Created 상태코드와 생성된 일정 정보를 반환해야 한다") {

                mockMvc.perform(
                    post("/api/schedules")
                        .with(authentication(testAuthentication))
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
                        .with(authentication(testAuthentication))
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
                endDate = LocalDateTime.of(2024, 8, 15, 14, 0),
                priority = SchedulePriority.HIGH,
                alarmOffsetMinutes = 30
            )

            Then("400 Bad Request 상태코드를 반환해야 한다") {
                mockMvc.perform(
                    post("/api/schedules")
                        .with(authentication(testAuthentication))
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

    Given("인증된 사용자가 일정 목록 조회를 요청하면") {
        val pageNumber = 0
        val pageSize = 5
        val pageable = PageRequest.of(pageNumber, pageSize)

        val scheduleResponses = (1..pageSize).map {
            ScheduleResponse(
                id = it.toLong(),
                title = "테스트 일정 $it",
                description = "상세 설명 $it",
                startDate = LocalDateTime.now().plusDays(it.toLong()),
                endDate = LocalDateTime.now().plusDays(it.toLong()).plusHours(1),
                priority = SchedulePriority.MEDIUM,
                isCompleted = false,
                alarmOffsetMinutes = null
            )
        }
        val schedulePage = PageImpl(scheduleResponses, pageable, 10L)

        When("페이징 정보를 포함하여 GET /api/schedules를 호출하면") {
            every { scheduleService.getSchedules(userId, null, null, any()) } returns schedulePage

            Then("200 OK 상태 코드와 함께 페이징된 일정 목록이 반환된다") {

                mockMvc.perform(
                    get("/api/schedules")
                        .param("page", pageNumber.toString())
                        .param("size", pageSize.toString())
                        .with(authentication(testAuthentication))
                )
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("$.content.length()").value(pageSize))
                    .andExpect(jsonPath("$.totalElements").value(10))
                    .andExpect(jsonPath("$.content[0].title").value("테스트 일정 1"))
            }
        }

        When("연도와 월 필터링 정보를 포함하여 GET /api/schedules를 호출하면") {
            val year = 2024
            val month = 8
            every { scheduleService.getSchedules(userId, year, month, any()) } returns schedulePage

            Then("200 OK 상태 코드와 함께 필터링 및 페이징된 일정 목록이 반환된다") {
                mockMvc.perform(
                    get("/api/schedules")
                        .param("year", year.toString())
                        .param("month", month.toString())
                        .param("page", pageNumber.toString())
                        .param("size", pageSize.toString())
                        .with(authentication(testAuthentication))
                )
                    .andExpect(status().isOk)
                    .andExpect(jsonPath("$.content.length()").value(pageSize))
                    .andExpect(jsonPath("$.totalElements").value(10))
            }
        }
    }
}) {
    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun scheduleService(): ScheduleService = mockk(relaxed = true)
    }
} 