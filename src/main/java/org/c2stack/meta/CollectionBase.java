package org.c2stack.meta;

/**
 *
 */
public abstract class CollectionBase extends MetaBase implements MetaCollection {
    private LinkedList children = new LinkedList(this);

    public CollectionBase(String ident) {
        super(ident);
    }

    @Override
    public Meta getFirstMeta() {
        return children.getFirstMeta();
    }

    @Override
    public void addMeta(Meta m) {
        children.addMeta(m);
    }

    @Override
    public void clear() {
        children.clear();
    }
}
