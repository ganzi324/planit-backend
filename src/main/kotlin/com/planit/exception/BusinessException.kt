package com.planit.exception

import org.springframework.http.HttpStatus

/**
 * 비즈니스 로직 관련 예외의 기본 클래스
 */
abstract class BusinessException(
    message: String,
    val errorCode: String,
    val httpStatus: HttpStatus
) : RuntimeException(message)

/**
 * 404 Not Found 관련 예외
 */
abstract class NotFoundException(
    message: String,
    errorCode: String = "NOT_FOUND"
) : BusinessException(message, errorCode, HttpStatus.NOT_FOUND)

/**
 * 400 Bad Request 관련 예외
 */
abstract class BadRequestException(
    message: String,
    errorCode: String = "BAD_REQUEST"
) : BusinessException(message, errorCode, HttpStatus.BAD_REQUEST)

/**
 * 403 Forbidden 관련 예외
 */
abstract class ForbiddenException(
    message: String,
    errorCode: String = "FORBIDDEN"
) : BusinessException(message, errorCode, HttpStatus.FORBIDDEN)

/**
 * 401 Unauthorized 관련 예외
 */
abstract class UnauthorizedException(
    message: String,
    errorCode: String = "UNAUTHORIZED"
) : BusinessException(message, errorCode, HttpStatus.UNAUTHORIZED)

/**
 * 409 Conflict 관련 예외
 */
abstract class ConflictException(
    message: String,
    errorCode: String = "CONFLICT"
) : BusinessException(message, errorCode, HttpStatus.CONFLICT)

/**
 * 사용자를 찾을 수 없는 경우
 */
class UserNotFoundException(userId: Long) : NotFoundException(
    "User not found with id: $userId",
    "USER_NOT_FOUND"
)

/**
 * 이메일로 사용자를 찾을 수 없는 경우
 */
class UserNotFoundByEmailException(email: String) : NotFoundException(
    "User not found with email: $email",
    "USER_NOT_FOUND_BY_EMAIL"
)

/**
 * 일정을 찾을 수 없는 경우
 */
class ScheduleNotFoundException(scheduleId: Long) : NotFoundException(
    "Schedule not found with id: $scheduleId",
    "SCHEDULE_NOT_FOUND"
)

/**
 * 잘못된 일정 상태 변경 시도
 */
class InvalidScheduleStateException(message: String) : BadRequestException(
    message,
    "INVALID_SCHEDULE_STATE"
)

/**
 * 권한이 없는 접근 시도
 */
class UnauthorizedAccessException(message: String) : ForbiddenException(
    message,
    "UNAUTHORIZED_ACCESS"
)

/**
 * 잘못된 토큰
 */
class InvalidTokenException(message: String) : UnauthorizedException(
    message,
    "INVALID_TOKEN"
)

/**
 * 중복된 리소스 생성 시도
 */
class DuplicateResourceException(message: String) : ConflictException(
    message,
    "DUPLICATE_RESOURCE"
) 