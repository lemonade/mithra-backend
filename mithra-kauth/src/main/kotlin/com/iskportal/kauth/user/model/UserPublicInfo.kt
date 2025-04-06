package com.iskportal.kauth.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.iskportal.kauth.user.dto.UserPublicInformationDto
import jakarta.persistence.*
import java.util.*

@Entity
class UserPublicInfo(
    @OneToOne(cascade = [(CascadeType.PERSIST)])
    @MapsId
    @JsonIgnore
    val user: User,

    @OneToOne(cascade = [(CascadeType.PERSIST)])
    @JsonIgnore
    val userPrivateInfo: UserPrivateInfo,

    @Column(columnDefinition = "varchar(255)", name = "user_handler", unique = true)
    var userHandler: String? = null,

    @Column(columnDefinition = "varchar(255)", name = "user_name")
    var name: String? = null,

    @Column(columnDefinition = "varchar(255)", name = "user_last_name")
    var lastName: String? = null,
) {

    @get:Transient
    val asDto: UserPublicInformationDto
        get() =
            UserPublicInformationDto(
                handle = userHandler,
                name = name,
                lastName = lastName,
                profilePictures = profilePictures,
                email = userEmail,
                emailPublic = userPrivateInfo.userEmailPublicized,
                phoneNumber = phoneNumber,
                phonePublic = userPrivateInfo.phoneNumberPublicized,
                isOnlinePublic = userPrivateInfo.useIsOnlinePublicized,
                picturePublic = userPrivateInfo.profilePicturesPublicized
            )

    @Id
    var userPublicInfoId: Long? = null

    @get:Transient
    val profilePictures: MutableSet<UUID>
        get() =
            if (userPrivateInfo.profilePicturesPublicized)
                userPrivateInfo.profilePictures
            else mutableSetOf()

    @get:Transient
    val userEmail: String?
        get() =
            if (userPrivateInfo.userEmailPublicized)
                userPrivateInfo.userEmail
            else null

    @get:Transient
    val phoneNumber: String?
        get() =
            if (userPrivateInfo.phoneNumberPublicized)
                userPrivateInfo.phoneNumber
            else null

    @get:Transient
    val useIsOnlinePublicized: Boolean
        get() =
            userPrivateInfo.useIsOnlinePublicized

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPublicInfo

        return userPublicInfoId == other.userPublicInfoId
    }

    override fun hashCode(): Int {
        return userPublicInfoId?.hashCode() ?: 0
    }

}
