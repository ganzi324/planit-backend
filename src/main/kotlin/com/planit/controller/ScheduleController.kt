package com.planit.controller

import com.planit.dto.ScheduleRequest
import com.planit.dto.ScheduleResponse
import com.planit.service.ScheduleService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/schedules")
class ScheduleController(
    private val scheduleService: ScheduleService
) {

    @PostMapping
    fun createSchedule(
        @AuthenticationPrincipal userId: Long,
        @Valid @RequestBody request: ScheduleRequest
    ): ResponseEntity<ScheduleResponse> {
        val schedule = scheduleService.createSchedule(userId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule)
    }

    @GetMapping
    fun getSchedules(
        @AuthenticationPrincipal userId: Long,
        @RequestParam(required = false) year: Int?,
        @RequestParam(required = false) month: Int?,
        @PageableDefault(size = 10, sort = ["startDate"]) pageable: Pageable
    ): ResponseEntity<Page<ScheduleResponse>> {
        val schedules = scheduleService.getSchedules(userId, year, month, pageable)
        return ResponseEntity.ok(schedules)
    }

    @PutMapping("/{scheduleId}")
    fun updateSchedule(
        @AuthenticationPrincipal userId: Long,
        @PathVariable scheduleId: Long,
        @Valid @RequestBody request: ScheduleRequest
    ): ResponseEntity<ScheduleResponse> {
        val updatedSchedule = scheduleService.updateSchedule(userId, scheduleId, request)
        return ResponseEntity.ok(updatedSchedule)
    }

    @DeleteMapping("/{scheduleId}")
    fun deleteSchedule(
        @AuthenticationPrincipal userId: Long,
        @PathVariable scheduleId: Long
    ): ResponseEntity<Unit> {
        scheduleService.deleteSchedule(userId, scheduleId)
        return ResponseEntity.noContent().build()
    }
} 