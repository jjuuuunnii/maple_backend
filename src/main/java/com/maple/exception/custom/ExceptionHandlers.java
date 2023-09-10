package com.maple.exception.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity handleCustomException(CustomException e) {
        log.error("error = {}",e.getErrorCode().getCode());
        return new ResponseEntity(e.getErrorCode().getCode(), e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity handleCustomException(UsernameNotFoundException e) {
        log.error("error = {}",e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    //DTO에서 예외가 들어왔을 때
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
