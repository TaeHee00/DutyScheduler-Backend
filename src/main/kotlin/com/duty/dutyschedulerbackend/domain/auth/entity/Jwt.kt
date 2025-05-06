package com.duty.dutyschedulerbackend.domain.auth.entity

data class Jwt(
    val accessToken: String,
    val refreshToken: String,
)
