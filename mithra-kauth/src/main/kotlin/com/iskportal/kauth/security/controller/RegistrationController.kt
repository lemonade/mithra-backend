package com.iskportal.kauth.security.controller

import com.iskportal.kauth.security.dto.PrincipalPasswordDto
import com.iskportal.kauth.security.service.AccessControlService
import com.iskportal.kauth.security.service.RegistrationService
import com.iskportal.kauth.user.model.User
import com.iskportal.mithra.exception.MalformedData
import com.iskportal.mithra.exception.UnAuthorizedException
import com.iskportal.mithra.utils.otherwiseThrow
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/register")
class RegistrationController(
    val ac: AccessControlService,
    val registrationService: RegistrationService,
    private val authenticationController: AuthenticationController,
) {


    private fun handlePrincipalAndPasswordDto(
        dto: PrincipalPasswordDto,
        handler: (PrincipalPasswordDto) -> User
    ): User {
        ac.canUserRegisterWithPrincipal(dto.principal) otherwiseThrow UnAuthorizedException("duplicate principal")
        return handler(dto)
            .also { authenticationController.principalAndPasswordAuthentication(dto) }
    }


    @PostMapping("registration-method/principal-and-password")
    fun registerNewUserWithPrincipalAndPassword(@RequestBody dto: PrincipalPasswordDto): User =
        handlePrincipalAndPasswordDto(dto) { registrationService.saveNewUserWithPrincipalAndPassword(it.principal, it.password) }


    val emailRegex = Regex("^[\\w\\W-\\.]+@([\\w\\W-]+\\.)+[\\w-]{2,4}$")
    @PostMapping("registration-method/email-and-password")
    fun registerNewUserWithEmailAndPassword(@RequestBody dto: PrincipalPasswordDto): User {
        emailRegex.matches(dto.principal) otherwiseThrow MalformedData("email is not valid: ${dto.principal}")
        return handlePrincipalAndPasswordDto(dto) {
            registrationService.saveNewUserWithEmailAndPassword(dto.principal, dto.password)
        }
    }


    val phoneRegex = Regex("/0{0,1}9\\d{9}")
    @PostMapping("registration-method/phone-and-password")
    fun registerNewUserWithPhoneAndPassword(@RequestBody dto: PrincipalPasswordDto): User {
        phoneRegex.matches(dto.principal) otherwiseThrow MalformedData("phone is not valid: ${dto.principal}")
        return handlePrincipalAndPasswordDto(dto) {
            registrationService.saveNewUserWithPhoneAndPassword(dto.principal, dto.password)
        }
    }
}