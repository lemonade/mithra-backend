package com.iskportal.mithra.kservice.model

import com.iskportal.kauth.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "isk_canvas")
class Canvas(
    @Column(name = "title", nullable = false)
    var title: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canvas_id_seq")
    @SequenceGenerator(name = "canvas_id_seq", sequenceName = "canvas_id_seq")
    @Column(name = "canvas_id", nullable = false)
    var id: Long? = null

    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "canvas_uuid", nullable = false)
    lateinit var canvasUuid: UUID

    @OneToMany(
        mappedBy = "canvas",
        cascade = [(CascadeType.ALL)],
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    @OrderBy("order_id ASC")
    var slides: MutableList<CanvasSlide> = mutableListOf()

    @CreatedBy
    @JoinColumn(name = "created_by")
    @ManyToOne
    lateinit var createdBy: User

    @CreatedDate
    lateinit var createdAt: Instant

    @LastModifiedDate
    lateinit var updatedAt: Instant

    @JoinColumn(name = "updated_by")
    @LastModifiedBy
    @ManyToOne
    lateinit var updatedBy: User

    fun orderingSlides() {
        (0..slides.lastIndex).forEach {
            slides[it].orderId = it
        }
    }
}
