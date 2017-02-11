package org.c2stack.meta;

/**
 *
 */
public interface MetaCollection extends Meta {
    public Meta getFirstMeta();
    public void addMeta(Meta m);
    public void clear();
}
