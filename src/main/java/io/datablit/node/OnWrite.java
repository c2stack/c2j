package io.datablit.node;

import io.datablit.meta.HasDataType;

/**
 *
 */
public interface OnWrite {
    void write(FieldRequest r, Value val);
}
