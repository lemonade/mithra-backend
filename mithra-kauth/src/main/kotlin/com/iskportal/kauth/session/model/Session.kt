package com.iskportal.kauth.session.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.iskportal.kauth.security.model.AuthenticationMethod
import com.iskportal.kauth.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*
import kotlin.jvm.Transient

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Table(name = "isk_session")
@EntityListeners(AuditingEntityListener::class)
@DiscriminatorColumn(name = "d_type", columnDefinition = "varchar(255)")
abstract class Session(
    @ManyToOne
    @JsonIgnore
    open val user: User,
    open val sessionInformation: SessionInformation = SessionInformation(),
    open var token: UUID = UUID.randomUUID(),
    open var tokenLastUsed: Instant = Instant.now(),
    @ManyToOne
    open val authenticationMethod: AuthenticationMethod
) {


    @JsonIgnore
    @jakarta.persistence.Transient
    abstract fun getSessionInvariantInformation(): String

    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    open lateinit var sessionUUID: UUID


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "isk-session_gen")
    @SequenceGenerator(name = "isk-session_gen", sequenceName = "isk-session_seq")
    @Column(name = "session_id", nullable = false)
    open var sessionId: Long? = null

    @CreatedDate
    open lateinit var createdDate: Instant

    @LastModifiedDate
    open lateinit var lastModifiedDate: Instant

}

@Embeddable
open class SessionInformation(
    var ipAddress: String? = null,
    var lastUsed: Date? = null,
    var lastOrigin: String? = null,
    var lastUserAgent: String? = null,
)
