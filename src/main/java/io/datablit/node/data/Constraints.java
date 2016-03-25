package org.conf2.data;

import java.util.*;

/**
 *
 */
public class Constraints implements FieldPostConstraint, FieldPreConstraint, ContainerPreConstraint, ContainerPostConstraint, ListPostConstraint, ListPreConstraint {
    public static class Entry implements Comparator<Entry> {
        String ident;
        int weight;
        int priority;
        Object constraint;
        FieldPreConstraint prefield;
        FieldPostConstraint postfield;
        ContainerPostConstraint postcont;
        ContainerPreConstraint precont;
        ListPostConstraint postlist;
        ListPreConstraint prelist;

        @Override
        public int compare(Entry o1, Entry o2) {
            return 0;
        }
    }
    private Map<String, Entry> entries;
    private Entry[] compiled;

    public Constraints() {
        entries = new HashMap<String, Entry>();
    }

    public Constraints(Constraints parent) {
        entries = new HashMap<String, Entry>(parent.entries);
    }

    private Entry[] getCompiled() {
        if (compiled == null) {
            Entry[] c = new Entry[entries.size()];
            Iterator<Entry> e = entries.values().iterator();
            for (int i = 0; i < c.length; i++) {
                c[i] = e.next();
            }
            Arrays.sort(c);
            compiled = c;
        }
        return compiled;
    }

    public void addConstraint(String ident, int weight, int priority, Object constraint) {
        boolean oneMatch = false;
        Entry e = new Entry();
        e.ident = ident;
        e.weight = weight;
        e.priority = priority;
        if (constraint instanceof ListPreConstraint) {
            oneMatch = true;
            e.prelist = (ListPreConstraint) constraint;
        }
        if (constraint instanceof ListPostConstraint) {
            oneMatch = true;
            e.postlist = (ListPostConstraint) constraint;
        }
        if (constraint instanceof ContainerPostConstraint) {
            oneMatch = true;
            e.postcont = (ContainerPostConstraint) constraint;
        }
        if (constraint instanceof ContainerPreConstraint) {
            oneMatch = true;
            e.precont = (ContainerPreConstraint) constraint;
        }
        if (constraint instanceof FieldPreConstraint) {
            oneMatch = true;
            e.prefield = (FieldPreConstraint) constraint;
        }
        if (constraint instanceof FieldPostConstraint) {
            oneMatch = true;
            e.postfield = (FieldPostConstraint) constraint;
        }
        entries.put(ident, e);
        compiled = null;
        if (!oneMatch) {
            throw new IllegalArgumentException("Constraint didn't implement any constaint interfaces");
        }
    }

    @Override
    public boolean checkContainerPreConstraints(ContainerRequest r) {
        return false;
    }

    @Override
    public boolean checkContainerPostConstraints(ContainerRequest r, Selection child) {
        return false;
    }

    @Override
    public boolean checkFieldPostConstraints(FieldRequest r, Value val) {
        return false;
    }

    @Override
    public boolean checkFieldPreConstraints(FieldRequest r) {
        return false;
    }

    @Override
    public boolean checkListPostConstraints(ListRequest r, Selection child, Value[] key) {
        return false;
    }

    @Override
    public boolean checkListPreConstraints(ListRequest r) {
        return false;
    }
}
