package com.duty.dutyschedulerbackend.global.converter

import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import org.springframework.context.NoSuchMessageException
import java.util.*

class MessageConverter {
    private val messageSource: YamlMessageSource = YamlMessageSource()

    fun getMessage(
        code: String,
        vararg args: Any?
    ): String {
        return try {
            messageSource.getMessage(code, args, Locale.KOREAN)
        } catch (e: NoSuchMessageException) {
            messageSource.getMessage(ErrorCode.INVALID_ERROR.code, args, Locale.KOREAN)
        }
    }
}