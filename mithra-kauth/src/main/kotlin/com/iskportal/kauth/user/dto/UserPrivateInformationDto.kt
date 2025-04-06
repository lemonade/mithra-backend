package com.iskportal.kauth.user.dto

import java.util.*

data class UserPrivateInformationDto(
    val profilePictures: MutableSet<UUID>,
    val email: String?,
    val emailPublic: Boolean,
    val phoneNumber: String?,
    val phonePublic: Boolean,
    val isOnlinePublic: Boolean,
    val picturePublic: Boolean
)
