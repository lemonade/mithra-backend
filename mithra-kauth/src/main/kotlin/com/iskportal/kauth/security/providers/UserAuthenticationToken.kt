package com.iskportal.kauth.security.providers

import com.iskportal.kauth.user.model.User
import org.springframework.security.authentication.AbstractAuthenticationToken
import java.util.*

class UserAuthenticationToken(
    user: User
): AbstractAuthenticationToken(emptySet()) {
    val userId: Long = user.userId!!
    val userUuid: UUID = user.userUuid!!

    override fun getCredentials(): Any {
        TODO("ignore TODO - doesn't need implementation")
    }

    override fun getPrincipal(): String = userId.toString()

    override fun isAuthenticated(): Boolean = true
}