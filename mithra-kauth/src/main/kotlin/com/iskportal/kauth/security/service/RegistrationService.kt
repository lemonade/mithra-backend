package com.iskportal.kauth.security.service

import com.iskportal.kauth.security.model.EmailPasswordAuthenticationMethod
import com.iskportal.kauth.security.model.PhonePasswordAuthenticationMethod
import com.iskportal.kauth.security.model.PrincipalPasswordAuthenticationMethod
import com.iskportal.kauth.security.repository.PrincipalPasswordAuthenticationRepo
import com.iskportal.kauth.user.model.User
import com.iskportal.kauth.user.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegistrationService(
    val ppRepo: PrincipalPasswordAuthenticationRepo,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    fun saveNewUserWithPhoneAndPassword(phone: String, password: String): User =
        userService.registerUserWithAuthenticationMethod(
            PhonePasswordAuthenticationMethod(
                phone = phone,
                passwordHash = passwordEncoder.encode(password),
                User()
            )
        )


    fun saveNewUserWithEmailAndPassword(email: String, password: String) =
        userService.registerUserWithAuthenticationMethod(
            EmailPasswordAuthenticationMethod(
                email = email,
                passwordHash = passwordEncoder.encode(password),
                User()
            )
        )


    fun saveNewUserWithPrincipalAndPassword(principal: String, password: String): User =
        userService.registerUserWithAuthenticationMethod(
            PrincipalPasswordAuthenticationMethod(
                principal = principal,
                passwordHash = passwordEncoder.encode(password),
                User()
            )
        )


    fun isThisPrincipalExist(principal: String): Boolean {
        return ppRepo.findByPrincipal(principal) == null
    }

}