package com.orderprocessing.orderprocessing.exception;

public class OrderPersistenceException extends RuntimeException
{
    public OrderPersistenceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
