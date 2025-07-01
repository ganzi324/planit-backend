package com.planit.auth.support

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret-key}") private val secretKey: String,
    @Value("\${jwt.expiration-hours}") private val expirationHours: Long,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createToken(userId: String, email: String, role: String): String {
        val now = Date()
        val expiration = Date(now.time + expirationHours * 3600 * 1000)

        return Jwts.builder()
            .subject(userId)
            .claim("email", email)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            getClaims(token)
            true
        } catch (e: Exception) {
            // MalformedJwtException, ExpiredJwtException 등 처리 가능
            false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val email = claims.get("email", String::class.java)
        val role = claims.get("role", String::class.java)
        val authorities = listOf(SimpleGrantedAuthority(role))

        return UsernamePasswordAuthenticationToken(email, null, authorities)
    }

    fun getEmail(token: String): String {
        return getClaims(token).get("email", String::class.java)
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
} 