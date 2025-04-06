package com.iskportal.kauth.session

import com.iskportal.kauth.security.providers.UserAuthenticationToken
import com.iskportal.kauth.session.model.Session
import com.iskportal.kauth.session.service.SessionService
import com.iskportal.mithra.exception.NotFoundException
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Component

@Component
class SessionFilter(
    val sessionService: SessionService
) : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        val containsISKSessionCookie = request.cookies?.any { it.name == SessionService.ISK_SESSION_COOKIE_NAME } ?: false


        // none of my business
        if (!containsISKSessionCookie)
            return chain.doFilter(request, response)

        // check if security context exist
        val authentication = (request.getSession(false)
            ?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) as SecurityContext?)?.let { it.authentication as? UserAuthenticationToken }

        // no renewal is required cause authentication is still valid!
        if(authentication!= null)
            return chain.doFilter(request, response)

        // if it does...
        val cookie = request.cookies.first { it.name == SessionService.ISK_SESSION_COOKIE_NAME }
        val dto = sessionService.retrieveSessionDtoFromCookie(cookie)
        val session = try {
            sessionService.getSessionByUUID(dto.series)
        }catch (e: NotFoundException){
            response.addCookie(sessionService.unSetCookie())
            return chain.doFilter(request, response)
        }

        // check matching
        val tokenMatches = sessionService.checkDigest(cookie.value, session)
        if (!tokenMatches) {
            // CASE cookie theft - TODO: do logout all the sessions of the user and(BigRedButton)
            bigRedButton(session)
        }

        response.addCookie(sessionService.asCookie(sessionService.sessionRenewal(session)))

        // let's authenticate the user then
        sessionService.createSessionFor(UserAuthenticationToken(session.user))
        chain.doFilter(request, response)
    }

    fun bigRedButton(session: Session) {
        // TODO implement me(BigRedButton)
        // may send notification to user
        // logout all sessions
        // logs information of the attacker ...
    }
}