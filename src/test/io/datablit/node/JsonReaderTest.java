package io.datablit.node;

import io.datablit.meta.Container;
import io.datablit.meta.DataType;
import io.datablit.meta.Leaf;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;

/**
 *
 */
public class JsonReaderTest {

    @Test
    public void testRead() {
        Reader r = new StringReader("{\"a\":\"b\", \"c\" : {\"d\" : \"e\"}}");
        JsonReader jr = new JsonReader(r);
        Node n = jr.getNode();

        FieldRequest fr = new FieldRequest();
        fr.meta = new Leaf("a");
        fr.meta.setDataType(new DataType(fr.meta, "string"));
        Value v = n.read(fr);
        assertNotNull(v);
        assertEquals(v.str, "b");

        ContainerRequest cr = new ContainerRequest();
        cr.meta = new Container("c");
        Node child = n.select(cr);
        assertNotNull(child);

        FieldRequest fr2 = new FieldRequest();
        fr2.meta = new Leaf("d");
        fr2.meta.setDataType(new DataType(fr.meta, "string"));
        Value v2 = child.read(fr2);
        assertNotNull(v2);
        assertEquals(v2.str, "e");
    }
}
