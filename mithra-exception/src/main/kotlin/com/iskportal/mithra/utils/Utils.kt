package com.iskportal.mithra.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.iskportal.mithra.exception.MalformedData

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

val objectMapper = jacksonObjectMapper()


private class Anonymous;

val Any?.logger: Logger
    get() =
        LoggerFactory.getLogger(this?.javaClass ?: Anonymous::class.java)

fun Any.dumpToString(): String = objectMapper.writeValueAsString(this)
//fun <T : Any> publishEvent(event: T) = SingletonPool.publisher.publishEvent(event)

infix fun Boolean.otherwiseThrow(t: Throwable) {
    if (!this)
        throw t
}

fun neither(vararg condition: Boolean): Boolean {
    return !condition.any { it }
}

fun String.toUUID(): UUID =
    try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        throw MalformedData("invalid uuid")
    }