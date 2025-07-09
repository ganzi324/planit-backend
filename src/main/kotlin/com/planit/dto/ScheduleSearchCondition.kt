package com.planit.dto

import com.planit.domain.enums.SchedulePriority

data class ScheduleSearchCondition(
    val year: Int?,
    val month: Int?,
    val priority: SchedulePriority?,
    val isCompleted: Boolean?
) 