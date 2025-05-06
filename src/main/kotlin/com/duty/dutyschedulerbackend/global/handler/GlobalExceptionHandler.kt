package com.duty.dutyschedulerbackend.global.handler

import com.duty.dutyschedulerbackend.global.dto.Response
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<Response<Unit>> {
        return ResponseEntity
            .status(e.errorCode.status)
            .body(
                Response(
                    status = e.errorCode.status.value(),
                    message = e.message!!,
                )
            )
    }
}