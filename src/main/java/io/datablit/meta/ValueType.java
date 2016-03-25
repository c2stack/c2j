package org.conf2.schema;

/**
 *
 */
public enum ValueType {
    // In specific order - see browse.h
    BINARY_LIST("binary"),
    BITS_LIST("bits"),
    BOOLEAN_LIST("boolean"),
    DECIMAL64_LIST("decimal64"),
    ENUMERATION_LIST("enumeration"),
    IDENTITYDEF_LIST("identitydef"),
    INSTANCE_IDENTIFIER_LIST("instance-identifier"),
    INT8_LIST("int8"),
    INT16_LIST("int16"),
    INT32_LIST("int32"),
    INT64_LIST("int64"),
    LEAFREF_LIST("leafref"),
    STRING_LIST("string"),
    UINT8_LIST("uint8"),
    UINT16_LIST("uint16"),
    UINT32_LIST("uint32"),
    UINT64_LIST("uint64"),
    UNION_LIST("union"),
    ANY_LIST("any"),

    BINARY(BINARY_LIST),
    BITS(BITS_LIST),
    BOOLEAN(BOOLEAN_LIST),
    DECIMAL64(DECIMAL64_LIST),
    ENUMERATION(ENUMERATION_LIST),
    IDENTITYREF(IDENTITYDEF_LIST),
    INSTANCE_IDENTIFIER(INSTANCE_IDENTIFIER_LIST),
    INT8(INT8_LIST),
    INT16(INT16_LIST),
    INT32(INT32_LIST),
    INT64(INT64_LIST),
    LEAFREF(LEAFREF_LIST),
    STRING(STRING_LIST),
    UINT8(UINT8_LIST),
    UINT16(UINT16_LIST),
    UINT32(UINT32_LIST),
    UINT64(UINT64_LIST),
    UNION(UNION_LIST),
    ANY(ANY_LIST);

    public String ident;
    public ValueType listVersion;
    public ValueType itemVersion;

    ValueType(String ident) {
        this.ident = ident;
        this.listVersion = this;
    }

    ValueType(ValueType listVersion) {
        this.ident = listVersion.ident;
        this.listVersion = listVersion;
        listVersion.itemVersion = this;
    }

    public boolean isList() {
        return listVersion == this;
    }
}
