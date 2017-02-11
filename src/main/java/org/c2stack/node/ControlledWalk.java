package org.c2stack.node;

import org.c2stack.meta.*;

import java.util.Iterator;

public class ControlledWalk implements WalkController {
    private Constraints constraints;

    public ControlledWalk(Constraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public Selection visitList(ListRequest r) {
        if (constraints != null) {
            r.constraints = constraints;
            if (!r.constraints.checkListPreConstraints(r)) {
                return null;
            }
        }
        Selection child = null;
        NodeKeyPair next = r.selection.node.next(r);
        Value[] key = null;
        if (next != null) {
            child = r.selection.select(next.node, next.key);
            key = next.key;
        }

        if (constraints != null) {
            if (!r.constraints.checkListPostConstraints(r, child, key)) {
                return null;
            }
        }
        return child;
    }

    @Override
    public Iterator<Meta> containerIterator(Selection sel) {
        return new MetaCollectionIterator(sel.path.meta, true);
    }

    @Override
    public Selection visitContainer(ContainerRequest r) {
        if (constraints != null) {
            r.constraints = constraints;
            if (!r.constraints.checkContainerPreConstraints(r)) {
                return null;
            }
        }

        Selection child = null;
        Node childNode = r.selection.node.select(r);
        if (childNode != null) {
            child = r.selection.select(r.meta, childNode);
        }

        if (constraints != null) {
            r.constraints = constraints;
            if (!r.constraints.checkContainerPostConstraints(r, child)) {
                return null;
            }
        }

        return child;
    }

    @Override
    public Selection visitAction(ActionRequest r) {
        return null;
    }

    @Override
    public Value visitField(FieldRequest r) {
        if (constraints != null) {
            r.constraints = constraints;
            if (!r.constraints.checkFieldPreConstraints(r)) {
                return null;
            }
        }
        return r.selection.node.read(r);
    }
}
