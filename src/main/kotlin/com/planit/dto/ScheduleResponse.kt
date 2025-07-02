package com.planit.dto

import com.planit.domain.Schedule
import com.planit.domain.enums.SchedulePriority
import java.time.LocalDateTime

data class ScheduleResponse(
    val id: Long,
    val title: String,
    val description: String? = null,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val priority: SchedulePriority,
    val isCompleted: Boolean,
    val alarmOffsetMinutes: Int? = null
) {
    companion object {
        fun from(schedule: Schedule): ScheduleResponse {
            return ScheduleResponse(
                id = schedule.id!!,
                title = schedule.title,
                description = schedule.description,
                startDate = schedule.startDate,
                endDate = schedule.endDate,
                priority = schedule.priority,
                isCompleted = schedule.isCompleted,
                alarmOffsetMinutes = schedule.alarmOffsetMinutes
            )
        }
    }
} 