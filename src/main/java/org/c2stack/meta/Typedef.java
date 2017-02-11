package org.c2stack.meta;

/**
 *
 */
public class Typedef extends MetaBase implements Describable, HasDataType {
    private DataType dataType;

    public Typedef(String ident) {
        super(ident);
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }
}
