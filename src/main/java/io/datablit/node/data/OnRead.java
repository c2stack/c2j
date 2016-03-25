package org.conf2.data;

import org.conf2.schema.HasDataType;

/**
 *
 */
public interface OnRead {
    Value read(FieldRequest r);
}
