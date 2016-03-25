package io.datablit.node;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Selector {
    private Context context;
    private Selection selection;
    private Constraints constraints;
    private PathSlice target;

    public Selector(Selection selection) {
        this.selection = selection;
    }

    public Selection getSelection() {
        return this.selection;
    }

    public Selector insertFrom(Node src) {
        editor(selection.copy(src), selection).edit(this.context, Editor.Strategy.INSERT);
        return this;
    }

    public Selector insertInto(Node dest) {
        editor(selection, selection.copy(dest)).edit(this.context, Editor.Strategy.INSERT);
        return this;
    }

    public Selector upsertFrom(Node src) {
        editor(selection.copy(src), selection).edit(this.context, Editor.Strategy.UPSERT);
        return this;
    }

    public Selector upsertInto(Node dest) {
        editor(selection, selection.copy(dest)).edit(this.context, Editor.Strategy.UPSERT);
        return this;
    }

    public Selector updateFrom(Node src) {
        editor(selection.copy(src), selection).edit(this.context, Editor.Strategy.UPDATE);
        return this;
    }

    public Selector updateInto(Node dest) {
        editor(selection, selection.copy(dest)).edit(this.context, Editor.Strategy.UPDATE);
        return this;
    }

    private Editor editor(Selection from, Selection to) {
        return new Editor(from, to, new ControlledWalk(this.constraints));
    }

    public Selector Find(String path) {
        Selector s = this;
        if (path.startsWith("../")) {
            Selection sel = this.selection;
            while (path.startsWith("../")) {
                sel = sel.parent;
                path = path.substring(3);
            }
            s = new Selector(sel);
        }
        try {
            URL url = new URL(path);
            return s.Find(url);
        } catch (MalformedURLException e) {
            throw new ParseUrlException(e.getMessage());
        }
    }

    public Selector Find(URL path) {
        Selector copy = new Selector(selection);
        copy.target = new PathSlice(path, selection.path.meta);
        Map<String, String[]> params = SelectorUtil.getQueryMap(path.getQuery());
        if (params != null && params.size() > 0) {
            copy.constraints = buildConstraints(params);
        }
        FindTargetWalk findTarget = new FindTargetWalk(copy.target);
        selection.walk(context, findTarget);
        copy.selection = findTarget.getFoundTarget();
        return copy;
    }

    public Constraints buildConstraints(Map<String, String[]> params) {
        Constraints c = new Constraints(constraints);

        String[] depth = params.get("depth");
        if (depth != null && depth.length == 1) {
            int initialDepth = selection.path.length();
            int maxDepth = Integer.parseInt(depth[0]);
            c.addConstraint("depth", 10, 50, new MaxDepth(initialDepth, maxDepth));
        }

        return c;
    }
}

class SelectorUtil {

    static Map<String, String[]> getQueryMap(String query) {
        try {
            String[] params = query.split("&");
            Map<String, String[]> map = new HashMap<String, String[]>();
            for (String param : params) {
                String[] split = param.split("=");
                String name = URLDecoder.decode(split[0], "UTF-8");
                String value = null;
                if (split.length > 1) {
                    value = URLDecoder.decode(split[1], "UTF-8");
                }
                String[] values = map.get(name);
                if (values != null) {
                    if (value != null) {
                        map.put(name, appendString(values, value));
                    }
                } else {
                    map.put(name, new String[] {value});
                }
            }
            return map;
        } catch (UnsupportedEncodingException ignore) {
        }
        throw new RuntimeException();
    }

    static String[] appendString(String[] orig, String s) {
        String[] larger = new String[orig.length + 1];
        System.arraycopy(orig, 0, larger, 0, orig.length);
        larger[larger.length - 1] = s;
        return larger;
    }
}
