package com.iskportal.mithra.kservice.repository

import com.iskportal.mithra.kservice.model.Canvas
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CanvasRepo : JpaRepository<Canvas, Long> {
    @Query(
        """
        select p 
        from Canvas p where p.createdBy.userId = :createdBy
        """
    )
    fun findByOwner(createdBy: Long): List<Canvas>

    @Query(
        """
        select p from Canvas p 
        where p.canvasUuid = :uuid and p.createdBy.userId = :createdBy
        """
    )
    fun findByCanvasUuidAndOwner(uuid: UUID, createdBy: Long): Canvas?
}
