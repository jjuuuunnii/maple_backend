package com.maple.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //400
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "EMAIL ALREADY EXISTS"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "LOGIN FAILED"),

    //401
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID TOKEN"),

    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),

    //500
    INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "INVALID REQUEST");

    private HttpStatus httpStatus;
    private String code;

    ErrorCode(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
    }
}
