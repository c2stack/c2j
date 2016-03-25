package io.datablit.node;

import io.datablit.meta.Meta;
import io.datablit.meta.MetaCollection;
import io.datablit.meta.Rpc;

import java.util.Iterator;

/**
 *
 */
public class FindTargetWalk implements WalkController {
    private PathSlice target;
    private Selection found;

    public FindTargetWalk(PathSlice target) {
    }

    public Selection getFoundTarget() {
        return found;
    }

    @Override
    public Selection visitList(ListRequest r) {
        return null;
    }

    @Override
    public Iterator<Meta> containerIterator(Selection sel) {
        return null;
    }

    @Override
    public Selection visitContainer(ContainerRequest r) {
        return null;
    }

    @Override
    public Selection visitAction(ActionRequest r) {
        return null;
    }

    @Override
    public Value visitField(FieldRequest r) {
        return null;
    }
}
