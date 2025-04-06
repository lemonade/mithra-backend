package com.iskportal.mithra.exception

import org.springframework.http.HttpStatus

class MalformedData(message: String) :
    BaseException(HttpStatus.BAD_REQUEST, message, ErrorType.BAD_REQUEST, 2)