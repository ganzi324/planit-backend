package com.planit.repository

import com.planit.domain.Schedule
import com.planit.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<Schedule, Long>, ScheduleRepositoryQuerydsl {
    fun findAllByUser(user: User): List<Schedule>
    fun findByUser_Id(userId: Long, pageable: Pageable): Page<Schedule>
} 