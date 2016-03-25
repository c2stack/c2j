package io.datablit.node;

/**
 *
 */
public interface ListPostConstraint {
    public boolean checkListPostConstraints(ListRequest r, Selection child, Value[] key);
}
