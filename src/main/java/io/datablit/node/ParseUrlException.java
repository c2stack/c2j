package io.datablit.node;

import io.datablit.util.CodedError;

/**
 *
 */
public class ParseUrlException extends CodedError {
    public ParseUrlException(String message) {
        super(message);
    }
    public ParseUrlException(String message, int code) {
        super(message, code);
    }
}
