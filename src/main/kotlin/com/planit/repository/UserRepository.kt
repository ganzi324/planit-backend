package com.planit.repository

import com.planit.domain.User
import com.planit.domain.enums.UserProvider
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByProviderAndProviderId(provider: UserProvider, providerId: String): User?
} 