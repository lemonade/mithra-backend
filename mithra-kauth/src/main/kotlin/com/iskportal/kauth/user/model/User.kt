package com.iskportal.kauth.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.iskportal.kauth.security.model.AuthenticationMethod
import com.iskportal.kauth.session.model.Session
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@Entity
@Table(name = "isk_user")
@EntityListeners(AuditingEntityListener::class)
class User internal constructor() {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq")
    @Column(name = "user_id", nullable = false)
    var userId: Long? = null

    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "user_uuid", nullable = false)
    var userUuid: UUID? = null

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    val authenticationMethods: MutableSet<AuthenticationMethod> = mutableSetOf()

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    val sessions: MutableSet<Session> = mutableSetOf()

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as User

        return userId == other.userId
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $userId )"
    }
}