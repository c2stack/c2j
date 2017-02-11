package org.c2stack.util;

import java.io.*;

public class StreamGobbler extends Thread {
    InputStream is;
    StringBuilder str = new StringBuilder();
    char[] buff = new char[1024];

    public StreamGobbler(InputStream is) {
        this.is = is;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            int len = isr.read(buff);
            while (len > 0) {
                str.append(buff, 0, len);
                len = isr.read(buff);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String toString() {
        return str.toString();
    }
}
