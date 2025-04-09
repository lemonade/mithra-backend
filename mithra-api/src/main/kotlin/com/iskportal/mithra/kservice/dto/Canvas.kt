package com.iskportal.mithra.kservice.dto

import com.iskportal.mithra.kservice.model.Canvas
import com.iskportal.mithra.kservice.model.CanvasSlide
import java.time.Instant
import java.util.*

data class CanvasCreateReqDto(val title: String, val slides: List<String>)

data class CanvasResDto(
    val canvasId: String,
    val title: String,
    val createdAt: Instant
) {
    constructor(model: Canvas) :
            this(model.canvasUuid.toString(), model.title, model.createdAt)
}

data class CanvasListDto(val canvases: List<CanvasResDto>) {
    companion object {
        fun of(canvases: List<Canvas>): CanvasListDto =
            CanvasListDto(canvases.map { CanvasResDto(it) })
    }
}

data class SlideDto(
    val id: UUID, val orderId: Int, val content: String
) {
    constructor(model: CanvasSlide) :
            this(model.slideUuid, model.orderId, model.content)
}

data class CanvasDetailDto(
    val canvasId: UUID,
    val title: String,
    val slides: List<SlideDto>,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    constructor(canvas: Canvas) : this(
        canvas.canvasUuid,
        canvas.title,
        canvas.slides.map { SlideDto(it) },
        canvas.createdAt,
        canvas.updatedAt
    )
}

data class CanvasDeleteResDto(val success: Boolean)

enum class SlideUpdateType {
    ADD,        // Add new slide
    UPDATE,     // Update existing slide
    DELETE,     // Delete existing slide
}

data class SlideUpdateDto(
    val updateType: SlideUpdateType,
    val content: List<String>?,
    val slideId: UUID
)

data class CanvasUpdateDto(
    val title: String?,
    val slideUpdate: List<SlideUpdateDto>?
)