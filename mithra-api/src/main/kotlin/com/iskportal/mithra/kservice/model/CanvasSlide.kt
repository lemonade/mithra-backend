package com.iskportal.mithra.kservice.model

import com.iskportal.kauth.user.model.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "isk_canvas_slide")
class CanvasSlide(
    @ManyToOne
    @JoinColumn(name = "canvas_id")
    val canvas: Canvas,

    @Column(name = "content", nullable = false)
    var content: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canvas_slide_id_seq")
    @SequenceGenerator(name = "canvas_slide_id_seq", sequenceName = "canvas_slide_id_seq")
    @Column(name = "canvas_slide_id", nullable = false)
    var id: Long? = null

    @Column(name = "slide_uuid", nullable = false)
    lateinit var slideUuid: UUID

    @Column(name = "order_id", nullable = false)
    var orderId: Int = 0

    @CreatedDate
    lateinit var createdAt: Instant

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    lateinit var createdBy: User

    @LastModifiedDate
    lateinit var updatedAt: Instant

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_by")
    lateinit var updatedBy: User

    @PrePersist
    fun prePersist() {
        slideUuid = UUID.randomUUID()
    }

    fun hasUuidInitialized(): Boolean = ::slideUuid.isInitialized
}