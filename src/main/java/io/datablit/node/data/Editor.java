package org.conf2.data;

import org.conf2.schema.Choice;
import org.conf2.schema.MetaList;

public class Editor {
    private Selection from;
    private Selection to;
    private WalkController controller;

    public enum Strategy {
        INSERT,
        UPSERT,
        UPDATE
    }

    public Editor(Selection from, Selection to, WalkController controller) {
        this.from = from;
        this.to = to;
        this.controller = controller;
    }

    void edit(Context context, Strategy strategy) {
        Node n = null;
        if ((from.path.meta instanceof MetaList) && !from.insideList) {
            n = list(from, to, false, strategy);
        } else {
            n = container(from, to, false, strategy);
        }
        Selection s = from.copy(n);
        to.fire(EventType.START_TREE_EDIT.create());
        s.walk(context, controller);
        to.fire(EventType.LEAVE_EDIT.create());
        to.fire(EventType.END_TREE_EDIT.create());
    }

    private Node container(Selection from, Selection to, boolean create, Strategy strategy) {
        MyNode n = new MyNode();
        final boolean[] created = new boolean[1];
        n.onChoose = (Selection sel, Choice choice) -> {
            return from.node.choose(sel, choice);
        };
        n.onSelect = (ContainerRequest r) -> {
            created[0] = false;
            ContainerRequest fromRequest = r.copy();
            fromRequest.create = false;
            fromRequest.selection = from;
            Node fromChildNode = from.node.select(fromRequest);
            if (fromChildNode == null) {
                return null;
            }
            ContainerRequest toRequest = r.copy();
            toRequest.create = false;
            toRequest.selection = to;
            Node toChildNode = to.node.select(toRequest);
            toRequest.create = true;
            switch (strategy) {
                case INSERT:
                    if (toChildNode != null) {
                        String msg = String.format("Duplicate '%s' found in '%s'", r.meta.getIdent(), r.selection.toString());
                        throw new EditException(msg, 409);
                    }
                    toChildNode = to.node.select(toRequest);
                    created[0] = true;
                    break;
                case UPSERT:
                    if (toChildNode == null) {
                        toChildNode = to.node.select(toRequest);
                        created[0] = true;
                    }
                    break;
                case UPDATE:
                    if (toChildNode == null) {
                        String msg = String.format("'%v' not found in '%s' node node ", r.meta.getIdent(), r.selection.toString());
                        throw new EditException(msg, 404);
                    }
                    break;
            }

            if (toChildNode == null) {
                throw new EditException("Could not create destination container node " + r.selection.toString());
            }

            Selection fromChild = from.select(r.meta, fromChildNode);
            Selection toChild = from.select(r.meta, toChildNode);
            if (r.meta instanceof MetaList) {
                return list(fromChild, toChild, created[0], Strategy.UPSERT);
            }
            return container(fromChild, toChild, created[0], Strategy.UPSERT);
        };
        n.onEvent = (Selection sel, Event e) -> {
            handleEvent(sel, from, to, created[0], e);
        };
        n.onRead = (FieldRequest r) -> {
            Value v = from.node.read(r);
            if (v == null && strategy == Strategy.UPDATE) {
                if (r.meta.getDataType().hasDefault()) {
                    v = Value.coerse(r.meta.getDataType(), r.meta.getDataType().getDefault());
                }
            }
            if (v != null) {
                v.valType = r.meta.getDataType().getValType();
                to.node.write(r, v);
            }
            return v;
        };
        return n;
    }

    private Node list(Selection from, Selection to, boolean create, Strategy strategy) {
        MyNode n = new MyNode();
        final boolean[] created = new boolean[1];
        n.onNext = (ListRequest r) -> {
            created[0] = false;
            ListRequest fromRequest = r.copy();
            fromRequest.create = false;
            fromRequest.selection = from;
            NodeKeyPair fromNextNode = from.node.next(fromRequest);
            if (fromNextNode == null) {
                return null;
            }

            ListRequest toRequest = r.copy();
            NodeKeyPair toNextNode = null;
            if (r.key != null && r.key.length > 0) {
                toRequest.key = r.key;
                toRequest.create = false;
                toRequest.selection = to;
                toNextNode = to.node.next(toRequest);
            }

            toRequest.create = true;
            switch (strategy) {
                case UPDATE:
                    if (toNextNode == null) {
                        String keyStr = Value.toString(toRequest.key);
                        String msg = String.format("'%v' not found in '%s' node node ", keyStr, r.selection.toString());
                        throw new EditException(msg, 404);
                    }
                    break;
                case UPSERT:
                    if (toNextNode == null) {
                        toNextNode = to.node.next(toRequest);
                        created[0] = true;
                    }
                    break;
                case INSERT:
                    if (toNextNode != null) {
                        String keyStr = Value.toString(toRequest.key);
                        String msg = String.format("Duplicate item found in %s with key %s", r.selection.toString(), keyStr);
                        throw new EditException(msg, 409);
                    }
                    toNextNode = to.node.next(toRequest);
                    created[0] = true;
                    break;
            }

            if (toNextNode == null) {
                throw new EditException("Could not create destination node node " + r.selection.toString());
            }

            Selection fromChild = from.select(fromNextNode.node, fromNextNode.key);
            Selection toChild = from.select(toNextNode.node, fromNextNode.key);
            return new NodeKeyPair(container(fromChild, toChild, created[0], Strategy.UPSERT), fromNextNode.key);
        };
        n.onEvent = (Selection sel, Event e) -> {
            handleEvent(sel, from, to, created[0], e);
        };
        return n;
    }

    void handleEvent(Selection sel, Selection from, Selection to, boolean created, Event e) {
        if (e.type == EventType.LEAVE) {
            if (created) {
                to.fire(EventType.NEW.create());
            }
            to.fire(EventType.LEAVE_EDIT.create());
        }
        to.node.event(sel, e);
        from.node.event(sel, e);
    }
}
