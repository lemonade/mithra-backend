package com.iskportal.kauth.security.providers

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class UserAuthenticationProvider: AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication is UserAuthenticationToken)
            return authentication
        else throw BadCredentialsException("Invalid Authentication, can only validate ${UserAuthenticationToken::class.java.canonicalName}")
    }

    override fun supports(authentication: Class<*>?): Boolean =
        UserAuthenticationToken::class.java.isAssignableFrom(authentication)

}