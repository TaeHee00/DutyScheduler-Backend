package com.duty.dutyschedulerbackend.domain.member.repository

import com.duty.dutyschedulerbackend.domain.member.entity.Member
import com.duty.dutyschedulerbackend.domain.member.entity.MemberGroup
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberGroupRepository : JpaRepository<MemberGroup, UUID> {
    fun countByGroup_Id(id: UUID): Int
    fun existsByMember_IdAndGroup_Id(memberId: UUID, groupId: UUID): Boolean
    fun deleteByMember_IdAndGroup_Id(memberId: UUID, groupId: UUID)


    fun findAllByMember_Id(memberId: UUID): List<MemberGroup>
}