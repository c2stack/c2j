package org.c2stack.node;

/**
 *
 */
public interface ContainerPostConstraint {
    public boolean checkContainerPostConstraints(ContainerRequest r, Selection child);
}
