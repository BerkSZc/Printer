package com.berksozcu.handler;

import com.berksozcu.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ApiResponse<String>> handleBaseException (BaseException ex, WebRequest request) {
        return ResponseEntity.ok(createApiResponse(ex.getMessage(), request));
    }

    public <T>ApiResponse<T> createApiResponse(T message, WebRequest request) {
        ApiResponse<T> apiResponse = new ApiResponse();
        Exception<T> ex = new Exception<>();
                ex.setCreateTime(new Date());
                ex.setHostName(getHostName());
                ex.setPath(request.getDescription(false));
                ex.setMessage(message);
                apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                apiResponse.setException(ex);

                return apiResponse;
    }

    private String getHostName() {
        try{
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


}
