package com.duty.dutyschedulerbackend.global.filter

import com.duty.dutyschedulerbackend.domain.auth.JwtProvider
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.reflect.KClass

@Component
class AuthInterceptor(
    private val jwtProvider: JwtProvider,
): HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val method = request.method
        if (method.equals(HttpMethod.OPTIONS)) {
            // CORS 사전 요청 허용
            return true
        }

        if (handler !is HandlerMethod) {
            // 핸들러 메서드가 아닌 경우 기본 처리
            return true
        }

        val auth = getAnnotation(handler, Auth::class) ?: run { return true }

        when (auth.authType) {
            AuthType.ADMIN, AuthType.MEMBER -> {
                getAccessToken(request)?.let {
                    val memberId = jwtProvider.parseJwt(it)?.let { _it ->
                        _it.payload["memberId"] as String?
                    } ?: run {
                        // 재발급
                        getRefreshToken(request)?.let {
                            // 2. 있으면 재발급
                            val memberId: String = jwtProvider.parseJwt(it)?.let { _it ->
                                _it.payload["memberId"] as String?
                            } ?: run {
                                throw BusinessException(ErrorCode.EXPIRED_TOKEN)
                            }

                            val newAccessTokenCookie = Cookie(
                                "ACCESS_TOKEN",
                                URLEncoder.encode(jwtProvider.createAccessToken(UUID.fromString(memberId)), StandardCharsets.UTF_8)
                            )
                            newAccessTokenCookie.path = "/"
                            newAccessTokenCookie.isHttpOnly = true
                            newAccessTokenCookie.maxAge = JwtProvider.ACCESS_TOKEN_VALIDITY_TIME.toInt()

                            response.addCookie(newAccessTokenCookie)
                            request.setAttribute("memberId", memberId)
                            return true
                        } ?: run {
                            // 2. 없으면
                            throw BusinessException(ErrorCode.EXPIRED_TOKEN)
                        }
                    }

                    request.setAttribute("memberId", memberId)
                    return true
                } ?: run {
                    throw BusinessException(ErrorCode.EXPIRED_TOKEN)
                }
            }
        }
    }

    private fun <A : Annotation> getAnnotation(
        handlerMethod: HandlerMethod,
        annotationClass: KClass<A>
    ): A? {
        val methodAnnotation = handlerMethod.getMethodAnnotation(annotationClass.java)

        return methodAnnotation
    }

    private fun getAccessToken(request: HttpServletRequest): String? {
        request.cookies.firstOrNull {
            it.name == "ACCESS_TOKEN"
        }?.let {
            if (it.value.startsWith("Bearer+")) {
                return it.value.replace("Bearer+", "")
            } else {
                return null
            }
        } ?: run { return null }
    }

    private fun getRefreshToken(request: HttpServletRequest): String? {
        request.cookies.firstOrNull {
            it.name == "REFRESH_TOKEN"
        }?.let {
            return it.value
        } ?: run { return null }
    }

    private fun isWhiteListUrl(requestUrl: String): Boolean {
        val whiteList = arrayListOf(
            "/login",
        )

        whiteList.firstOrNull {
            it == requestUrl
        }?.let { return true }

        return false
    }
}