package org.conf2.data;

import org.conf2.CodedError;
import org.conf2.schema.DataType;
import org.conf2.schema.HasDataType;
import org.conf2.schema.MetaList;
import org.conf2.schema.ValueType;
import org.conf2.util.CollectionUtil;

import java.lang.reflect.Array;
import java.util.List;

/**
 *
 */
public class Value {
    public ValueType valType = ValueType.EMPTY;
    public String str;
    public boolean bool;
    public int int32;
    public String[] strlist;
    public boolean[] boollist;
    public int[] int32list;

    public static Value Str(String v) {
        Value bv = new Value();
        bv.str = v;
        bv.valType = ValueType.STRING;
        return bv;
    }

    public static Value Int32(int v) {
        Value bv = new Value();
        bv.int32 = v;
        bv.valType = ValueType.INT32;
        return bv;
    }

    public static Value Bool(boolean v) {
        Value bv = new Value();
        bv.bool = v;
        bv.valType = ValueType.BOOLEAN;
        return bv;
    }

    public static Value Enum(int n, String s) {
        Value bv = new Value();
        bv.int32 = n;
        bv.str = s;
        bv.valType = ValueType.ENUMERATION;
        return bv;
    }

    public void setEnum(DataType type, int n) {
        valType = ValueType.ENUMERATION;
        int32 = n;
        str = type.enumeration[n];
    }

    public void setEnum(DataType type, String enumLabel) {
        valType = ValueType.ENUMERATION;
        for (int n= 0; n < type.enumeration.length; n++) {
            if (type.enumeration[n].equals(enumLabel)) {
                int32 = n;
                str = type.enumeration[n];
                return;
            }
        }
    }

    public void setEnumList(DataType type, int[] enumIds) {
        int32list = enumIds;
        strlist = new String[enumIds.length];
        for (int i = 0; i < enumIds.length; i++) {
            strlist[i] = type.enumeration[int32list[i]];
        }
    }

    public void addEnum(DataType type, int n) {
        valType = ValueType.ENUMERATION_LIST;
        strlist = CollectionUtil.strArrayAppend(strlist, type.enumeration[n]);
        int32list = CollectionUtil.intArrayAppend(int32list, n);
    }

    public int listLen() {
        switch (valType) {
            case ENUMERATION_LIST:
            case INT32_LIST:
                return int32list.length;
            case STRING_LIST:
                return strlist.length;
            case BOOLEAN_LIST:
                return boollist.length;
        }
        return 0;
    }

    public static Value coerse(DataType t, Object o) {
        Value v = new Value();
        v.valType = t.getValType();
        Class c = o.getClass();
        if (t.getValType().isList()) {
            int arrayLen = 0;
            Object array = null;
            if (o instanceof Array) {
                arrayLen = Array.getLength(o);
            } else if (o instanceof List) {
                arrayLen = ((List)o).size();
            } else {
                throw new CodedError("Unsupposed coersion " + o.toString(), 501);
            }
            switch (t.getValType()) {
                case BINARY_LIST:
                    v.boollist = new boolean[arrayLen];
                    array = v.boollist;
                case ENUMERATION_LIST:
                case INT32_LIST:
                    v.int32list = new int[arrayLen];
                    array = v.int32list;
                default:
                    v.str = o.toString();
            }
            DataType itemType = new DataType(null, t.getValType().itemVersion.ident);
            Value surrogate = new Value();
            if (o instanceof Array) {
                for (int i = 0; i < arrayLen; i++) {
                    coerseValue(surrogate, itemType, Array.get(o, i));
                    Array.set(array, i, surrogate.getValue());
                }
            } else if (o instanceof List) {
                List l = (List)o;
                for (int i = 0; i < arrayLen; i++) {
                    coerseValue(surrogate, itemType, l.get(i));
                    Array.set(array, i, surrogate.getValue());
                }
            } else {
                throw new CodedError("Unsupposed coersion " + o.toString(), 501);
            }
            if (t.getValType() == ValueType.ENUMERATION_LIST) {
                v.setEnumList(t, v.int32list);
            }
        } else {
            coerseValue(v, t, o);
        }
        return v;
    }

    private static void coerseValue(Value v, DataType t, Object o) {
        switch (t.getValType()) {
            case BOOLEAN:
                v.bool = Coerse.toBool(o);
                return;
            case INT32:
                v.int32 = Coerse.toInt(o);
                return;
            case ENUMERATION:
                try {
                    v.setEnum(t, Coerse.toInt(o));
                } catch (NumberFormatException nan) {
                    v.setEnum(t, o.toString());
                }
                return;
            default:
                v.str = o.toString();
                return;

        }
    }

    public static Value[] coerseKey(MetaList meta, String[] keyStrs) {
        if (keyStrs == null || keyStrs.length == 0) {
            return null;
        }
        HasDataType[] keyMeta = meta.getKey();
        if (keyMeta.length != keyStrs.length) {
            throw new RuntimeException(String.format("Wrong number of keys. Got %d expected %d",
                    keyStrs.length, keyMeta.length));
        }
        Value[] key = new Value[keyStrs.length];
        for (int i = 0; i < key.length; i++) {
            coerseValue(key[i], keyMeta[i].getDataType(), keyStrs[i]);
        }
        return key;
    }

    public static String toString(Value[] values) {
        StringBuilder b = new StringBuilder();
        for (Value v : values) {
            if (b.length() != 0) {
                b.append(", ");
            }
            b.append(v.toString());
        }
        return b.toString();
    }

    public Object getValue() {
        switch (valType) {
            case BOOLEAN:
                return bool;
            case INT32:
                return int32;
            case ENUMERATION:
            case STRING:
                return str;
            case BINARY_LIST:
                return boollist;
            case ENUMERATION_LIST:
            case STRING_LIST:
                return strlist;
        }
        throw new RuntimeException(valType.toString());
    }
}


class Coerse {

    static boolean toBool(Object o) {
        if (o instanceof Boolean) {
            return (Boolean)o;
        } else if (o instanceof Number) {
            return (((Number) o).longValue()) != 0;
        } else if (o instanceof String) {
            return o.equals("true");
        }
        throw new CodedError("Unsupposed boolean coersion " + o.toString(), 501);
    }

    static int toInt(Object o) {
        if (o instanceof Number) {
            return ((Number) o).intValue();
        } else if (o instanceof String) {
            return Integer.parseInt((String)o);
        }
        throw new CodedError("Unsupposed int coersion " + o.toString(), 501);
    }
}