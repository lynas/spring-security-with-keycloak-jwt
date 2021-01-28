package com.lynas.demokeycloak

import com.google.gson.Gson
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import reactor.core.publisher.Mono

class CustomJwtAuthenticationConverter : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()

    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken> {
        println(Gson().toJson(source))
        val scopes = jwtGrantedAuthoritiesConverter.convert(source)
        val authorities = source.getClaimAsString("authorities")?.split(",")?.map { SimpleGrantedAuthority(it) }
        return Mono.just(JwtAuthenticationToken(source, scopes.orEmpty() + authorities.orEmpty()))
    }
}