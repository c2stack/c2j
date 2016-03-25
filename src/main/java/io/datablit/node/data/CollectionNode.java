package org.conf2.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CollectionNode {

    public static Node container(Map<String, Object> data) {
        MyNode n = new MyNode();
        return n;
    }

    public static Node list(List<Map<String, Object>> data) {
        MyNode n = new MyNode();
        return n;
    }

    public static List<Map<String, Object>> singleton(String ident, Object data) {
        List<Map<String, Object>> singleton = new ArrayList<Map<String, Object>>(1);
        singleton.add(new HashMap<String, Object>());
        return singleton;
    }
}
