package com.duty.dutyschedulerbackend.domain.member.service

import com.duty.dutyschedulerbackend.domain.auth.JwtProvider
import com.duty.dutyschedulerbackend.domain.auth.entity.Jwt
import com.duty.dutyschedulerbackend.domain.member.dto.CheckGroup
import com.duty.dutyschedulerbackend.domain.member.dto.MemberResponse
import com.duty.dutyschedulerbackend.domain.member.entity.Member
import com.duty.dutyschedulerbackend.domain.member.repository.MemberRepository
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberService(
    private val jwtProvider: JwtProvider,
    private val memberRepository: MemberRepository,
    private val groupService: GroupService,
) {

    @Transactional(readOnly = true)
    fun login(
        id: String,
        password: String,
    ): Jwt {
        if (!(id == "admin" && password == "admin")) {
            throw BusinessException(ErrorCode.LOGIN_FAILED)
        }
        return jwtProvider.createJwt(UUID.randomUUID())
    }

    @Transactional(readOnly = true)
    fun getMemberList(): List<MemberResponse> {
        val memberList = memberRepository.findAll()
        return memberList.map { member ->
            MemberResponse.fromEntity(
                member = member,
                groups = groupService.getMemberGroupList(member.id!!),
            )
        }
    }

    @Transactional
    fun editMember(
        memberId: UUID,
        memberName: String,
        groups: List<CheckGroup>,
    ) {
        memberRepository.findByIdOrNull(memberId)?.let {
            it.name = memberName
            groups.forEach { group ->
                run {
                    // 가입 된 그룹
                    if (groupService.existsJoinGroup(
                            memberId = it.id!!,
                            groupId = group.id,
                        )
                    ) {
                        // 가입되어있는데 삭제 요청
                        if (!group.isChecked) {
                            groupService.deleteMemberGroup(
                                memberId = memberId,
                                groupId = group.id,
                            )
                        }
                    } else {
                        // 가입 안된 그룹
                        if (group.isChecked) {
                            // 가입 요청
                            groupService.joinGroup(
                                member = it,
                                groupId = group.id,
                            )
                        }
                    }
                }
            }
        }
    }

    @Transactional(readOnly = true)
    fun getRefMember(memberId: UUID): Member {
        return memberRepository.getReferenceById(memberId)
    }

}