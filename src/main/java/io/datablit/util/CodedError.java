package io.datablit.util;

public class CodedError extends RuntimeException {
    private int code = 500;
    public CodedError(String message) {
        super(message);
    }
    public CodedError(String message, int code) {
        super(message);
        this.code = code;
    }
}
