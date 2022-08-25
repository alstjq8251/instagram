package com.sparta.instagram.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode { //이렇게 해주는 방법도 있다. 현재 코드에는 적용되지않았다.

    //200 Response to 400 Bad Request
    NEED_A_LOGIN(HttpStatus.OK, "로그인이 필요합니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    //    private final String errorCode;
    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}



//    // 400 Bad Request
//    DUPLICATED_FOLDER_NAME(HttpStatus.BAD_REQUEST, "400_1", "중복폴더명이 이미 존재합니다."),
//
//
//    // 404 Not Found
//    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "404_1", "해당 관심상품 아이디가 존재하지 않습니다."),
//    NOT_FOUND_FOLDER(HttpStatus.NOT_FOUND, "404_2", "해당 폴더 아이디가 존재하지 않습니다."),

