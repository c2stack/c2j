package io.datablit.node;

/**
 *
 */
public interface ContainerPostConstraint {
    public boolean checkContainerPostConstraints(ContainerRequest r, Selection child);
}
