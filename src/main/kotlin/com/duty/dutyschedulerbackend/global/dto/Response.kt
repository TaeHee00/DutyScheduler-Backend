package com.duty.dutyschedulerbackend.global.dto

class Response<T>(
    var data: T? = null,
    var status: Int = 500,
    var message: String = "Server Error",
)