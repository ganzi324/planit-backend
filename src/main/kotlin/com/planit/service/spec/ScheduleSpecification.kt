package com.planit.service.spec

import com.planit.domain.Schedule
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object ScheduleSpecification {

    fun hasUser(userId: Long): Specification<Schedule> {
        return Specification { root, _, builder ->
            builder.equal(root.get<Long>("user").get<Long>("id"), userId)
        }
    }

    fun isYear(year: Int?): Specification<Schedule>? {
        if (year == null) return null
        return Specification { root, _, builder ->
            val predicates = mutableListOf<Predicate>()
            val function = builder.function("YEAR", Int::class.java, root.get<Any>("startDate"))
            predicates.add(builder.equal(function, year))
            builder.and(*predicates.toTypedArray())
        }
    }

    fun isMonth(month: Int?): Specification<Schedule>? {
        if (month == null) return null
        return Specification { root, _, builder ->
            val predicates = mutableListOf<Predicate>()
            val function = builder.function("MONTH", Int::class.java, root.get<Any>("startDate"))
            predicates.add(builder.equal(function, month))
            builder.and(*predicates.toTypedArray())
        }
    }
} 