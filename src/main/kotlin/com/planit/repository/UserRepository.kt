package com.planit.repository

import com.planit.domain.User
import com.planit.domain.enums.UserProvider
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByProviderAndProviderId(provider: UserProvider, providerId: String): Optional<User>
} 