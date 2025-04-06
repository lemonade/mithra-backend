package com.iskportal.kauth.security.model

import com.iskportal.kauth.user.model.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener::class)
abstract class AuthenticationMethod(
    @ManyToOne(cascade = [CascadeType.PERSIST])
    val user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authentication_method_gen")
    @SequenceGenerator(name = "authentication_method_gen", sequenceName = "authentication_method_seq")
    @Column(name = "authentication_method_id", nullable = false)
    var authenticationMethodId: Long? = null

    @CreatedDate
    lateinit var createdDate: Date

    @LastModifiedDate
    lateinit var lastModifiedDate: Date
}
