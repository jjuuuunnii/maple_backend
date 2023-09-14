package com.maple.exception.custom;

import com.maple.dto.exception.ErrorDto;
import com.maple.exception.jwt.InvalidAccessTokenException;
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
    public ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
        log.error("error = {}",e.getErrorCode().getCode());
        ErrorDto errorDto = ErrorDto.builder().code(e.getMessage()).build();
        return new ResponseEntity<>(errorDto, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorDto> handleCustomException(UsernameNotFoundException e) {
        log.error("error = {}",e.getMessage());
        ErrorDto errorDto = ErrorDto.builder().code(e.getMessage()).build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    //DTO에서 예외가 들어왔을 때
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("error = {}", ex.getMessage());
        ErrorDto errorDto = ErrorDto.builder().code(ex.getMessage()).build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidAccessTokenException.class})
    public ResponseEntity<ErrorDto> handleInvalidAccessTokenException(InvalidAccessTokenException e) {
        log.error("error = {}", e.getMessage());
        ErrorDto errorDto = ErrorDto.builder().code(e.getMessage()).build();
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }

}
