package com.planit.repository

import com.planit.domain.Schedule
import com.planit.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<Schedule, Long> {
    fun findAllByUser(user: User): List<Schedule>
} 