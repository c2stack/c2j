package org.c2stack.node;

public class MaxDepth implements ContainerPreConstraint {
    private int initialDepth;
    private int maxDepth;

    public MaxDepth(int initialDepth, int maxDepth) {
        this.initialDepth = initialDepth;
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean checkContainerPreConstraints(ContainerRequest r) {
        int depth = r.selection.path.length() + 1;
        if (depth - initialDepth >= maxDepth) {
            return false;
        }
        return true;
    }
}
