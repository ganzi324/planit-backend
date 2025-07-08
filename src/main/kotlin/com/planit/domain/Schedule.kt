package com.planit.domain

import com.planit.domain.enums.SchedulePriority
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "schedules")
class Schedule(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, length = 255)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priority: SchedulePriority,

    @Column(nullable = false)
    var isCompleted: Boolean = false,

    @Column(nullable = false)
    var startDate: LocalDateTime,

    @Column(nullable = false)
    var endDate: LocalDateTime,

    var alarmOffsetMinutes: Int? = null,

) : BaseEntity() {
    init {
        require(startDate.isBefore(endDate)) { "종료일은 시작일보다 이전일 수 없습니다." }
    }

    fun complete() {
        if (this.isCompleted) {
            throw IllegalStateException("이미 완료된 일정입니다.")
        }
        this.isCompleted = true
    }

    fun uncomplete() {
        if (!this.isCompleted) {
            throw IllegalStateException("아직 완료되지 않은 일정입니다.")
        }
        this.isCompleted = false
    }

    fun updateCompletion(completed: Boolean) {
        if (this.isCompleted == completed) {
            throw IllegalStateException("이미 '${if (completed) "완료" else "미완료"}' 상태입니다.")
        }
        this.isCompleted = completed
    }
} 