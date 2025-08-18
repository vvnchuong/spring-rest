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
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    FILE_EMPTY(1009, "File is empty. Please upload a file", HttpStatus.BAD_REQUEST),
    INVALID_FILE(1010, "Invalid file", HttpStatus.BAD_REQUEST),
    RESUME_NOT_EXISTED(1011, "Resume not existed", HttpStatus.BAD_REQUEST),
    JOB_NOT_EXISTED(1012, "Job not existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1013, "Permission not existed", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1014, "Permission existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1015, "Role not existed", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1016, "Role existed", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_EXISTED(1017, "Company not existed", HttpStatus.BAD_REQUEST),
    COMPANY_EXISTED(1018, "Company existed", HttpStatus.BAD_REQUEST),
    SKILL_NOT_EXISTED(1019, "Skill not existed", HttpStatus.BAD_REQUEST),
    SKILL_EXISTED(2020, "SKill existed", HttpStatus.BAD_REQUEST)
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
