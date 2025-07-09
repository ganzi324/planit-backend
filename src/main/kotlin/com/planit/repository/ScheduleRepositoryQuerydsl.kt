package com.planit.repository

import com.planit.domain.Schedule
import com.planit.dto.ScheduleSearchCondition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ScheduleRepositoryQuerydsl {
    fun search(userId: Long, condition: ScheduleSearchCondition, pageable: Pageable): Page<Schedule>
} 