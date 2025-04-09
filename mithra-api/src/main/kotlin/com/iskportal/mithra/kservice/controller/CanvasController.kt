package com.iskportal.mithra.kservice.controller

import com.iskportal.mithra.kservice.controller.resource.CanvasResource
import com.iskportal.mithra.kservice.dto.*
import com.iskportal.mithra.kservice.service.CanvasService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/canvases")
class CanvasController(
    private val canvasService: CanvasService,
) : CanvasResource {

    @PostMapping
    override fun createCanvas(@RequestBody req: CanvasCreateReqDto): CanvasResDto {
        return CanvasResDto(canvasService.createCanvas(req.title, req.slides))
    }

    @GetMapping
    fun listCanvases(): CanvasListDto {
        return CanvasListDto.of(canvasService.listCanvas())
    }

    @GetMapping("/{uuid}")
    fun getDetails(@PathVariable uuid: String): CanvasDetailDto? {
        return CanvasDetailDto(canvasService.getDetail(uuid))
    }

    @PutMapping("/{uuid}")
    fun updateCanvas(
        @PathVariable uuid: String,
        @RequestBody req: CanvasUpdateDto
    ): CanvasDetailDto? {
        return CanvasDetailDto(canvasService.update(uuid, req.title, req.slideUpdate))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: String): CanvasDeleteResDto {
        return CanvasDeleteResDto(canvasService.delete(uuid))
    }
}