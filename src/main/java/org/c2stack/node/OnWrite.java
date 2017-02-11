package org.c2stack.node;

import org.c2stack.meta.HasDataType;

/**
 *
 */
public interface OnWrite {
    void write(FieldRequest r, Value val);
}
