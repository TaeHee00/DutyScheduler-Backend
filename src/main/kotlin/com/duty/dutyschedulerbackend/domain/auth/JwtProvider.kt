package com.duty.dutyschedulerbackend.domain.auth

import com.duty.dutyschedulerbackend.domain.auth.entity.Jwt
import com.duty.dutyschedulerbackend.global.exception.BusinessException
import com.duty.dutyschedulerbackend.global.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret_key}") private val key: String,
) {
    init {
        keyBase64Encoded = Base64.getEncoder().encodeToString(key.toByteArray())
        signingKey = Keys.hmacShaKeyFor(keyBase64Encoded.toByteArray())
    }

    companion object {
//        const val ACCESS_TOKEN_VALIDITY_TIME: Long = (1000 * 60 * 30).toLong()
        const val ACCESS_TOKEN_VALIDITY_TIME: Long = (1000 * 60).toLong()
        const val REFRESH_TOKEN_VALIDITY_TIME: Long = (1000 * 60 * 60 * 24 * 14).toLong()
        const val ACCESS_TOKEN_PREFIX: String = "Bearer "
        const val ACCESS_TOKEN_HEADER: String = "Authorization"
        const val REFRESH_HEADER_PREFIX: String = "RefreshToken "
        lateinit var signingKey: SecretKey
        lateinit var keyBase64Encoded: String
    }

    fun createJwt(memberId: UUID): Jwt {
        return Jwt(
            accessToken = createAccessToken(memberId),
            refreshToken = createRefreshToken(memberId),
        )
    }

    fun createAccessToken(memberId: UUID): String {
        val claims: MutableMap<String, Any> = HashMap()

        claims["memberId"] = memberId

        val expiration = Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_TIME)
        val accessToken: String = ACCESS_TOKEN_PREFIX + Jwts.builder()
            .subject(memberId.toString())
            .claims(claims)
            .expiration(expiration)
            .signWith(signingKey)
            .compact()

        return accessToken
    }

    fun createRefreshToken(memberId: UUID): String {
        val expiration = Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_TIME)
        val refreshToken: String = Jwts.builder()
            .subject(memberId.toString())
            .claim("memberId", memberId.toString())
            .expiration(expiration)
            .signWith(signingKey)
            .compact()

        // Redis 저장
        return refreshToken
    }

    fun parseJwt(token: String): Jws<Claims>? {
        val claimsJws: Jws<Claims>
        try {
            claimsJws = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)

        } catch (eje: ExpiredJwtException) {
            // 만료 재발급
//            throw BusinessException(ErrorCode.EXPIRED_TOKEN)
            return null
        } catch (je: JwtException) {
            throw BusinessException(ErrorCode.INVALID_TOKEN)
        }

        return claimsJws
    }
}