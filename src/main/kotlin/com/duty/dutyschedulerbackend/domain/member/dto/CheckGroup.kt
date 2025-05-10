package com.duty.dutyschedulerbackend.domain.member.dto

import java.util.UUID

data class CheckGroup(
    val id: UUID,
    val name: String,
    val isChecked: Boolean,
)