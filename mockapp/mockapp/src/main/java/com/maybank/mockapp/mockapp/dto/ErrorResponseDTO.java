package com.maybank.mockapp.mockapp.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponseDTO {

    private String error;
    private String message;
    private int status;
    private LocalDateTime time;
    private String apiPath;

    public ErrorResponseDTO(HttpStatus httpStatus, String message, String apiPath) {
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.status = httpStatus.value();
        this.time = LocalDateTime.now();
        this.apiPath = apiPath;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
