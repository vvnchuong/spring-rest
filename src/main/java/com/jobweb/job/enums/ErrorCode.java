package com.jobweb.job.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_KEY(1001, "Invalid Message Key", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(1002, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1003, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN)
    ;

    private int code;
    private String message;
    private HttpStatus httpStatusCode;

    ErrorCode(int code, String message, HttpStatus httpStatusCode){
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
