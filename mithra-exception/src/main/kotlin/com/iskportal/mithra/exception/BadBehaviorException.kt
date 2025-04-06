package com.iskportal.mithra.exception

import org.springframework.http.HttpStatus

class BadBehaviorException(msg: String) :
    BaseException(HttpStatus.BAD_REQUEST, msg, ErrorType.BAD_REQUEST, 5)