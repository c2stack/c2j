package org.conf2.data;

import org.conf2.schema.Meta;
import org.conf2.schema.MetaCollection;
import org.conf2.schema.MetaList;
import org.conf2.schema.MetaUtil;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PathSlice {
    private Path head;
    private Path tail;

    public Path getHead() {
        return head;
    }

    public Path getTail() {
        return tail;
    }

    public PathSlice(URL url, MetaCollection meta) {
        try {
            Path parent = new Path(meta);
            String[] segments = url.getPath().split("/");
            for (String segment : segments) {
                if (segment.length() == 0) {
                    break;
                }
                int eq = segment.indexOf('=');
                String ident;
                String[] keyStrs = null;
                if (eq > 0) {
                    ident = URLDecoder.decode(segment.substring(0, eq), "UTF-8");
                    keyStrs = segment.substring(eq + 1).split(",");
                    for (int i = 0; i < keyStrs.length; i++) {
                        keyStrs[i] = URLDecoder.decode(keyStrs[i], "UTF-8");
                    }
                } else {
                    ident = URLDecoder.decode(segment, "UTF-8");
                }
                Meta m = MetaUtil.findByIdent(meta, ident);
                if (m == null) {
                    throw new ParseUrlException(ident + " not found in " + meta.getIdent(), 404);
                }
                Path child;
                if (keyStrs != null) {
                    MetaList listMeta = (MetaList)meta;
                    child = new Path(parent, listMeta, Value.coerseKey(listMeta, keyStrs));
                } else {
                    child = new Path(parent, (MetaCollection)m);
                }
                append(child);
                parent = child;
            }
        } catch (UnsupportedEncodingException ignore) {
        }
        throw new RuntimeException();
    }

    public void append(Path child) {
        if (tail == null) {
            head = child;
            tail = child;
        } else {
            child.parent = tail;
            tail = child;
        }
    }
}
