package com.sparta.instagram.exception;


import lombok.Getter;

//
@Getter
public class ErrorResponse extends Throwable {

    //    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;
//    private final String errorCode;

    public ErrorResponse(ErrorCode errorCode) {

        this.message = errorCode.getErrorMessage();
    }
}

