package org.c2stack.node;

import org.c2stack.meta.HasDataType;

/**
 *
 */
public interface OnRead {
    Value read(FieldRequest r);
}
