package com.duty.dutyschedulerbackend.global.exception

import com.duty.dutyschedulerbackend.global.converter.MessageConverter

class BusinessException(
    val errorCode: ErrorCode,
    vararg args: Any?,
    messageConverter: MessageConverter = MessageConverter()
) : RuntimeException(messageConverter.getMessage(errorCode.code, *args))