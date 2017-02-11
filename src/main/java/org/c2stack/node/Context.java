package org.c2stack.node;

import org.c2stack.meta.MetaCollection;

/**
 *
 */
public class Context {

    public Selector select(MetaCollection m, Node n) {
        return new Selector(new Selection(n, m));
    }
}
