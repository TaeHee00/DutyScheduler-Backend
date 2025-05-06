package com.duty.dutyschedulerbackend.domain.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:application-jwt.properties")
class JwtProperties(
    @Value("\${jwt.issuer}")
    private val issuer: String,
    @Value("\${jwt.secret_key}")
    private val secretKey: String,
    @Value("\${jwt.sign_in.password.salt}")
    private val passwordSalt: String,
)