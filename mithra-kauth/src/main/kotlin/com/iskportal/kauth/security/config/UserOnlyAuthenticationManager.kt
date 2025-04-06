package com.iskportal.kauth.security.config

import com.iskportal.kauth.security.providers.PrincipalPasswordAuthenticationToken
import com.iskportal.kauth.security.providers.UserAuthenticationToken
import com.iskportal.kauth.security.service.PrincipalPasswordAuthenticationService
import com.iskportal.mithra.utils.logger
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component


@Component
class UserOnlyAuthenticationManager(
    providers: List<AuthenticationProvider>,
    val ppService: PrincipalPasswordAuthenticationService
) : ProviderManager(providers) {

    init {
        logger.info("using providers: $providers")
    }

    private fun convertPPA(auth: PrincipalPasswordAuthenticationToken): UserAuthenticationToken {
        val user =
            ppService.getAuthenticationMethod(auth.principal)?.user ?: throw BadCredentialsException("user not found")
        return UserAuthenticationToken(user)
    }

    /**
     * this method hammers down any information provided by authentication
     * in case it's saved somewhere (for instance inside a session repository) and clear any chance of
     * recovery of raw authentication information
     */
    private fun tryToConvertAuthenticationToUserAuthentication(authentication: Authentication): UserAuthenticationToken {
        if (!authentication.isAuthenticated)
            throw BadCredentialsException("Authentication is not authenticated")

        return when (authentication) {
            is PrincipalPasswordAuthenticationToken -> convertPPA(authentication)
            is UserAuthenticationToken -> authentication
            //...
            else -> throw NoWhenBranchMatchedException("Can't Normalize Authentication")
        }
    }

    override fun authenticate(authentication: Authentication): UserAuthenticationToken {
        val producedAuth = super.authenticate(authentication) // this has stuff in it!
        logger.debug("got authentication: {}", producedAuth)
        return tryToConvertAuthenticationToUserAuthentication(producedAuth)
    }

}