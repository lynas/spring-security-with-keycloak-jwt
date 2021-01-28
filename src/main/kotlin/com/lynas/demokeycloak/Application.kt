package com.lynas.demokeycloak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@SpringBootApplication
class Application



fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


@RestController
class DemoController{

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/demo")
    suspend fun demo() : String = "demo"

//    @GetMapping("/auth/complete")
//    fun complete() = "demo"
}

