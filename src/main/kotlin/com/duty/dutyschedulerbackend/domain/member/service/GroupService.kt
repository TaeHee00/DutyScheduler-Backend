package com.duty.dutyschedulerbackend.domain.member.service

import com.duty.dutyschedulerbackend.domain.member.dto.GroupResponse
import com.duty.dutyschedulerbackend.domain.member.entity.Group
import com.duty.dutyschedulerbackend.domain.member.repository.GroupRepository
import com.duty.dutyschedulerbackend.domain.member.repository.MemberRepository
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import org.springframework.stereotype.Service

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val memberRepository: MemberRepository,
) {

    fun createGroup(
        groupName: String,
    ): Group {
        // 그룹명 중복 체크
        val hasGroup = groupRepository.existsByName(groupName)

        if (hasGroup) {
            // 이미 같은 이름을 가진 그룹이 있을 경우
            throw BusinessException(ErrorCode.DUPLICATE_GROUP)
        }

        val newGroup = groupRepository.save(
            Group(
                name = groupName,
            )
        )

        return newGroup
    }

    fun getGroupList(): List<GroupResponse> {
        val groupList = groupRepository.findAll()

        return groupList.map { group ->
            GroupResponse.fromEntity(
                group = group,
                count = memberRepository.countAllByGroup_Id(group.id!!)
            )
        }
    }
}