package io.datablit.node;

import io.datablit.meta.yang.YangModule;
import org.junit.Test;

/**
 *
 */
public class WalkTest {

    @Test
    public void testExhaustiveWalk() {
        ModuleBrowser browser = new ModuleBrowser(YangModule.YANG, false);
//        Walk.walk(browser.getRootSelector(), new ExhaustiveWalk());
    }
}
