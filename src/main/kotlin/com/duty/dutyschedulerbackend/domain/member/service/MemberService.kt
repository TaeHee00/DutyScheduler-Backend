package com.duty.dutyschedulerbackend.domain.member.service

import com.duty.dutyschedulerbackend.domain.auth.JwtProvider
import com.duty.dutyschedulerbackend.domain.auth.entity.Jwt
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(
    private val jwtProvider: JwtProvider,
) {

    fun login(
        id: String,
        password: String,
    ): Jwt {
        if (!(id == "admin" && password == "admin")) {
            throw BusinessException(ErrorCode.LOGIN_FAILED)
        }
        return jwtProvider.createJwt(UUID.randomUUID())
    }

}