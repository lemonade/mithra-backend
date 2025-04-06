package com.iskportal.kauth.security.controller

import com.iskportal.kauth.security.dto.PrincipalPasswordDto
import com.iskportal.kauth.security.service.PrincipalPasswordAuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authenticate") //You don't "login". You "authenticate". World of difference.
class AuthenticationController(
    val ppService: PrincipalPasswordAuthenticationService,
) {

    @PostMapping("/authentication-method/principal-and-password")
    fun principalAndPasswordAuthentication(@RequestBody dto: PrincipalPasswordDto) {
        ppService.authenticateWithPrincipalAndPasswordAlsoCreateSession(dto.principal, dto.password)
    }
}