package com.duty.dutyschedulerbackend.domain.member.entity

import com.duty.dutyschedulerbackend.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction

@Entity
@Table
@SQLRestriction("deleted_at IS NULL")
class Member(
    @Column(nullable = false)
    var name: String,
) : BaseEntity() {
}