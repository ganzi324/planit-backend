package com.planit.dto

import jakarta.validation.constraints.NotNull

data class ScheduleCompletionRequest(
    @field:NotNull(message = "완료 여부는 필수입니다.")
    val isCompleted: Boolean
) 