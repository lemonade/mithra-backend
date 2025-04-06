package com.iskportal.mithra.exception

import org.springframework.http.HttpStatus

class NotFoundException(msg: String) :
    BaseException(HttpStatus.NOT_FOUND, msg, ErrorType.BAD_REQUEST, 3)