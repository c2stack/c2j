package org.c2stack.node;

import org.c2stack.util.CodedError;

/**
 *
 */
public class EditException extends CodedError {
    public EditException(String msg) {
        super(msg);
    }
    public EditException(String msg, int code) {
        super(msg, code);
    }
}
