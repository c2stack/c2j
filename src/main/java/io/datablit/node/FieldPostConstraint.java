package io.datablit.node;

/**
 *
 */
public interface FieldPostConstraint {
    public boolean checkFieldPostConstraints(FieldRequest r, Value v);
}
