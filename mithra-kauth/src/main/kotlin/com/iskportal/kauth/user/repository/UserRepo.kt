package com.iskportal.kauth.user.repository

import com.iskportal.kauth.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepo: JpaRepository<User, Long> {
    fun findByUserUuid(userUuid: UUID): User?
}
