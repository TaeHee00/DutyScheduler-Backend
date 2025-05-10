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
import java.time.LocalTime

@Entity
@Table
@SQLRestriction("deleted_at IS NULL")
class DutyTimeGroup(
    // DutyTimeGroup <- 여기에 시작 종료 시간 들어가야함
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var startTime: LocalTime,
    @Column(nullable = false)
    var endTime: LocalTime,
) : BaseEntity() {
}