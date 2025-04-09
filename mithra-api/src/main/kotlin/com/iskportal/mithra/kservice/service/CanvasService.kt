package com.iskportal.mithra.kservice.service

import com.iskportal.kauth.security.service.SecurityService
import com.iskportal.mithra.exception.NotFoundException
import com.iskportal.mithra.kservice.dto.SlideUpdateDto
import com.iskportal.mithra.kservice.dto.SlideUpdateType
import com.iskportal.mithra.kservice.model.Canvas
import com.iskportal.mithra.kservice.model.CanvasSlide
import com.iskportal.mithra.kservice.repository.CanvasRepo
import com.iskportal.mithra.utils.toUUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CanvasService(
    private val canvasRepo: CanvasRepo
) {

    /**
     * Create canvas with title and slides content.
     */
    @Transactional
    fun createCanvas(title: String, slides: List<String>): Canvas {
        var canvas = Canvas(title)
        canvas.slides = slides.map { CanvasSlide(canvas, it) }.toMutableList()
        canvas = canvasRepo.save(canvas)
        canvas.orderingSlides()

        canvasRepo.save(canvas)
        return canvas
    }

    /**
     * List all canvases owned by current user.
     */
    fun listCanvas(): List<Canvas> {
        return canvasRepo.findByOwner(SecurityService.userId)
    }

    /**
     * Get canvas detail by uuid.
     */
    fun getDetail(uuid: String): Canvas {
        return canvasRepo.findByCanvasUuidAndOwner(uuid.toUUID(), SecurityService.userId)
            ?: throw NotFoundException("No canvas found")
    }

    /**
     * Update canvas content.
     */
    @Transactional
    fun update(uuid: String, title: String?, slideUpdate: List<SlideUpdateDto>?) : Canvas {
        val canvas = getDetail(uuid)

        if (title != null) {
            canvas.title = title
        }

        if (!slideUpdate.isNullOrEmpty()) {
            for (slide in slideUpdate) {
                if (slide.updateType == SlideUpdateType.UPDATE) {
                    // update existing slide
                    canvas.slides.forEach {
                        if (it.hasUuidInitialized() && it.slideUuid == slide.slideId) {
                            require(!slide.content.isNullOrEmpty())
                            it.content = slide.content[0]
                        }
                    }

                } else if (slide.updateType == SlideUpdateType.ADD) {
                    // append slide after existing slide
                    val index = canvas.slides.indexOfFirst { it.slideUuid == slide.slideId }
                    require(index != -1 && !slide.content.isNullOrEmpty())
                    canvas.slides.addAll(index + 1, slide.content.map { CanvasSlide(canvas, it) })

                } else if (slide.updateType == SlideUpdateType.DELETE) {
                    canvas.slides.removeIf { it.slideUuid == slide.slideId }
                }
            }
        }

        canvasRepo.save(canvas)
        canvas.orderingSlides()
        return canvasRepo.save(canvas)
    }

    /**
     * Delete canvas by uuid.
     */
    @Transactional
    fun delete(uuid: String): Boolean {
        // ignore if canvas not found
        val canvas = canvasRepo.findByCanvasUuidAndOwner(uuid.toUUID(), SecurityService.userId)
            ?: return true

        // delete it
        canvasRepo.deleteById(canvas.id!!)
        return true
    }
}