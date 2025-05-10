package com.duty.dutyschedulerbackend.domain.schedule.entity

import com.duty.dutyschedulerbackend.domain.member.entity.Member
import com.duty.dutyschedulerbackend.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate

@Entity
@Table
@SQLRestriction("deleted_at IS NULL")
class Schedule(
    @Column(nullable = false)
    var scheduleDate: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duty_time_group_id", nullable = false)
    var dutyTimeGroup: DutyTimeGroup,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duty_member_id", nullable = false)
    var dutyMember: Member,
) : BaseEntity() {
}