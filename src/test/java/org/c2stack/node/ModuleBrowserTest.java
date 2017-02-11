package org.c2stack.node;

import org.c2stack.meta.*;
import org.c2stack.meta.yang.YangModule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class ModuleBrowserTest {

    @Test
    public void testRootSelector() {
        ModuleBrowser b = new ModuleBrowser(YangModule.YANG, true);
//        Selection s = b.getRootSelector();
//        Selection ms = Walk.walk(s, new BrowsePath("module"));
//        assertNotNull(ms);
//        assertEquals("module", ms.meta.getIdent());
//        assertNotNull(ms.meta);
//        ms.position = MetaUtil.findByIdent(ms.meta, "prefix");
//        assertNotNull(ms.position);
//        BrowseValue v = ms.read.read();
//        assertEquals("yang", v.str);
    }
}
