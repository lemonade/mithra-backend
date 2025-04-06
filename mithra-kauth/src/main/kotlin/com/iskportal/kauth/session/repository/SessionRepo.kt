package com.iskportal.kauth.session.repository

import com.iskportal.kauth.session.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SessionRepo: JpaRepository<Session, Long>{
    @Modifying
    fun deleteBySessionUUID(sessionUUID: UUID)

    fun findBySessionUUID(sessionUUID: UUID): Session?
}