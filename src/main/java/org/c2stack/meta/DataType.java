package org.c2stack.meta;

import java.util.HashMap;
import java.util.Map;

public class DataType extends MetaBase {
    public HasDataType parent;
    public String range;
    public int minLength;
    public int maxLength;
    public String path;
    public String pattern;
    public String[] enumeration;
    ValueType valType;
    public String defaultStr;
    private DataType resolved;
    private static Map<String, ValueType> internalTypes = new HashMap<String, ValueType>(ValueType.values().length);
    static {
        // NOTE: adding each type manually because ValueType.values is not initialized in
        // time for this static block to work
        //
        //        for (ValueType t : ValueType.values()) {
        //            if (t.itemVersion == t) {
        //                internalTypes.put(t.ident, t);
        //            }
        //        }
        internalTypes.put("binary", ValueType.BINARY);
        internalTypes.put("bits", ValueType.BITS);
        internalTypes.put("boolean", ValueType.BOOLEAN);
        internalTypes.put("decimal64", ValueType.DECIMAL64);
        internalTypes.put("enumeration", ValueType.ENUMERATION);
        internalTypes.put("identityref", ValueType.IDENTITYREF);
        internalTypes.put("instance-identifier", ValueType.INSTANCE_IDENTIFIER);
        internalTypes.put("int8", ValueType.INT8);
        internalTypes.put("int16", ValueType.INT16);
        internalTypes.put("int32", ValueType.INT32);
        internalTypes.put("int64", ValueType.INT64);
        internalTypes.put("leafref", ValueType.LEAFREF);
        internalTypes.put("string", ValueType.STRING);
        internalTypes.put("uint8", ValueType.UINT8);
        internalTypes.put("uint16", ValueType.UINT16);
        internalTypes.put("uint32", ValueType.UINT32);
        internalTypes.put("uint64", ValueType.UINT64);
        internalTypes.put("union", ValueType.UNION);
        internalTypes.put("any", ValueType.ANY);

    }

    public DataType(HasDataType parent, String ident) {
        super(ident);
        parent = parent;
    }

    @Override
    public void setIdent(String ident) {
        if (valType == null) {
            valType = internalTypes.get(ident);
        }
    }

    public ValueType getValType() {
        ValueType type = valType;
        if (valType == null) {
            DataType dtype = resolve();
            if (dtype != null) {
                valType = dtype.getValType();
            }
        }
        if (valType != null) {
            if (parent instanceof LeafList) {
                valType = valType.listVersion;
            }
        }
        return valType;
    }

    public boolean hasDefault() {
        if (defaultStr != null) {
            return true;
        }
        DataType r = resolve();
        if (r != null) {
            return r.hasDefault();
        }
        return false;
    }

    public String getDefault() {
        if (defaultStr != null) {
            return defaultStr;
        }
        DataType r = resolve();
        if (r != null) {
            return r.getDefault();
        }
        return null;
    }

    DataType resolve() {
        if (resolved == null) {
            // leafref types are determined by the meta path pointed to
            if (valType != null && (valType == ValueType.LEAFREF || valType == ValueType.LEAFREF_LIST)) {
                if (path == null) {
                    throw new SchemaError("Missing 'path' on leafref " + getIdent());
                }
                Meta resolvedMeta = MetaUtil.findByPath(parent.getParent(), path);
                if (resolvedMeta == null) {
                    throw new SchemaError("Could not resolve 'path' on leafref " + getIdent());
                }
                resolved = ((HasDataType) resolvedMeta).getDataType();
            }
            // TODO resolve typedefs
        }
        return resolved;
    }
}
