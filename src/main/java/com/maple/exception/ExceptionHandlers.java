package com.maple.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity handleCustomException(CustomException e) {
        log.info("error = {}",e.getErrorCode().getCode());
        return new ResponseEntity(e.getErrorCode().getCode(), e.getErrorCode().getHttpStatus());
    }
}
