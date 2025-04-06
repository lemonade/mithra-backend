package com.iskportal.mithra.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.RestClientResponseException
import java.time.Instant

@Component
@Order(800)
class ExceptionHandlerFilter(
    val actualHandler: ISKExceptionHandler,
    private val jacksonObjectMapper: ObjectMapper
): Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            chain.doFilter(request, response)
        }catch (t: Throwable){
            val res = actualHandler.baseExceptionHandler(t)
            response as HttpServletResponse
            response.status = res.statusCode.value()
            response.writer.write(jacksonObjectMapper.writeValueAsString(res.body!!))
            response.writer.flush()
        }
    }

}

@Component
@ControllerAdvice
class ISKExceptionHandler {

    inner class ExceptionDto(
        val message: String?
    ){
        val time: Instant = Instant.now()
        // val traceId: String = tracer.currentSpan().context().traceIdString()
        // val spanId: String = tracer.currentSpan().context().spanIdString()
    }


    // @Autowired
    // lateinit var tracer: Trace //TODO fix tracer in exception handling

    @ExceptionHandler(Throwable::class)
    fun baseExceptionHandler(e: Throwable): ResponseEntity<ExceptionDto> =
        when (e) {
            is BaseException -> {
                ResponseEntity.status(e.statusCode)
                    .body(ExceptionDto(e.message))
            }

            is RestClientResponseException -> {
                ResponseEntity.status(e.statusCode)
                    .body(ExceptionDto(e.message))
            }

            is AccessDeniedException -> {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ExceptionDto(e.message))
            }

            else -> {
                logger.warn("unmanaged exception", e)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ExceptionDto(e.message))
            }
        }

    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionHandler::class.java)
    }

}