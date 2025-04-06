package com.iskportal.kauth.user.repository

import com.iskportal.kauth.user.model.UserPrivateInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPrivateInfoRepo: JpaRepository<UserPrivateInfo, Long>
