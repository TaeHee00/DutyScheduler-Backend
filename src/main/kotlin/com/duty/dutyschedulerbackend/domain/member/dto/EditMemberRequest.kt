package com.duty.dutyschedulerbackend.domain.member.dto

import java.util.UUID

data class EditMemberRequest(
    val memberId: UUID,
    val memberName: String,
    val groups: List<CheckGroup>
)