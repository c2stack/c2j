package org.c2stack.meta.yang;

import org.c2stack.util.CodedError;
import org.c2stack.node.Context;
import org.c2stack.node.JsonReader;
import org.c2stack.node.Node;
import org.c2stack.meta.Module;
import org.c2stack.meta.StreamSource;
import org.c2stack.node.ModuleBrowser;
import org.c2stack.util.StreamGobbler;

import java.io.*;
import java.util.logging.Logger;


/**
 *
 */
public class ModuleLoader {
    Logger log = Logger.getLogger("c2stack");
    private static String c2stackExecutable = System.getProperty("org.c2stack.executable", "c2stack");

    public Module loadModule(StreamSource source, String resource) {
        ModuleBrowser moduleBrowser = new ModuleBrowser(new Module("unknown"), false);
        Node json = new JsonReader(read(resource)).getNode();
        new Context().select(moduleBrowser.getSchema(), moduleBrowser.getNode()).insertFrom(json);
        return moduleBrowser.module;
    }

    Reader read(String module) {
        String[] cmd = asArray(c2stackExecutable, "-module", module);
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            StreamGobbler stderr = new StreamGobbler(p.getErrorStream());
            StreamGobbler stdout = new StreamGobbler(p.getInputStream());
            stderr.start();
            stdout.start();
            int status = p.waitFor();
            if (status != 0) {
                String s = stderr.toString();
                stderr.join(100);
                throw new CodedError(s);
            }
            stdout.join(100);
            return new StringReader(stdout.toString());
        } catch (IOException e) {
            throw new CodedError(e.getMessage());
        } catch (InterruptedException e) {
            throw new CodedError(e.getMessage());
        }
    }

    String[] asArray(String ...a) {
        return a;
    }
}
