package org.conf2.data;

import org.conf2.schema.Choice;
import org.conf2.schema.Meta;

public interface Node {
    Node select(ContainerRequest r);
    Value read(FieldRequest r);
    NodeKeyPair next(ListRequest r);
    void write(FieldRequest r, Value v);
    void event(Selection sel, Event e);
    Meta choose(Selection sel, Choice choice);
}
