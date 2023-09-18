package com.musala.drones.exception;


import com.musala.drones.dto.CommonErrorDto;

public class InternalServerErrorException extends OperationsAbstractException {

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(CommonErrorDto error) {
        super(error);
    }
}
