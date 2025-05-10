package com.duty.dutyschedulerbackend.domain.member.controller

import com.duty.dutyschedulerbackend.domain.auth.JwtProvider
import com.duty.dutyschedulerbackend.domain.member.dto.EditMemberRequest
import com.duty.dutyschedulerbackend.domain.member.dto.LoginRequest
import com.duty.dutyschedulerbackend.domain.member.dto.MemberResponse
import com.duty.dutyschedulerbackend.domain.member.service.MemberService
import com.duty.dutyschedulerbackend.global.dto.Response
import com.duty.dutyschedulerbackend.global.filter.Auth
import com.duty.dutyschedulerbackend.global.filter.AuthType
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping("/login")
    fun login(
        response: HttpServletResponse,
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Response<Unit>> {
        val jwt = memberService.login(
            id = loginRequest.id,
            password = loginRequest.password,
        )
        val accessTokenCookie = Cookie(
            "ACCESS_TOKEN",
            URLEncoder.encode(jwt.accessToken, StandardCharsets.UTF_8)
        )
        accessTokenCookie.maxAge = JwtProvider.ACCESS_TOKEN_VALIDITY_TIME.toInt()
        accessTokenCookie.isHttpOnly = false
        accessTokenCookie.path = "/"

        val refreshTokenCookie = Cookie(
            "REFRESH_TOKEN",
            URLEncoder.encode(jwt.refreshToken, StandardCharsets.UTF_8)
        )
        refreshTokenCookie.maxAge = JwtProvider.REFRESH_TOKEN_VALIDITY_TIME.toInt()
        refreshTokenCookie.isHttpOnly = false
        refreshTokenCookie.path = "/"

        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)

        return ResponseEntity
            .status(200)
            .body(
                Response(
                    status = 200,
                    message = "Successfully logged in",
                )
            )
    }

    @PutMapping
    @Auth(AuthType.MEMBER)
    fun editMember(
        @RequestBody editRequest: EditMemberRequest,
    ): ResponseEntity<Response<Unit>> {
        memberService.editMember(
            memberId = editRequest.memberId!!,
            memberName = editRequest.memberName,
            groups = editRequest.groups,
        )

        return ResponseEntity.ok(
            Response(
                status = 200,
                message = "Successfully edited member",
            )
        )
    }

    @GetMapping("/list")
    @Auth(AuthType.MEMBER)
    fun getMemberList(): ResponseEntity<Response<List<MemberResponse>>> {
        return ResponseEntity.ok(
            Response(
                data = memberService.getMemberList(),
                status = 200,
                message = "Successfully found members"
            )
        )
    }

    @GetMapping("/test")
    @Auth(AuthType.MEMBER)
    fun test(
        request: HttpServletRequest,
    ): ResponseEntity<Response<Unit>> {
        request.getAttribute("memberId")?.let { memberId -> println(memberId) }
        return ResponseEntity.ok(Response(
            status = 200,
            message = "Successfully test",
        ))
    }
}