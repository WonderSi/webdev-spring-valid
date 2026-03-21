package com.example.lab5.web

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: NoSuchElementException) = ErrorResponse(
        status = 404,
        error = "Not Found",
        message = ex.message ?: "Not found"
    )

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(ex: IllegalArgumentException) = ErrorResponse(
        status = 400,
        error = "Bad Request",
        message = ex.message ?: "Bad request"
    )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNotReadable(ex: HttpMessageNotReadableException) = ErrorResponse(
        status = 400,
        error = "Bad Request",
        message = "Invalid request body"
    )
}
