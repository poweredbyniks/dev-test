package com.musala.drones.controller;

import com.musala.drones.dto.CommonErrorDto;
import com.musala.drones.exception.InternalServerErrorException;
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


    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<CommonErrorDto> handleInternalServerErrorException(InternalServerErrorException e) {
        log.error("{} InternalServerErrorException, preparing error response: {}", e.getCommonError().getProcessId(), e.getCommonError().getMessage());

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MethodArgumentNotValidException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponseBadRequest(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MethodArgumentNotValidException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponseBadRequest(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MethodArgumentNotValidException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponseBadRequest(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MissingRequestHeaderException, preparing error response: {}", ex.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponseBadRequest(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("HttpMessageNotReadableException, preparing error response: {}", e.getMessage());
        return new ResponseEntity<>(buildCommonErrorResponseBadRequest(), HttpStatus.BAD_REQUEST);
    }


    private CommonErrorDto buildCommonErrorResponseBadRequest() {
        final CommonErrorDto response = new CommonErrorDto();
        response.setTimestamp(Instant.now());
        response.setService(serviceName);
        response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return response;
    }
}