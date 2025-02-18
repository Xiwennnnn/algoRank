package com.algo.exception;

public class HttpRequestWrongException extends Exception {
    public HttpRequestWrongException(String message) {
        super(message);
    }
    public HttpRequestWrongException() {
        super();
    }
}
