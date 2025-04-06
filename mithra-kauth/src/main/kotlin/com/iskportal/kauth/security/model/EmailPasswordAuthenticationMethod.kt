package com.iskportal.kauth.security.model

import com.iskportal.kauth.user.model.User
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.Transient

@Entity
@DiscriminatorValue("EMAIL")
class EmailPasswordAuthenticationMethod(
    email: String, passwordHash: String, user: User
) : PrincipalPasswordAuthenticationMethod(
    passwordHash = passwordHash, user = user, principal = email
) {
    @Transient
    fun getEmail() = principal
}
