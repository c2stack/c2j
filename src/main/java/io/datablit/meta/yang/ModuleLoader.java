package org.conf2.schema.yang;

import org.conf2.CodedError;
import org.conf2.data.Context;
import org.conf2.data.JsonReader;
import org.conf2.data.Node;
import org.conf2.schema.Module;
import org.conf2.schema.StreamSource;
import org.conf2.data.ModuleBrowser;
import org.conf2.util.StreamGobbler;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 */
public class ModuleLoader {
    Logger log = Logger.getLogger("conf2");

    public Module loadModule(StreamSource source, String resource) {
        ModuleBrowser moduleBrowser = new ModuleBrowser(null);
        Node json = new JsonReader(read(resource)).getNode();
        new Context().select(moduleBrowser.getSchema(), moduleBrowser.getNode()).insertFrom(json);
        return moduleBrowser.module;
    }

    Reader read(String module) {
        String[] cmd = asArray("c2-yang", "-module", module);
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
