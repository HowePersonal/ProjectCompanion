package com.example.projectwaifu.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {
    private HttpStatus status;
    private Object data;

    public ApiResponse() {}

    public ApiResponse(HttpStatus status, Object data) {
        this.status = status;
        this.data = data;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setResponse(Object data, HttpStatus status) {
        this.status = status;
        this.data = data;
    }

    public ResponseEntity<Object> toResponseEntity() {
        return new ResponseEntity<>(this.data, this.status);
    }


}
