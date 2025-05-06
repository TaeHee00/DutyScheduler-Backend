package com.duty.dutyschedulerbackend.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
) {
    // System Error
    INVALID_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYSTEM.001"),

    // Member Error
    LOGIN_FAILED(HttpStatus.FORBIDDEN, "MEMBER.001"),

    // AUTH ERROR
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH.001"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH.002"),

    // GROUP ERROR
    DUPLICATE_GROUP(HttpStatus.CONFLICT, "GROUP.001"),
}