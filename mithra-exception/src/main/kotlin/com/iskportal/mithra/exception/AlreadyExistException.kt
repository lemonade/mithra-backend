package com.iskportal.mithra.exception

import org.springframework.http.HttpStatus

class AlreadyExistException(msg: String) :
    BaseException(HttpStatus.BAD_REQUEST, msg, ErrorType.BAD_REQUEST, 4)
