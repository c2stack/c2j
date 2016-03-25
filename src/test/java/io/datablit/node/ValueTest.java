package io.datablit.node;

import io.datablit.meta.DataType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ValueTest {

    @Test
    public void testCoerse() {
        Object[][] tests = new Object[][] {
                new Object[] {
                    "string", "hello", "hello"
                },
                new Object[] {
                        "int32", "1", 1
                },
                new Object[] {
                        "boolean", "true", Boolean.TRUE
                },
                new Object[] {
                        "boolean", "false", Boolean.FALSE
                },
        };
        for (int i = 0; i < tests.length; i++) {
            Object[] test = tests[i];
            DataType t = new DataType(null, test[0].toString());
            Value v = Value.coerse(t, test[1]);
            assertEquals(String.format("test #%d", i), v.getValue(), test[2]);
        }
    }
}
