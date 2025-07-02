package com.planit.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * 시작일과 종료일의 유효성을 검증하는 어노테이션
 * 시작일이 종료일보다 이전이어야 함을 검증합니다.
 * 
 * @param startField 시작일 필드명 (기본값: "startDate")
 * @param endField 종료일 필드명 (기본값: "endDate")
 * @param message 검증 실패 시 메시지
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateRangeValidator::class])
annotation class DateRange(
    val startField: String = "startDate",
    val endField: String = "endDate",
    val message: String = "시작일은 종료일보다 이전이어야 합니다",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class DateRangeValidator : ConstraintValidator<DateRange, Any> {
    
    private lateinit var startField: String
    private lateinit var endField: String
    
    override fun initialize(annotation: DateRange) {
        startField = annotation.startField
        endField = annotation.endField
    }
    
    override fun isValid(obj: Any?, context: ConstraintValidatorContext?): Boolean {
        if (obj == null) return true
        
        try {
            val kClass = obj::class
            
            // 리플렉션을 사용하여 필드 값을 가져옴
            val startProperty = kClass.memberProperties.find { it.name == startField }
            val endProperty = kClass.memberProperties.find { it.name == endField }
            
            if (startProperty == null || endProperty == null) {
                // 필드가 존재하지 않으면 검증을 통과시킴 (다른 검증에서 처리)
                return true
            }
            
            // 필드 접근 허용
            startProperty.isAccessible = true
            endProperty.isAccessible = true
            
            val startValue = startProperty.call(obj) as? LocalDateTime
            val endValue = endProperty.call(obj) as? LocalDateTime
            
            // 둘 중 하나라도 null이면 검증을 통과시킴 (@NotNull 등 다른 검증에서 처리)
            if (startValue == null || endValue == null) {
                return true
            }
            
            return startValue.isBefore(endValue)
            
        } catch (e: Exception) {
            // 리플렉션 오류 발생 시 검증을 통과시킴
            return true
        }
    }
} 