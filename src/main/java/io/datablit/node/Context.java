package io.datablit.node;

import io.datablit.meta.MetaCollection;

/**
 *
 */
public class Context {

    public Selector select(MetaCollection m, Node n) {
        return new Selector(new Selection(n, m));
    }
}
