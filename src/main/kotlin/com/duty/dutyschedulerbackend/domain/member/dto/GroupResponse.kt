package com.duty.dutyschedulerbackend.domain.member.dto

import com.duty.dutyschedulerbackend.domain.member.entity.Group
import java.time.LocalDateTime
import java.util.*

data class GroupResponse(
    val id: UUID,
    val name: String,
    val count: Int?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(
            group: Group,
            count: Int?,
        ): GroupResponse {
            return GroupResponse(
                id = group.id!!,
                name = group.name,
                count = count,
                createdAt = group.createdAt!!,
            )
        }
    }
}