package com.iskportal.kauth.session.controller

import com.iskportal.kauth.security.service.SecurityService
import com.iskportal.kauth.session.model.Session
import com.iskportal.kauth.session.service.SessionService
import com.iskportal.kauth.session.service.SpringSession
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/session")
class SessionController(
    val sessionService: SessionService,
    val securityService: SecurityService
) {

    @GetMapping("/isk-session")
    fun getISKSessionFromSession(): Session? =
        sessionService.getCurrentUserISKSession()

    @GetMapping("/isk-session/all")
    fun getAllSessions(): MutableSet<Session> =
        with(securityService.currentUser) {
            sessionService.getAllISKSessionsOfUser(this!!)
        }


    @GetMapping("/servlet-session")
    fun getSpringSessionFromSession() =
        sessionService.getCurrentSpringSession()?.serialize()

    @GetMapping("/servlet-session/all")
    fun getAllSpringSessions(): List<Map<String, Any>> {
        val currentUser = securityService.currentUser
        return sessionService
            .getAllSpringSessions(currentUser!!).map {
                it.value.serialize()
            }.toList()
    }
}


fun SpringSession.serialize() =
    mapOf(
        "sessionId" to this.id,
        "creationTime" to this.creationTime,
        "lastAccessedTime" to this.lastAccessedTime,
        "isExpired" to this.isExpired,
        "maxInactiveInterval" to this.maxInactiveInterval,
    )