package org.conf2.data;

import org.conf2.schema.HasDataType;

/**
 *
 */
public interface OnWrite {
    void write(FieldRequest r, Value val);
}
