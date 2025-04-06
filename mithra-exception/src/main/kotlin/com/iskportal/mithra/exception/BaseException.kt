package com.iskportal.mithra.exception

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

open class BaseException(
    errStatusCode: HttpStatus,
    msg: String,
    val errorType: ErrorType,
    val enumCode: Int
) : HttpClientErrorException(msg, errStatusCode, "", null, null, null)

enum class ErrorType {
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    INTERNAL_ERROR
}

