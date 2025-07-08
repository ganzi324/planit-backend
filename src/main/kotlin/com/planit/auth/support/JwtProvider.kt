package com.planit.auth.support

import com.planit.domain.User
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

    fun createToken(user: User): String {
        val now = Date()
        val expiration = Date(now.time + expirationHours * 3600 * 1000)

        return Jwts.builder()
            .subject(user.id.toString())
            .claim("role", user.role.key)
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
        val userId = claims.subject.toLong()
        val role = claims.get("role", String::class.java)
        val authorities = listOf(SimpleGrantedAuthority(role))

        return UsernamePasswordAuthenticationToken(userId, null, authorities)
    }

    fun getUserId(token: String): Long {
        return getClaims(token).subject.toLong()
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
} 