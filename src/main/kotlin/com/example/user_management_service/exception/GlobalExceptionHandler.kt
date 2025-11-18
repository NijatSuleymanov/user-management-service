package com.example.user_management_service.exception

import com.example.user_management_service.response.ErrorResponse
import com.example.user_management_service.response.ValidationErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        ex: ResourceNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("ResourceNotFoundException - Message: {}, Path: {}", ex.message, request.requestURI)
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Resource Not Found",
            message = ex.message ?: "The requested resource does not exist",
            path = request.requestURI,
            errorCode = "RESOURCE_NOT_FOUND"
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicateResourceException(
        ex: DuplicateResourceException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("DuplicateResourceException - Message: {}, Path: {}", ex.message, request.requestURI)
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Duplicate Resource",
            message = ex.message ?: "A resource with this data already exists",
            path = request.requestURI,
            errorCode = "DUPLICATE_RESOURCE"
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(
        ex: ValidationException,
        request: HttpServletRequest
    ): ResponseEntity<ValidationErrorResponse> {
        logger.warn("ValidationException - Message: {}, Path: {}, Fields: {}", ex.message, request.requestURI, ex.fieldErrors)
        val errorResponse = ValidationErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            message = ex.message ?: "Input validation failed",
            path = request.requestURI,
            fieldErrors = ex.fieldErrors
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        ex: BusinessException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("BusinessException - Message: {}, ErrorCode: {}, Path: {}", ex.message, ex.errorCode, request.requestURI)
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Business Logic Error",
            message = ex.message ?: "A business logic error occurred",
            path = request.requestURI,
            errorCode = ex.errorCode
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ValidationErrorResponse> {
        val fieldErrors = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.forEach { error ->
            fieldErrors[error.field] = error.defaultMessage ?: "Invalid value"
        }

        logger.warn("MethodArgumentNotValidException - Path: {}, Fields: {}", request.requestURI, fieldErrors)
        val errorResponse = ValidationErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            message = "Request validation failed",
            path = request.requestURI,
            fieldErrors = fieldErrors
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(
        ex: NoHandlerFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("NoHandlerFoundException - Path: {}, Method: {}", ex.requestURL, ex.httpMethod)
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Endpoint Not Found",
            message = "The requested endpoint does not exist",
            path = request.requestURI,
            errorCode = "ENDPOINT_NOT_FOUND"
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected Exception - Message: {}, Path: {}, StackTrace: ", ex.message, request.requestURI, ex)
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred",
            path = request.requestURI,
            errorCode = "INTERNAL_SERVER_ERROR"
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}