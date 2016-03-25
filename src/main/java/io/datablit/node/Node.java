package io.datablit.node;

import io.datablit.meta.Choice;
import io.datablit.meta.Meta;

public interface Node {
    Node select(ContainerRequest r);
    Value read(FieldRequest r);
    NodeKeyPair next(ListRequest r);
    void write(FieldRequest r, Value v);
    void event(Selection sel, Event e);
    Meta choose(Selection sel, Choice choice);
}
