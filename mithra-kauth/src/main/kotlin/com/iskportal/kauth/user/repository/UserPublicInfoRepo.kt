package com.iskportal.kauth.user.repository

import com.iskportal.kauth.user.model.UserPublicInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPublicInfoRepo: JpaRepository<UserPublicInfo, Long>
