package com.planit.service

import com.planit.domain.Schedule
import com.planit.domain.enums.SchedulePriority
import com.planit.dto.ScheduleRequest
import com.planit.dto.ScheduleResponse
import com.planit.repository.ScheduleRepository
import com.planit.repository.UserRepository
import com.planit.service.spec.ScheduleSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createSchedule(userId: Long, request: ScheduleRequest): ScheduleResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw IllegalArgumentException("User not found with id: $userId")

        val schedule = Schedule(
            user = user,
            title = request.title,
            startDate = request.startDate,
            endDate = request.endDate,
            priority = request.priority,
            alarmOffsetMinutes = request.alarmOffsetMinutes
        )
        val savedSchedule = scheduleRepository.save(schedule)
        return ScheduleResponse.from(savedSchedule)
    }

    @Transactional(readOnly = true)
    fun getSchedules(userId: Long, year: Int?, month: Int?, pageable: Pageable): Page<ScheduleResponse> {
        val spec: Specification<Schedule> = Specification.where(ScheduleSpecification.hasUser(userId))
            .and(ScheduleSpecification.isYear(year))
            .and(ScheduleSpecification.isMonth(month))

        return scheduleRepository.findAll(spec, pageable)
            .map { ScheduleResponse.from(it) }
    }

    @Transactional
    fun updateSchedule(userId: Long, scheduleId: Long, request: ScheduleRequest): ScheduleResponse {
        val schedule = scheduleRepository.findByIdOrNull(scheduleId)
            ?: throw IllegalArgumentException("Schedule not found with id: $scheduleId")

        require(schedule.user.id == userId) {
            "User has no permission to update this schedule"
        }

        schedule.apply {
            this.title = request.title
            this.description = request.description
            this.startDate = request.startDate
            this.endDate = request.endDate
            this.priority = request.priority
            this.alarmOffsetMinutes = request.alarmOffsetMinutes
        }
        return ScheduleResponse.from(schedule)
    }

    @Transactional
    fun deleteSchedule(userId: Long, scheduleId: Long) {
        val schedule = scheduleRepository.findByIdOrNull(scheduleId)
            ?: throw IllegalArgumentException("Schedule not found with id: $scheduleId")

        require(schedule.user.id == userId) {
            "User has no permission to delete this schedule"
        }

        scheduleRepository.delete(schedule)
    }

    @Transactional
    fun updateCompletionStatus(userId: Long, scheduleId: Long, isCompleted: Boolean): ScheduleResponse {
        val schedule = scheduleRepository.findByIdOrNull(scheduleId)
            ?: throw IllegalArgumentException("Schedule not found with id: $scheduleId")

        require(schedule.user.id == userId) {
            "User has no permission to update this schedule"
        }

        schedule.updateCompletion(isCompleted)

        return ScheduleResponse.from(schedule)
    }
} 