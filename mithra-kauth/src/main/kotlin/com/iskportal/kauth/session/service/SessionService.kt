package com.iskportal.kauth.session.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.iskportal.kauth.security.providers.UserAuthenticationToken
import com.iskportal.kauth.session.dto.SessionCookieInternalDto
import com.iskportal.kauth.session.model.Session
import com.iskportal.kauth.session.repository.SessionRepo
import com.iskportal.kauth.user.model.User
import com.iskportal.mithra.exception.NotFoundException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Duration
import java.time.Instant
import java.util.*

fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)

typealias SpringSession = org.springframework.session.Session

@Service
class SessionService(
    val sessionRepo: SessionRepo,
    val objectMapper: ObjectMapper,
    @Value("\${server.servlet.session.cookie.domain}")
    val domain: String
) {
    @Autowired
    lateinit var req: HttpServletRequest

    @Autowired
    lateinit var res: HttpServletResponse


    fun createSessionFor(authentication: UserAuthenticationToken) {
        val ctx = SecurityContextHolder.createEmptyContext()
        ctx.authentication = authentication
        SecurityContextHolder.setContext(ctx)
        val session = req.getSession(true)
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, ctx)
    }


    fun retrieveSessionDtoFromCookie(cookie: Cookie): SessionCookieInternalDto =
        objectMapper.readValue(Base64.getDecoder().decode(cookie.value), SessionCookieInternalDto::class.java)

    /**
     * AKA. LogOut
     */
    fun deActiveCurrentSession() {
        val sessionCookieInternalDto = req
            .cookies
            ?.filter { it.name == ISK_SESSION_COOKIE_NAME }
            ?.map { retrieveSessionDtoFromCookie(it) }
            ?.firstOrNull()


        // remove this from repo using series

        if (sessionCookieInternalDto != null) {
            removeSession(sessionCookieInternalDto.series)
        }

        // unset this cookie from client
        res.addCookie(unSetCookie())
    }


    fun removeSession(sessionUUID: UUID) {
        sessionRepo.deleteBySessionUUID(sessionUUID)
    }


    fun activateSession(session: Session) {
        sessionRepo.save(session)
        res.addCookie(asCookie(session))
    }

    fun checkDigest(content: String, session: Session): Boolean =
        content == serializeSession(session)


    private fun serializeSession(session: Session): String {
        val cookieContent = SessionCookieInternalDto(
            session.sessionUUID,
            MessageDigest.getInstance("SHA-256")
                .digest("${session.getSessionInvariantInformation()}:${session.token}".toByteArray())
        )

        return objectMapper.writeValueAsBytes(cookieContent).toBase64()
    }


    fun asCookie(session: Session) =
        Cookie(ISK_SESSION_COOKIE_NAME, serializeSession(session))
            .apply {
                this.isHttpOnly = true
                this.path = "/"
                this.secure = true
                this.maxAge = Duration.ofDays(6 * 30).toSeconds().toInt()
                this.domain = this@SessionService.domain
            }

    fun unSetCookie() =
        Cookie(ISK_SESSION_COOKIE_NAME, "").apply {
            this.isHttpOnly = true
            this.path = "/"
            this.maxAge = 0
        }

    fun getSessionByUUID(sessionUUID: UUID) =
        sessionRepo.findBySessionUUID(sessionUUID) ?: throw NotFoundException("no such session")

    fun sessionRenewal(session: Session): Session {
        session.token = UUID.randomUUID()
        session.tokenLastUsed = Instant.now()
        return sessionRepo.save(session)
    }

    fun getAllISKSessionsOfUser(currentUser: User): MutableSet<Session> {

        return currentUser.sessions
    }

    fun getCurrentUserISKSession(): Session? =
        req.cookies?.firstOrNull() { it.name == ISK_SESSION_COOKIE_NAME }?.let {
            getSessionByUUID(retrieveSessionDtoFromCookie(it).series)
        }


    @Autowired
    lateinit var springSessionRepo: FindByIndexNameSessionRepository<out SpringSession>


    fun getAllSpringSessions(user: User): MutableMap<String, out SpringSession> =
        springSessionRepo.findByPrincipalName(user.userId!!.toString())

    fun getCurrentSpringSession(): SpringSession? =
        req.cookies?.firstOrNull() { it.name == "SESSION" }?.let {
            springSessionRepo.findById(
                Base64.getDecoder().decode(it.value.toByteArray()).decodeToString()
            )
        }


    companion object {
        const val ISK_SESSION_COOKIE_NAME = "_isks"
    }

}