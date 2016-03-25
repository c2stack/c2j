package io.datablit.node;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloTest {
    @Test
    public void hello() {
        Hello h = new Hello();
        assertEquals("hello", h.sayHello());
    }
}
