package com.iskportal.kauth.user.controller

import com.iskportal.kauth.security.service.SecurityService
import com.iskportal.kauth.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/self")
class SelfController (
    val userService: UserService,
    val securityService: SecurityService
){
    @GetMapping("is-authenticated")
    fun isAuthenticated() : Boolean =
        securityService.currentUser != null

}