package com.duty.dutyschedulerbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class DutySchedulerBackendApplication

fun main(args: Array<String>) {
    runApplication<DutySchedulerBackendApplication>(*args)
}
