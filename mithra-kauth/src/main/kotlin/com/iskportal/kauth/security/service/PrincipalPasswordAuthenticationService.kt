package com.iskportal.kauth.security.service

import com.iskportal.kauth.security.model.PrincipalPasswordAuthenticationMethod
import com.iskportal.kauth.security.providers.PrincipalPasswordAuthenticationToken
import com.iskportal.kauth.security.repository.PrincipalPasswordAuthenticationRepo
import com.iskportal.kauth.session.model.PrincipalPasswordSession
import com.iskportal.kauth.session.service.SessionService
import com.iskportal.kauth.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PrincipalPasswordAuthenticationService(
    val repo: PrincipalPasswordAuthenticationRepo,
    val passwordEncoder: PasswordEncoder,
    private val sessionService: SessionService,
    private val userService: UserService
) {


    @Autowired
    @Lazy
    lateinit var securityService: SecurityService


    fun getAuthenticationMethod(principal: String): PrincipalPasswordAuthenticationMethod? =
        repo.findByPrincipal(principal)

    fun produceAuthenticationMethodFrom(principal: String, password: String) =
        PrincipalPasswordAuthenticationMethod(principal, passwordEncoder.encode(password), userService.createNewUser())

    fun doesPasswordMatch(method: PrincipalPasswordAuthenticationMethod, password: String): Boolean =
        passwordEncoder.matches(password, method.passwordHash)


    fun authenticateWithPrincipalAndPasswordAlsoCreateSession(principal: String, password: String) {
        val result = securityService.authenticate(
            PrincipalPasswordAuthenticationToken(
                principal,
                password
            ),
            createSession = true
        )
        sessionService.activateSession(
            PrincipalPasswordSession(
                userService.getUserById(result.userId),
                getAuthenticationMethod(principal)!!
            )
        )
    }

}