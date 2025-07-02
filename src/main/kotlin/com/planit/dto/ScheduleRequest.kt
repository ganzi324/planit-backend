package com.planit.dto

import com.planit.domain.enums.SchedulePriority
import com.planit.validation.DateRange
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@DateRange(
    startField = "startDate",
    endField = "endDate",
    message = "종료일은 시작일보다 이후여야 합니다"
)
data class ScheduleRequest(
    @field:NotBlank(message = "일정 제목은 필수입니다")
    val title: String,
    
    @field:NotNull(message = "시작 날짜는 필수입니다")
    val startDate: LocalDateTime,
    
    @field:NotNull(message = "종료 날짜는 필수입니다")
    val endDate: LocalDateTime,
    
    @field:NotNull(message = "우선순위는 필수입니다")
    val priority: SchedulePriority,
    
    val alarmOffsetMinutes: Int? = null
) 