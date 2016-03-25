package io.datablit.node;

import io.datablit.meta.*;

import java.util.Iterator;

/**
 *
 */
public class Selection {
    Selection parent;
    EventDispatch dispatch;
    Node node;
    Path path;
    boolean insideList;

    private Selection() {
    }

    public Selection(Browser b) {
        this(b.getNode(), b.getModule());
    }

    public Selection(Node node, MetaCollection meta) {
        this.dispatch = new EventDispatch();
        this.path = new Path(null, meta);
        this.node = node;
    }

    public Selection select(Node node, Value[] key) {
        Selection s = new Selection(node, this.path.getMeta());

        // node items parent is node's parent, not the node itself!
        Path parentPath = null;
        if (this.parent != null) {
            parentPath = this.parent.path;
        }

        s.path = new Path(parentPath, (MetaList)this.path.getMeta(), key);
        s.parent = this;
        s.dispatch = this.dispatch;
        s.insideList = true;
        s.node = node;
        return s;
    }

    public Selection select(MetaCollection meta, Node node) {
        Selection s = new Selection(node, meta);
        s.path = new Path(this.path, meta);
        s.parent = this;
        s.dispatch = this.dispatch;
        return s;
    }

    public Path getPath() {
        return path;
    }

    public void fire(Event e) {
        Selection target = this;
        while (target != null) {
            target.node.event(target, e);
            if (e.type.isBubble() && ! e.propagationStopped) {
                target = target.parent;
            } else {
                break;
            }
        }
        dispatch.fire(path, e);
    }

    public Selection copy(Node n) {
        Selection copy = new Selection();
        copy.parent = parent;
        copy.dispatch = dispatch;
        copy.node = n;
        copy.path = path;
        copy.insideList = insideList;
        return copy;
    }

    public Selector selector() {
        return new Selector(this);
    }

    public void walk(Context context, WalkController controller) {
        if (MetaUtil.isList(path.meta) && ! insideList) {
            ListRequest r = new ListRequest();
            r.context = context;
            r.selection = this;
            r.isFirst = true;
            Selection next = controller.visitList(r);
            if (next != null) {
                next.walk(context, controller);
                next.fire(EventType.LEAVE.create());
                r.isFirst = false;
                next = controller.visitList(r);
            }
        } else {
            Iterator<Meta> i = controller.containerIterator(this);
            while (i.hasNext()) {
                Meta m = i.next();
                if (m instanceof Choice) {
                    Choice c = (Choice) m;
                    m = this.node.choose(this, c);
                }
                if (MetaUtil.isLeaf(m)) {
                    // not interested in results for a walk but we still
                    // call it so interceptors like edit can use the value
                    FieldRequest r = new FieldRequest();
                    r.selection = this;
                    r.meta = (HasDataType) m;
                    r.context = context;
                    controller.visitField(r);
                } else {
                    if (m instanceof Rpc) {
                        ActionRequest r = new ActionRequest();
                        r.selection = this;
                        r.meta = (Rpc) m;
                        r.context = context;
                        controller.visitAction(r);
                    } else {
                        ContainerRequest r = new ContainerRequest();
                        r.selection = this;
                        r.meta = (MetaCollection) m;
                        r.context = context;
                        Selection child = controller.visitContainer(r);
                        if (child != null) {
                            child.walk(context, controller);
                            child.fire(EventType.LEAVE.create());
                        }
                    }
                }
            }
        }
    }

    public Selection Action(Node node) {
        throw new RuntimeException("todo");
    }
}
