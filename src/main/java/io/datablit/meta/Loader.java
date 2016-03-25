package io.datablit.meta;

/**
 *
 */
public interface Loader {

    public Module loadModule(StreamSource source, String resource);
}
