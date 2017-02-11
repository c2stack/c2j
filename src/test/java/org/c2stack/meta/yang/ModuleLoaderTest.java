package org.c2stack.meta.yang;

import org.c2stack.meta.MetaUtil;
import org.c2stack.meta.SimpleStreamSource;
import org.c2stack.meta.Module;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ModuleLoaderTest {

    @Test
    public void testLoadModule() throws IOException {
		ModuleLoader loader = new ModuleLoader();
		SimpleStreamSource ds = new SimpleStreamSource(ModuleLoaderTest.class);
		Module simple = loader.loadModule(ds, "simple.yang");
		assertNotNull(simple);
		assertEquals("turing-machine", simple.getIdent());
		assertEquals(4, MetaUtil.collectionLength(simple.getTypedefs()));
		assertEquals(1, MetaUtil.collectionLength(simple.getGroupings()));
		assertEquals(1, MetaUtil.collectionLength(simple));
		assertEquals(2, MetaUtil.collectionLength(simple.getRpcs()));
		assertEquals(1, MetaUtil.collectionLength(simple.getNotifications()));
	}
}
