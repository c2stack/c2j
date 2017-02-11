package org.c2stack.node;

/**
 *
 */
public interface ListPostConstraint {
    public boolean checkListPostConstraints(ListRequest r, Selection child, Value[] key);
}
