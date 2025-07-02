package com.planit.service

import com.planit.domain.Schedule
import com.planit.domain.enums.SchedulePriority
import com.planit.dto.ScheduleRequest
import com.planit.dto.ScheduleResponse
import com.planit.repository.ScheduleRepository
import com.planit.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun createSchedule(email: String, request: ScheduleRequest): ScheduleResponse {
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("User not found")
        val schedule = Schedule(
            user = user,
            title = request.title,
            startDate = request.startDate,
            endDate = request.endDate,
            priority = request.priority
        )
        val savedSchedule = scheduleRepository.save(schedule)
        return ScheduleResponse.from(savedSchedule)
    }
} 