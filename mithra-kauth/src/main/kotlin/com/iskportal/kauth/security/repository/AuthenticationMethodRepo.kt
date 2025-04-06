package com.iskportal.kauth.security.repository

import com.iskportal.kauth.security.model.AuthenticationMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthenticationMethodRepo: JpaRepository<AuthenticationMethod, Long>
