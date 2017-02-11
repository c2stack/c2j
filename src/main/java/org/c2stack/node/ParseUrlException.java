package org.c2stack.node;

import org.c2stack.util.CodedError;

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
