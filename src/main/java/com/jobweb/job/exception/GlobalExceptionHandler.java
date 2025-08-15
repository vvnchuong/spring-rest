package com.jobweb.job.exception;

import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse>handingRuntimeException(){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse>handlingAppException(AppException appException){
        ErrorCode errorCode = appException.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

}
