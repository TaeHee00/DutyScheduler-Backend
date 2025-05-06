package com.duty.dutyschedulerbackend.domain.member.service

import com.duty.dutyschedulerbackend.domain.member.dto.GroupResponse
import com.duty.dutyschedulerbackend.domain.member.entity.Group
import com.duty.dutyschedulerbackend.domain.member.entity.MemberGroup
import com.duty.dutyschedulerbackend.domain.member.repository.GroupRepository
import com.duty.dutyschedulerbackend.domain.member.repository.MemberGroupRepository
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val memberGroupRepository: MemberGroupRepository,
    private val memberService: MemberService,
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
                count = memberGroupRepository.countByGroup_Id(group.id!!)
            )
        }
    }

    fun joinGroup(
        memberId: UUID,
        groupId: UUID,
    ) {
        // 중복 가입 확인(한 그룹에 여러번 가입)
        if (isGroupJoined(memberId, groupId)) {
            throw BusinessException(ErrorCode.ALREADY_JOINED_GROUP)
        }

        // NOTE: 불필요 조회를 줄이기 위해 프록시로 저장
        val refMember = memberService.getRefMember(memberId)
        val refGroup = getRefGroup(groupId)

        // 반환 타입은 미정
        memberGroupRepository.save(
            MemberGroup(
                member = refMember,
                group = refGroup,
            )
        )
    }

    fun getRefGroup(groupId: UUID): Group {
        return groupRepository.getReferenceById(groupId)
    }

    private fun isGroupJoined(
        groupId: UUID,
        memberId: UUID,
    ): Boolean {
        return memberGroupRepository.existsByMember_IdAndGroup_Id(
            memberId = memberId,
            groupId = groupId,
        )
    }
}