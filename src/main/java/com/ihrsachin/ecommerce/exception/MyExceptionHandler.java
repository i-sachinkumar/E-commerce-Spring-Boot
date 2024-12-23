package com.ihrsachin.ecommerce.exception;

import com.ihrsachin.ecommerce.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {




//    @ExceptionHandler
//    public ResponseEntity<String> onResourceNotFoundException() {
//        Map<String, String> map = new HashMap<>();
//
//        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
//    }


    @ExceptionHandler
    public ResponseEntity<APIResponse> onAPIException(APIException e) {
        APIResponse apiResponse = APIResponse.builder()
                .message(e.getMessage())
                .isSuccessful(false)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<APIResponse> onResourceNotFoundException(ResourceNotFoundException e) {
        APIResponse apiResponse = APIResponse.builder()
                .message(e.getMessage())
                .isSuccessful(false)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}
