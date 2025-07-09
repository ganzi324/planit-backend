package com.planit.repository

import com.planit.domain.QSchedule
import com.planit.domain.QSchedule.schedule
import com.planit.domain.Schedule
import com.planit.domain.enums.SchedulePriority
import com.planit.dto.ScheduleSearchCondition
import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ScheduleRepositoryQuerydslImpl(
    private val queryFactory: JPAQueryFactory
) : ScheduleRepositoryQuerydsl {

    override fun search(userId: Long, condition: ScheduleSearchCondition, pageable: Pageable): Page<Schedule> {
        val builder = BooleanBuilder()

        builder.and(schedule.user.id.eq(userId))

        condition.year?.let { builder.and(schedule.startDate.year().eq(it)) }
        condition.month?.let { builder.and(schedule.startDate.month().eq(it)) }
        condition.priority?.let { builder.and(schedule.priority.eq(it)) }
        condition.isCompleted?.let { builder.and(schedule.isCompleted.eq(it)) }

        val query = queryFactory
            .selectFrom(schedule)
            .where(builder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory.select(schedule.count()).from(schedule).where(builder).fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }
} 