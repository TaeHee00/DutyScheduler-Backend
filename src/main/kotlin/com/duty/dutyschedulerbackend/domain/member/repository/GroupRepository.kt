package com.duty.dutyschedulerbackend.domain.member.repository

import com.duty.dutyschedulerbackend.domain.member.entity.Group
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GroupRepository : JpaRepository<Group, UUID> {
    fun existsByName(name: String): Boolean
}