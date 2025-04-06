package com.iskportal.kauth.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.iskportal.kauth.user.dto.UserPrivateInformationDto
import jakarta.persistence.*
import java.util.*

@Entity
class UserPrivateInfo(
    @OneToOne(cascade = [CascadeType.PERSIST])
    @MapsId
    @JsonIgnore
    val user: User,
    var profilePicturesPublicized: Boolean = false,

    @Column(columnDefinition = "varchar(255)", name = "user_email")
    var userEmail: String? = null,
    var userEmailPublicized: Boolean = false,

    @Column(columnDefinition = "varchar(255)", name = "user_phone_number")
    var phoneNumber: String? = null,
    var phoneNumberPublicized: Boolean = false,

    @Column(name = "user_is_online")
    var useIsOnlinePublicized: Boolean = false

) {

    @get:Transient
    val asDto: UserPrivateInformationDto
        get() =
            UserPrivateInformationDto(
                profilePictures = profilePictures,
                email = userEmail,
                emailPublic = userEmailPublicized,
                phoneNumber = phoneNumber,
                phonePublic = phoneNumberPublicized,
                isOnlinePublic = useIsOnlinePublicized,
                picturePublic = profilePicturesPublicized
            )

    @Column(columnDefinition = "varchar(4095)", name = "profile_pictures")
    @Convert(converter = UUIDSetConverter::class)
    val profilePictures: MutableSet<UUID> = mutableSetOf()

    @Id
    @JsonIgnore
    var userPrivateInfoId: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPrivateInfo

        return userPrivateInfoId == other.userPrivateInfoId
    }

    override fun hashCode(): Int {
        return userPrivateInfoId?.hashCode() ?: 0
    }
}
