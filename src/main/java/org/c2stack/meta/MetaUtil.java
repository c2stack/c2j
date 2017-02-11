package org.c2stack.meta;

import java.util.Iterator;

/**
 *
 */
public class MetaUtil {

    public static boolean empty(MetaCollection c) {
        return new MetaCollectionIterator(c, true).hasNext();
    }

    public static Meta findByPath(MetaCollection c, String path) {
        return findByPathImpl(c, path, false);

    }

    public static Meta findByPathResolveProxies(MetaCollection c, String path) {
        return findByPathImpl(c, path, true);
    }

    private static Meta findByPathImpl(MetaCollection c, String path, boolean resolve) {
        if (path.startsWith("../")) {
            return findByPathImpl(c.getParent(), path.substring(3), resolve);
        }
        String[] elems = path.split("/");
        int lastLevel = elems.length - 1;
        Iterator<Meta> i = new MetaCollectionIterator(c, resolve);
        Meta found = null;
        for (int level = 0; level < lastLevel; level++) {
            found = MetaUtil.findByIdent(i, elems[level]);
            if (found == null) {
                return null;
            }
            if (level < lastLevel) {
                i = new MetaCollectionIterator((MetaCollection)found, resolve);
            }
        }
        return found;
    }

    public static Meta findByIdent(Iterator<Meta> i, String ident) {
        while (i.hasNext()) {
            Meta next = i.next();
            if (next.getIdent().equals(ident)) {
                return next;
            }
        }
        return null;
    }

    public static Meta findByIdent(MetaCollection c, String ident) {
        return findByIdent(new MetaCollectionIterator(c), ident);
    }

    public static Meta findByIdentResolveProxies(MetaCollection c, String ident) {
        return findByIdent(new MetaCollectionIterator(c, true), ident);
    }

    public static int collectionLength(MetaCollection c) {
        int len = 0;
        Iterator<Meta> i = new MetaCollectionIterator(c);
        while (i.hasNext()) {
            len++;
            i.next();
        }
        return len;
    }

    public static boolean isLeaf(Meta m) {
        return m instanceof Leaf || m instanceof LeafList;
    }

    public static boolean isList(Meta m) {
        return m instanceof MetaList;
    }
}
