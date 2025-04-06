package com.iskportal.kauth.user.dto

import java.util.UUID

data class UserPublicInformationDto(
    val handle: String?,
    val name: String?,
    val lastName: String?,
    val profilePictures: Set<UUID>,
    val email: String?,
    val emailPublic: Boolean,
    val phoneNumber: String?,
    val phonePublic: Boolean,
    val isOnlinePublic: Boolean,
    val picturePublic: Boolean
)
