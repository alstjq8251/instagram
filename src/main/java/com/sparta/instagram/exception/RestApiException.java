package com.sparta.instagram.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestApiException {
    private String errorMessage;
    private boolean resultFlag;
    private long code;
    private HttpStatus httpStatus;
}