package com.iskportal.kauth.security.model

import com.iskportal.kauth.user.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.hibernate.proxy.HibernateProxy

@Entity
class PrincipalPasswordAuthenticationMethod(
    @Column(unique = true, nullable = false, updatable = false, length = 255)
    open val principal: String,
    @Column(nullable = false, updatable = true)
    open var passwordHash: String,

    user: User,
) : AuthenticationMethod(user = user) {

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as PrincipalPasswordAuthenticationMethod

        return authenticationMethodId != null && authenticationMethodId == other.authenticationMethodId
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(  authenticationMethodId = $authenticationMethodId )"
    }
}
