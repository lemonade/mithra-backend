package com.iskportal.kauth.security.repository

import com.iskportal.kauth.security.model.PrincipalPasswordAuthenticationMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PrincipalPasswordAuthenticationRepo: JpaRepository<PrincipalPasswordAuthenticationMethod, Long> {
    fun findByPrincipal(principal: String): PrincipalPasswordAuthenticationMethod?
}