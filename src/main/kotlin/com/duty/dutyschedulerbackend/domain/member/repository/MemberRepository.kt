package com.duty.dutyschedulerbackend.domain.member.repository

import com.duty.dutyschedulerbackend.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberRepository : JpaRepository<Member, UUID> {
}