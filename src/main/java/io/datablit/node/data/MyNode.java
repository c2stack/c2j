package org.conf2.data;

import org.conf2.CodedError;
import org.conf2.schema.*;

/**
 *
 */
public class MyNode implements Node {
    public OnSelect onSelect;
    public OnRead onRead;
    public OnNext onNext;
    public OnWrite onWrite;
    public OnChoose onChoose;
    public OnEvent onEvent;

    @Override
    public Node select(ContainerRequest r) {
        if (onSelect == null) {
            throw new CodedError("select not implemented on " + r.selection.toString(), 501);
        }
        return onSelect.select(r);
    }

    @Override
    public Value read(FieldRequest r) {
        if (onRead == null) {
            throw new CodedError("read not implemented on " + r.selection.toString(), 501);
        }
        return onRead.read(r);
    }

    @Override
    public NodeKeyPair next(ListRequest r) {
        if (onNext == null) {
            throw new CodedError("next not implemented on " + r.selection.toString(), 501);
        }
        return onNext.next(r);
    }

    @Override
    public void write(FieldRequest r, Value v) {
        if (onWrite == null) {
            throw new CodedError("write not implemented on " + r.selection.toString(), 501);
        }
        onWrite.write(r, v);
    }

    @Override
    public void event(Selection sel, Event e) {
        if (onEvent != null) {
            onEvent.event(sel, e);
        }
    }

    @Override
    public Meta choose(Selection sel, Choice choice) {
        if (onChoose == null) {
            throw new CodedError("choose not implemented on " + sel.toString(), 501);
        }
        return onChoose.choose(sel, choice);
    }
}
