package io.datablit.node;

import io.datablit.meta.HasDataType;

/**
 *
 */
public interface OnRead {
    Value read(FieldRequest r);
}
