package com.duty.dutyschedulerbackend.domain.member.service

import com.duty.dutyschedulerbackend.domain.auth.JwtProvider
import com.duty.dutyschedulerbackend.domain.auth.entity.Jwt
import com.duty.dutyschedulerbackend.domain.member.entity.Member
import com.duty.dutyschedulerbackend.domain.member.repository.MemberRepository
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(
    private val jwtProvider: JwtProvider,
    private val memberRepository: MemberRepository,
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

    fun getRefMember(memberId: UUID): Member {
        return memberRepository.getReferenceById(memberId)
    }

}