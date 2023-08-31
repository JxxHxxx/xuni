package com.xuni.statistics.domain.exception;

public class RatingOutOfBoundException extends RuntimeException {
    public RatingOutOfBoundException(String message) {
        super(message);
    }
}
