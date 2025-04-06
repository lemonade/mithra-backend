package com.iskportal.kauth.session.model

import com.iskportal.kauth.security.model.AuthenticationMethod
import com.iskportal.kauth.security.model.PrincipalPasswordAuthenticationMethod
import com.iskportal.kauth.user.model.User
import jakarta.persistence.Entity

@Entity
class PrincipalPasswordSession(
    user: User,
    authenticationMethod: AuthenticationMethod
) : Session(user, authenticationMethod = authenticationMethod) {
    override fun getSessionInvariantInformation(): String {
        return (authenticationMethod as PrincipalPasswordAuthenticationMethod).principal.toString() +
                "-" +
                (authenticationMethod as PrincipalPasswordAuthenticationMethod).passwordHash.toString()
    }
}