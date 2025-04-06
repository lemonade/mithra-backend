package com.iskportal.mithra.exception

import org.springframework.http.HttpStatus

class UnAuthorizedException(msg: String) :
    BaseException(HttpStatus.UNAUTHORIZED, msg, ErrorType.UNAUTHORIZED, 1)