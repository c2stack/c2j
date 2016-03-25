package io.datablit.node;

import io.datablit.util.CodedError;

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
