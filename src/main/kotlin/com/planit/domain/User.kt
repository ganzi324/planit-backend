package com.planit.domain

import com.planit.domain.enums.Role
import com.planit.domain.enums.UserProvider
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false, length = 50)
    var email: String,

    @Column(nullable = false, length = 30)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: UserProvider,

    @Column(nullable = false)
    val providerId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role,

    var profileImageUrl: String? = null,

) : BaseEntity() 