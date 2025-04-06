package com.iskportal.kauth.user.model

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.*

@Converter
class UUIDSetConverter : AttributeConverter<MutableSet<UUID>, String> {
    override fun convertToDatabaseColumn(attribute: MutableSet<UUID>): String =
        attribute.joinToString(",") { it.toString() }


    override fun convertToEntityAttribute(dbData: String): MutableSet<UUID> =
        dbData.split(",").filter { it.isNotBlank() }.map { UUID.fromString(it) }.toMutableSet()

}