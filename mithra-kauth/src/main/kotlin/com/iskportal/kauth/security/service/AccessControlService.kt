package com.iskportal.kauth.security.service

import com.iskportal.kauth.user.service.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccessControlService(
    val securityService: SecurityService,
    val userService: UserService,
    val registrationService: RegistrationService
) {
    fun canUserRegisterWithPrincipal(principal: String):Boolean =
        registrationService.isThisPrincipalExist(principal)
}