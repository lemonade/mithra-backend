package com.iskportal.kauth.security.providers

import com.iskportal.kauth.security.service.PrincipalPasswordAuthenticationService
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class PrincipalPasswordAuthenticationProvider(
    val service: PrincipalPasswordAuthenticationService

) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        authentication as PrincipalPasswordAuthenticationToken
        val method = service.getAuthenticationMethod(authentication.principal)
            ?: throw BadCredentialsException("credential doesn't match")
        val passwordMatches = service.doesPasswordMatch(method, authentication.credentials)
        if (!passwordMatches)
            throw BadCredentialsException("credential doesn't match")
        authentication.isAuthenticated = true
        return authentication
    }

    override fun supports(authentication: Class<*>): Boolean =
        PrincipalPasswordAuthenticationToken::class.java.isAssignableFrom(authentication)

}

class PrincipalPasswordAuthenticationToken(
    private val principal: String,
    private val password: String,
) : AbstractAuthenticationToken(null) {
    override fun getCredentials() = password
    override fun getPrincipal() = principal
}