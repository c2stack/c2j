package io.datablit.meta;

/**
 *
 */
public class MetaList extends CollectionBase implements Describable, HasTypedefs, HasGroupings {
    private LinkedListCollection groupings = new LinkedListCollection("groupings", this);
    private LinkedListCollection typedefs = new LinkedListCollection("typedefs", this);
    private String[] keyIdents;
    private HasDataType[] key;
    private boolean config;
    private boolean mandatory;

    public MetaList(String ident) {
        super(ident);
    }

    public void setEncodedKeys(String encodedKeys) {
        keyIdents = encodedKeys.split(" ");
    }

    public String[] getKeyIdents() {
        return keyIdents;
    }

    public void setKeyIdents(String[] keyIdents) {
        this.keyIdents = keyIdents;
    }

    public HasDataType[] getKey() {
        if (key == null) {
            HasDataType[] candidate = new HasDataType[this.keyIdents.length];
            for (int i = 0; i < key.length; i++) {
                candidate[i] = (HasDataType) MetaUtil.findByIdent(this, this.keyIdents[i]);
            }
            this.key = candidate;
        }
        return key;
    }

    @Override
    public MetaCollection getGroupings() {
        return groupings;
    }

    @Override
    public MetaCollection getTypedefs() {
        return typedefs;
    }

    public void setConfig(boolean config) {
        this.config = config;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
}
