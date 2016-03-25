package org.conf2.data;

import org.conf2.CodedError;

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
