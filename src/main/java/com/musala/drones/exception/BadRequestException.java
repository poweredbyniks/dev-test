package com.musala.drones.exception;


import com.musala.drones.dto.CommonErrorDto;

public class BadRequestException extends OperationsAbstractException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(CommonErrorDto error) {
        super(error);
    }
}
