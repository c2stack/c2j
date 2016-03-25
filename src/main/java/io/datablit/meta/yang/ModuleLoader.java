package io.datablit.meta.yang;

import io.datablit.util.CodedError;
import io.datablit.node.Context;
import io.datablit.node.JsonReader;
import io.datablit.node.Node;
import io.datablit.meta.Module;
import io.datablit.meta.StreamSource;
import io.datablit.node.ModuleBrowser;
import io.datablit.util.StreamGobbler;

import java.io.*;
import java.util.logging.Logger;


/**
 *
 */
public class ModuleLoader {
    Logger log = Logger.getLogger("datablit");
    private static String datablitExecutable = System.getProperty("io.datablit.executable", "datablit");

    public Module loadModule(StreamSource source, String resource) {
        ModuleBrowser moduleBrowser = new ModuleBrowser(new Module("unknown"), false);
        Node json = new JsonReader(read(resource)).getNode();
        new Context().select(moduleBrowser.getSchema(), moduleBrowser.getNode()).insertFrom(json);
        return moduleBrowser.module;
    }

    Reader read(String module) {
        String[] cmd = asArray(datablitExecutable, "-module", module);
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
