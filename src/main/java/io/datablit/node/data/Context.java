package org.conf2.data;

import org.conf2.schema.MetaCollection;

/**
 *
 */
public class Context {

    public Selector select(MetaCollection m, Node n) {
        return new Selector(new Selection(n, m));
    }
}
