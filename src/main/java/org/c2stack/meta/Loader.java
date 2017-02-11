package org.c2stack.meta;

/**
 *
 */
public interface Loader {

    public Module loadModule(StreamSource source, String resource);
}
