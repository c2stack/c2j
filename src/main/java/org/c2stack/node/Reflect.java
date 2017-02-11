package org.c2stack.node;

import org.c2stack.util.CodedError;
import org.c2stack.meta.*;

import java.lang.reflect.*;
import java.util.*;

/**
 *
 */
public class Reflect {
    public Reflect parent;
    public Class c;
    public Meta meta;
    public Method setter;
    public Field setField;
    public Method getter;
    public Field getField;
    private Map<Meta, Reflect> properties;

    private Reflect(Reflect parent) {
        this.parent = parent;
    }

    public Reflect(Class c) {
        this.c = c;
        this.properties = new HashMap<Meta, Reflect>();
    }

    public Reflect property(Meta property) {
        Reflect r = properties.get(property);
        if (r == null) {
            if (MetaUtil.isList(property)) {
                r = buildLeafReflection((HasDataType) property);
            } else if (MetaUtil.isLeaf(property)) {
                r = buildListReflection((MetaList) property);
            } else {
                r = buildContainerReflection((MetaCollection) property);
            }
            properties.put(property, r);
        }
        return r;
    }

    public Object get(Object parent) {
        try {
            if (getter != null) {
                return getter.invoke(parent);
            } else if (getField != null) {
                return setField.get(parent);
            } else {
                throw new RuntimeException(String.format("No getter for %s found", this.meta.getIdent()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Value getValue(Object parent) {
        Object o = get(parent);
        if (o != null) {
            return Value.coerse(((HasDataType)meta).getDataType(), o);
        }
        return null;
    }

    public void setValue(Object o, Value v) {
        // TODO: performance, iterate thru

        try {
            switch (v.valType) {
                case INT32_LIST:
                    setter.invoke(o, new Object[] {v.int32list});
                    break;
                case STRING_LIST:
                    setter.invoke(o, new Object[] {v.strlist});
                    break;
                case BOOLEAN_LIST:
                    setter.invoke(o, new Object[] {v.boollist});
                    break;
                case INT32:
                    setter.invoke(o, v.int32);
                    break;
                case STRING:
                    setter.invoke(o, v.str);
                    break;
                case BOOLEAN:
                    setter.invoke(o, v.bool);
                    break;
                default:
                    set(o, v.getValue());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void set(Object parent, Object o) {
        try {
            if (setter != null) {
                setter.invoke(parent, o);
            } else if (setField != null) {
                setField.set(parent, o);
            } else {
                throw new RuntimeException(String.format("No setter for %s found", meta.getIdent()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object newInstance() {
        try {
            if (c != null) {
                return c.newInstance();
            }
            throw new RuntimeException(String.format("No constructor for %s found", meta.getIdent()));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String accessorMethodNameFromMeta(String prefix, String ident) {
        StringBuilder sb = new StringBuilder(prefix.length() + ident.length());
        boolean upper = false;
        if (prefix.length() > 0) {
            upper = true;
            sb.append(prefix);
        }
        for (int i = 0; i < ident.length(); i++) {
            char c = ident.charAt(i);
            if (c == '-') {
                upper = true;
            } else {
                if (upper) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
                upper = false;
            }
        }
        return sb.toString();
    }

    public Reflect buildLeafReflection(HasDataType property) {
        Reflect r = new Reflect(this);
        ValueType vtype = property.getDataType().getValType();
        String setterName = accessorMethodNameFromMeta("set", property.getIdent());
        try {
            switch (vtype) {
                case INT32_LIST:
                    r.c = int[].class;
                    break;
                case STRING_LIST:
                    r.c = String[].class;
                    break;
                case BOOLEAN_LIST:
                    r.c = boolean[].class;
                    break;
                case INT32:
                    r.c = int.class;
                    break;
                case STRING:
                    r.c = String.class;
                    break;
                case BOOLEAN:
                    r.c = boolean.class;
                    break;
                default:
                    throw new CodedError("Format " + vtype + " not supported", 501);
            }
            r.setter = c.getMethod(setterName, r.c);
        } catch (NoSuchMethodException checkField) {
            String fieldName = accessorMethodNameFromMeta("", property.getIdent());
            try {
                r.setField = c.getField(fieldName);
            } catch (NoSuchFieldException noSetterFound) {
            }
        }

        String getterName = accessorMethodNameFromMeta("get", property.getIdent());
        try {
            r.getter = c.getMethod(getterName);
        } catch (NoSuchMethodException checkField) {
            String fieldName = accessorMethodNameFromMeta("", property.getIdent());
            if (r.setField != null) {
                r.getField = r.setField;
            } else {
                try {
                    r.getField = c.getField(fieldName);
                } catch (NoSuchFieldException noSetterFound) {
                }
            }
        }
        return r;
    }

    static Class getPropertyClass(Type t) {
        if (t instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType)t).getActualTypeArguments();
            if (types.length > 0) {
                return types[0].getClass();
            }
        }
        return null;
    }

    Reflect buildListReflection(MetaList property) {
        Reflect r = new Reflect(this);
        String setterName = accessorMethodNameFromMeta("set", property.getIdent());
        try {
            r.setter = c.getMethod(setterName);

            // support List and Array

            r.c = getPropertyClass(r.setter.getGenericParameterTypes()[0]);
        } catch (NoSuchMethodException checkField) {
            String fieldName = accessorMethodNameFromMeta("", property.getIdent());
            try {
                setField = c.getField(fieldName);
                r.c = getPropertyClass(setField.getGenericType());
            } catch (NoSuchFieldException noSetterFound) {
            }
        }

        String getterName = accessorMethodNameFromMeta("get", property.getIdent());
        try {
            getter = c.getMethod(getterName);

        } catch (NoSuchMethodException checkField) {
            String fieldName = accessorMethodNameFromMeta("", property.getIdent());
            if (setField != null) {
                getField = setField;
            } else {
                try {
                    getField = c.getField(fieldName);
                    r.c = getPropertyClass(setField.getGenericType());
                } catch (NoSuchFieldException noSetterFound) {
                }
            }
        }
        properties.put(property, r);
        return r;
    }

    Reflect buildContainerReflection(MetaCollection property) {
        Reflect r = new Reflect(this);
        this.properties = new HashMap<Meta, Reflect>(MetaUtil.collectionLength(property));
        if (r != null) {
            return r;
        }
        r = new Reflect(this);
        String setterName = accessorMethodNameFromMeta("set", property.getIdent());
        try {
            r.setter = c.getMethod(setterName);

            // support List and Array

            r.c = getPropertyClass(r.setter.getGenericParameterTypes()[0]);
        } catch (NoSuchMethodException checkField) {
            String fieldName = accessorMethodNameFromMeta("", property.getIdent());
            try {
                setField = c.getField(fieldName);
                r.c = getPropertyClass(setField.getGenericType());
            } catch (NoSuchFieldException noSetterFound) {
            }
        }

        String getterName = accessorMethodNameFromMeta("get", property.getIdent());
        try {
            getter = c.getMethod(getterName);

        } catch (NoSuchMethodException checkField) {
            String fieldName = accessorMethodNameFromMeta("", property.getIdent());
            if (setField != null) {
                getField = setField;
            } else {
                try {
                    getField = c.getField(fieldName);
                    r.c = getPropertyClass(setField.getGenericType());
                } catch (NoSuchFieldException noSetterFound) {
                }
            }
        }
        properties.put(property, r);
        return r;
    }

    public static final void setterMethod(HasDataType m, Object o, Value v) {
        String methodName = accessorMethodNameFromMeta("set", m.getIdent());
        try {
            Method method;
            switch (v.valType) {
                case INT32_LIST:
                    method = o.getClass().getMethod(methodName, int[].class);
                    method.invoke(o, new Object[] {v.int32list});
                    break;
                case STRING_LIST:
                    method = o.getClass().getMethod(methodName, String[].class);
                    method.invoke(o, new Object[] {v.strlist});
                    break;
                case BOOLEAN_LIST:
                    method = o.getClass().getMethod(methodName, boolean[].class);
                    method.invoke(o, new Object[] {v.boollist});
                    break;
                case INT32:
                    method = o.getClass().getMethod(methodName, int.class);
                    method.invoke(o, v.int32);
                    break;
                case STRING:
                    method = o.getClass().getMethod(methodName, String.class);
                    method.invoke(o, v.str);
                    break;
                case BOOLEAN:
                    method = o.getClass().getMethod(methodName, boolean.class);
                    method.invoke(o, v.bool);
                    break;
                default:
                    throw new CodedError("Format " + v.valType + " not supported", 501);
            }
        } catch (ReflectiveOperationException e) {
            String msg = String.format("Method %s not found on class %s", methodName, o.getClass().getSimpleName());
            throw new MetaError(msg, e);
        }
    }

    public static final void writeObject(Meta m, Object p, Object c) {
        String methodName = accessorMethodNameFromMeta("set", m.getIdent());
        try {
            try {
                Method method = p.getClass().getMethod(methodName, new Class[] {c.getClass()});
                method.invoke(p, c);
            } catch (NoSuchMethodException tryField) {
                try {
                    String fieldName = accessorMethodNameFromMeta("", m.getIdent());
                    Field field = p.getClass().getField(fieldName);
                    field.set(p, c);
                } catch (NoSuchFieldException e) {
                    throw new ReflectionNotFound(m.getIdent());
                }
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Object readObject(Meta m, Object o, boolean create) {
        String methodName = accessorMethodNameFromMeta("get", m.getIdent());
        try {
            try {
                Method method = o.getClass().getMethod(methodName);
                if (create) {
                    Object child = method.getReturnType().newInstance();
                    writeObject(m, o, child);
                    return child;
                } else {
                    return method.invoke(o);
                }
            } catch (NoSuchMethodException tryField) {
                try {
                    String fieldName = accessorMethodNameFromMeta("", m.getIdent());
                    Field field = o.getClass().getField(fieldName);
                    if (create) {
                        Object child = field.getDeclaringClass().newInstance();
                        field.set(o, child);
                        return child;
                    } else {
                        return field.get(o);
                    }
                } catch (NoSuchFieldException e) {
                    throw new ReflectionNotFound(m.getIdent());
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Value readField(Meta m, Object o) {
        String fieldName = accessorMethodNameFromMeta("", m.getIdent());
        Value v;
        try {
            DataType t = ((HasDataType) m).getDataType();
            v = new Value();
            ValueType valType = ((HasDataType) m).getDataType().getValType();
            Field field = o.getClass().getField(fieldName);
            switch (valType) {
                case ENUMERATION:
                case INT32: {
                    v.int32 = field.getInt(o);
                    break;
                }
                case BOOLEAN:
                    v.bool = field.getBoolean(o);
                    break;
                case STRING:
                    v.str = field.get(o).toString();
                    break;
                case INT32_LIST:
                    v.int32list = (int[]) field.get(o);
                    if (v.int32list == null) {
                        return null;
                    }
                    break;
                case BOOLEAN_LIST:
                    v.boollist = (boolean[]) field.get(o);
                    if (v.boollist == null) {
                        return null;
                    }
                case STRING_LIST:
                    v.strlist = coerseStringList(field.get(o));
                    if (v.strlist == null) {
                        return null;
                    }
                default:
                    throw new CodedError("Format " + v.valType + " not supported", 501);
            }
        } catch (ReflectiveOperationException e) {
            throw new ReflectionNotFound(fieldName);
        }
        return v;
    }

    public static final void setterField(Meta m, Object o, Value v) {
        String fieldName = accessorMethodNameFromMeta("", m.getIdent());
        try {
            Field field = o.getClass().getField(fieldName);
            switch (v.valType) {
                case INT32_LIST:
                    field.set(o, v.int32list);
                case INT32:
                    field.setInt(o, v.int32);
                    break;
                case STRING_LIST:
                    field.set(o, v.strlist);
                case STRING:
                    field.set(o, v.str);
                    break;
                case BOOLEAN_LIST:
                    field.set(o, v.boollist);
                case BOOLEAN:
                    field.setBoolean(o, v.bool);
                    break;
                default:
                    throw new CodedError("Format " + v.valType + " not supported", 501);
            }
        } catch (ReflectiveOperationException e) {
            String msg = String.format("Field %s not found on class %s", fieldName, o.getClass().getSimpleName());
            throw new MetaError(msg, e);
        }
    }

    public static String[] coerseStringList(Object o) {
        if (o instanceof Collection) {
            Collection c = (Collection) o;
            String[] strlist = new String[c.size()];
            Iterator items = c.iterator();
            for (int i = 0; items.hasNext(); i++) {
                Object item = items.next();
                strlist[i] = (item != null ? item.toString() : null);
            }
            return strlist;
        }

        return (String[])o;
    }

    public static final Value getterMethod(Meta m, Object o) {
        String methodName = accessorMethodNameFromMeta("get", m.getIdent());
        Value v;
        try {
            Method method = o.getClass().getMethod(methodName);
            Object result = method.invoke(o);
            if (result == null) {
                return null;
            }
            v = new Value();
            ValueType valType = ((HasDataType) m).getDataType().getValType();
            switch (valType) {
                case INT32_LIST:
                    v.int32list = (int[]) result;
                    break;
                case INT32:
                    v.int32 = (Integer) result;
                    break;
                case STRING_LIST:
                    v.strlist = coerseStringList(result);
                    break;
                case STRING:
                    v.str = result.toString();
                    break;
                case BOOLEAN_LIST:
                    v.boollist = (boolean[]) result;
                    break;
                case BOOLEAN:
                    v.bool = (Boolean) result;
                    break;
                default:
                    throw new CodedError("Format " + v.valType + " not supported", 501);
            }
        } catch (ReflectiveOperationException e) {
            throw new ReflectionNotFound(methodName);
        }
        return v;
    }
}
