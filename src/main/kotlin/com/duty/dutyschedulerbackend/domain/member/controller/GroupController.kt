package com.duty.dutyschedulerbackend.domain.member.controller

import com.duty.dutyschedulerbackend.domain.member.dto.CreateGroupRequest
import com.duty.dutyschedulerbackend.domain.member.dto.GroupResponse
import com.duty.dutyschedulerbackend.domain.member.service.GroupService
import com.duty.dutyschedulerbackend.global.dto.Response
import com.duty.dutyschedulerbackend.global.filter.Auth
import com.duty.dutyschedulerbackend.global.filter.AuthType
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/groups")
class GroupController(
    private val groupService: GroupService,
) {
    @PostMapping
    @Auth(AuthType.MEMBER)
    fun createGroup(
        @RequestBody createGroupRequest: CreateGroupRequest,
        request: HttpServletRequest,
    ): ResponseEntity<Response<GroupResponse>> {
        val memberId = UUID.fromString(request.getAttribute("memberId") as String)

        val newGroup = groupService.createGroup(
            groupName = createGroupRequest.name,
        )

        return ResponseEntity
            .status(201)
            .body(
                Response(
                    data = GroupResponse.fromEntity(newGroup, null),
                    status = 201,
                    message = "Successfully created ${newGroup.name} group",
                )
            )
    }

    @GetMapping
    @Auth(AuthType.MEMBER)
    fun getGroupList(): ResponseEntity<Response<List<GroupResponse>>> {
        return ResponseEntity.ok(
            Response(
                data = groupService.getGroupList(),
                status = 200,
                message = "Successfully retrieved groups",
            )
        )
    }
}