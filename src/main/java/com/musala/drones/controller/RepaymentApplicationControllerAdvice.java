package com.musala.drones.controller;

import com.musala.drones.dto.CommonErrorDto;
import com.musala.drones.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class RepaymentApplicationControllerAdvice extends ResponseEntityExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CommonErrorDto> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException, preparing error response: {}", e.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MissingPathVariableException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MissingServletRequestParameterException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MethodArgumentNotValidException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponse(ex.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MissingRequestHeaderException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("HttpMessageNotReadableException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    private CommonErrorDto buildCommonErrorResponse(String message) {
        final CommonErrorDto response = new CommonErrorDto();
        response.setTimestamp(Instant.now());
        response.setService(serviceName);
        response.setMessage(message);
        return response;
    }
}