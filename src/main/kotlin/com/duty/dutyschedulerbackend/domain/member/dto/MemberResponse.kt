package com.duty.dutyschedulerbackend.domain.member.dto

import com.duty.dutyschedulerbackend.domain.member.entity.Member
import java.time.LocalDateTime
import java.util.*

data class MemberResponse(
    val id: UUID,
    val name: String,
    val groups: List<GroupResponse>,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(
            member: Member,
            groups: List<GroupResponse>
        ): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                name = member.name,
                groups = groups,
                createdAt = member.createdAt!!,
            )
        }
    }
}