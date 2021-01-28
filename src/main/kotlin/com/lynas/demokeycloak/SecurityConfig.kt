package com.lynas.demokeycloak

import com.google.gson.Gson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.registration.ClientRegistrations
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun reactiveJwtDecoder() = NimbusReactiveJwtDecoder {
        val claimsSet = it.jwtClaimsSet
        println(Gson().toJson(claimsSet))
        Mono.just(claimsSet)
    }

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain = http.apply {
        headers().frameOptions().disable()
            .and().csrf().disable()
            .cors().disable()
            .authorizeExchange()
            .pathMatchers("/v/*").permitAll()
            .anyExchange().authenticated()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(CustomJwtAuthenticationConverter())
    }.build()

    @Bean
    fun clientRegistration(): ReactiveClientRegistrationRepository =
        InMemoryReactiveClientRegistrationRepository(
            ClientRegistrations.fromOidcIssuerLocation("http://localhost:8080/auth/realms/demo")
                .clientId("app-server")
                .clientSecret("1d6f218d-54a2-4ed3-8cf1-5ad8f3a6df80")
                .build()
        )

}
