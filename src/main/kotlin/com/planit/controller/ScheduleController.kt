package com.planit.controller

import com.planit.dto.ScheduleRequest
import com.planit.dto.ScheduleResponse
import com.planit.service.ScheduleService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal

@RestController
@RequestMapping("/api/schedules")
class ScheduleController(
    private val scheduleService: ScheduleService
) {

    @PostMapping
    fun createSchedule(
        principal: Principal,
        @Valid @RequestBody request: ScheduleRequest
    ): ResponseEntity<ScheduleResponse> {
        val response = scheduleService.createSchedule(principal.name, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
} 