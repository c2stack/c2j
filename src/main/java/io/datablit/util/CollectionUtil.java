package io.datablit.util;

import java.util.Arrays;

/**
 *
 */
public class CollectionUtil {
    public static final String[] strArrayAppend(String[] strlist, String s) {
        if (strlist == null) {
            return new String[] { s };
        }
        String[] strlistNew = Arrays.copyOf(strlist, strlist.length + 1);
        strlistNew[strlist.length - 1] = s;
        return strlist;
    }

    public static final int[] intArrayAppend(int[] intlist, int n) {
        if (intlist == null) {
            return new int[] { n };
        }
        int[] intlistNew = Arrays.copyOf(intlist, intlist.length + 1);
        intlistNew[intlistNew.length - 1] = n;
        return intlistNew;
    }
}
