package org.c2stack.node;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ReflectTest {

    @Test
    public void testGetterMethodNameFromMeta() {
        assertEquals("xFoo", Reflect.accessorMethodNameFromMeta("x", "foo"));
        assertEquals("xFooBar", Reflect.accessorMethodNameFromMeta("x", "foo-bar"));
        assertEquals("fooBar", Reflect.accessorMethodNameFromMeta("", "foo-bar"));
    }
}
