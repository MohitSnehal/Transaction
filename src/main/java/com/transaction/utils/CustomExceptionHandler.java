package com.transaction.utils;

import com.transaction.dto.ResponseDTO;
import com.transaction.exceptions.BadRequestException;
import com.transaction.exceptions.EntityAlreadyExistsException;
import com.transaction.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.ServiceUnavailableException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseDTO<ErrorResponse> responseDTO = new ResponseDTO<>() ;
        responseDTO.setSuccess(false) ;
        String errorMessage = constructErrorMessage(ex) ;
        LOGGER.error("ValidationFailed {}",ex) ;
        ErrorResponse errorResponse = new ErrorResponse("ValidationFailed",errorMessage) ;
        responseDTO.setPayload(errorResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST) ;
    }

    private String constructErrorMessage(MethodArgumentNotValidException ex){
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors() ;
        String errorMessage = fieldErrorList.stream()
                .map(fieldError -> String.valueOf(fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", ")) ;
        return errorMessage ;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServerExceptions(Exception ex){
        ResponseDTO<ErrorResponse> responseDTO = new ResponseDTO<>() ;
        responseDTO.setSuccess(false) ;
        ErrorResponse errorResponse = null ;
        HttpStatus httpStatus = null ;

        if(ex instanceof CompletionException){
            ex  = (Exception)ex.getCause() ;
        }

       if(ex instanceof BadRequestException){
            errorResponse = new ErrorResponse("BadRequest", ex.getMessage());
            LOGGER.error("BadRequestException {}", ex);
            httpStatus = HttpStatus.BAD_REQUEST ;
        } else if(ex instanceof EntityNotFoundException) {
            errorResponse = new ErrorResponse("EntityNotFound", ex.getMessage());
            LOGGER.error("EntityNotFound {}", ex);
            httpStatus = HttpStatus.NOT_FOUND;
        } else if(ex instanceof EntityAlreadyExistsException){
            errorResponse = new ErrorResponse("EntityExists", ex.getMessage());
            LOGGER.error("EntityExistsException {}", ex);
            httpStatus = HttpStatus.BAD_REQUEST;
        } else {
           errorResponse = new ErrorResponse("Something went wrong!", ex.getMessage());
           LOGGER.error("EntityExistsException {}", ex);
           httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
       }
        responseDTO.setPayload(errorResponse);
        return new ResponseEntity<>(responseDTO, httpStatus) ;

    }
}
