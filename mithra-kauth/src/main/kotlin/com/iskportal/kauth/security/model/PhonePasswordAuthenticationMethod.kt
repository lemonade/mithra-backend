package com.iskportal.kauth.security.model

import com.iskportal.kauth.user.model.User
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.Transient

@Entity
@DiscriminatorValue("PHONE")
class PhonePasswordAuthenticationMethod(
    phone: String, passwordHash: String, user: User
) : PrincipalPasswordAuthenticationMethod(
    passwordHash = passwordHash, user = user, principal = phone
) {
    @Transient
    fun getPhone() = principal
}
