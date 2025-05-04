package com.orderprocessing.orderprocessing.advice;

import com.orderprocessing.orderprocessing.exception.OrderPersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(OrderPersistenceException.class)
    public ResponseEntity<String> handleOrderPersistenceException(OrderPersistenceException ex)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error saving order: " + ex.getMessage());
    }
}
