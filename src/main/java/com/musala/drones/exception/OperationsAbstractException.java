package com.musala.drones.exception;

import com.musala.drones.dto.CommonErrorDto;
import lombok.Data;

@Data
public class OperationsAbstractException extends RuntimeException {

    private CommonErrorDto commonError;

    public OperationsAbstractException() {
        super();
    }

    public OperationsAbstractException(String message) {
        super(message);
    }

    public OperationsAbstractException(CommonErrorDto commonError) {
        super();
        this.commonError = commonError;
    }
}
