package com.yafix.engine;

public class FixParseException extends Exception {

    private final ParseError error;

    public enum ParseError {
        MalformedMessage,

    }

    public FixParseException(String message, ParseError error) {
        super(message);
        this.error = error;
    }
}
