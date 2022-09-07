package dev._2lstudios.asm;

import java.io.IOException;
import java.io.InputStream;

public class ASMClassLoader extends ClassLoader {
    public Class<?> loadClassFromStream(String name, InputStream stream) throws ClassNotFoundException {
        try {
            byte[] buf = new byte[10000];
            int len = stream.read(buf);
            return defineClass(name, buf, 0, len);
        } catch (IOException e) {
            throw new ClassNotFoundException("", e);
        }
    }
}
