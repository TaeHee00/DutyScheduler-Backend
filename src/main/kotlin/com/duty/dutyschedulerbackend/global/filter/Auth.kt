package com.duty.dutyschedulerbackend.global.filter

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
)
annotation class Auth(
    val authType: AuthType
)
