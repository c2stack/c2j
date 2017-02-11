package org.c2stack.meta;

/**
 *
 */
public class Module extends CollectionBase implements HasGroupings, HasTypedefs, HasActions, HasNotifications, Describable {
    private String namespace;
    private String prefix;
    public Revision revision;
    private LinkedListCollection groupings;
    private LinkedListCollection typedefs;
    private LinkedListCollection notifications;
    private LinkedListCollection rpcs;

    public Module(String ident) {
        super(ident);
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public void addMeta(Meta m) {
        Class c = m.getClass();
        if (c == Grouping.class) {
            if (groupings == null) {
                groupings = new LinkedListCollection("groupings", this);
            }
            groupings.addMeta(m);
        } else if (c == Typedef.class) {
            if (typedefs == null) {
                typedefs = new LinkedListCollection("typedefs", this);
            }
            typedefs.addMeta(m);
        } else if (c == Notification.class) {
            if (notifications == null) {
                notifications = new LinkedListCollection("notifications", this);
            }
            notifications.addMeta(m);
        } else if (c == Rpc.class) {
            if (rpcs == null) {
                rpcs = new LinkedListCollection("rpcs", this);
            }
            rpcs.addMeta(m);
        } else {
            super.addMeta(m);
        }
    }

    public MetaCollection getRpcs() {
        return rpcs;
    }

    public MetaCollection getNotifications() {
        return notifications;
    }

    public MetaCollection getTypedefs() {
        return typedefs;
    }

    public MetaCollection getGroupings() {
        return groupings;
    }
}
