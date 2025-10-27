package ru.otus.chat.server.exception;

public class CommonServerException extends RuntimeException {

    public CommonServerException (Throwable throwable) {
        super(throwable.getMessage(), throwable.getCause());
    }
}
