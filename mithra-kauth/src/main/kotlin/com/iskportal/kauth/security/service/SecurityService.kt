package com.iskportal.kauth.security.service

import com.iskportal.kauth.security.config.UserOnlyAuthenticationManager
import com.iskportal.kauth.security.providers.UserAuthenticationToken
import com.iskportal.kauth.session.service.SessionService
import com.iskportal.kauth.user.model.User
import com.iskportal.kauth.user.service.UserService
import com.iskportal.mithra.exception.BadBehaviorException
import com.iskportal.mithra.utils.logger
import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityService(
    private val userService: UserService,
    private val sessionService: SessionService,
    val authManager: UserOnlyAuthenticationManager,
) {

    val currentUser: User?
        get() = (SecurityContextHolder.getContext().authentication as? UserAuthenticationToken?) ?.userId?.let { userService.getUserById(it) }


    fun authenticate(authentication: Authentication, createSession: Boolean): UserAuthenticationToken =
        try {
            val userAuthentication = authManager.authenticate(authentication)
            if (createSession)
                sessionService.createSessionFor(userAuthentication)
            userAuthentication
        } catch (e: AuthenticationException) {
            logger.warn("(authenticate) error: ${ExceptionUtils.getStackTrace(e)}")
            throw BadBehaviorException("credentials doesn't match")
        }

    companion object {
        val userId: Long
            get() = (SecurityContextHolder.getContext().authentication as UserAuthenticationToken).userId
    }
}