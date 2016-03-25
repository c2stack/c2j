package io.datablit.meta;

/**
 *
 */
public class Any extends MetaBase implements Describable {
    private String description;
    public Any(String ident) {
        super(ident);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
