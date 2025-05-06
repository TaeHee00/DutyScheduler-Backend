package com.duty.dutyschedulerbackend.domain.member.dto

import java.util.UUID

data class JoinGroupRequest(
    val groupId: UUID,
    val memberId: UUID,
)