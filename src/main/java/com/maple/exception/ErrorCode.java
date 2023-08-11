package com.maple.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND");


    private HttpStatus httpStatus;
    private String code;

    ErrorCode(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
    }
}
