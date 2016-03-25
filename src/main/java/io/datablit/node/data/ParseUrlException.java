package org.conf2.data;

import org.conf2.CodedError;

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
