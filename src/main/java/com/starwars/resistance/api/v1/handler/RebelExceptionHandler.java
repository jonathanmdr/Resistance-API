package com.starwars.resistance.api.v1.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.starwars.resistance.api.v1.dto.output.ApiErrorDto;
import com.starwars.resistance.domain.exception.BusinessException;
import com.starwars.resistance.domain.exception.EntityNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RebelExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String GENERIC_ERROR_MESSAGE = "An unexpected internal error has occurred. "
        + "Try again later, if the problem persists, please contact the system administrator.";

    private final MessageSource messageSource;

    public RebelExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handlerUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorDto error = createApiError(status, GENERIC_ERROR_MESSAGE);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorDto error = createApiError(status, ex.getMessage());
        error.setUserMessage(ex.getMessage());

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorDto error = createApiError(status, ex.getMessage());
        error.setUserMessage(ex.getMessage());

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format("The resource '%s' not found.", ex.getRequestURL());

        ApiErrorDto error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        } else if (rootCause instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) rootCause, headers, status, request);
        }

        ApiErrorDto error = createApiError(status, "The request body is invalid. Verify syntax error.");
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("The property '%s' received value '%s', which is an invalid value."
            + " Inform the compatible value with data type %s.", path, ex.getValue(), ex.getTargetType().getSimpleName());

        ApiErrorDto error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("Property '%s' not found.", path);

        ApiErrorDto error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format("The URL parameter '%s' received value '%s', which is an invalid value."
            + " Inform the compatible value with data type %s.", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ApiErrorDto error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = "One or more fields are invalid. fill in correctly and try again.";

        List<ApiErrorDto.FieldError> fields = bindingResult.getAllErrors()
            .stream()
            .map(objectError -> {
                String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                String name = objectError.getObjectName();

                if (objectError instanceof FieldError) {
                    name = ((FieldError) objectError).getField();
                }

                return new ApiErrorDto.FieldError(name, message);
            })
            .collect(Collectors.toList());

        ApiErrorDto error = createApiError(status, detail);
        error.setUserMessage(detail);
        error.setFields(fields);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ApiErrorDto createApiError(HttpStatus status, String detail) {
        ApiErrorDto apiError = new ApiErrorDto();

        apiError.setStatus(status.value());
        apiError.setDetail(detail);
        apiError.setTimestamp(OffsetDateTime.now());

        return apiError;
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
            .map(JsonMappingException.Reference::getFieldName)
            .collect(Collectors.joining("."));
    }

}
