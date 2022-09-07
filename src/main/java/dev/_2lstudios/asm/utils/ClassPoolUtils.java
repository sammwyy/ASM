package dev._2lstudios.asm.utils;

import java.io.File;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ClassPoolUtils {
    private static ClassPool pool = ClassPool.getDefault();

    public static ClassPool getPool() {
        return pool;
    }

    public static boolean addJarToPoolClasspath(File jar) {
        try {
            pool.appendClassPath(jar.getAbsolutePath());
            return true;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static CtClass getClass(String className, boolean createDummyIfNotExist) {
        String fixedName = ClassNameUtils.fixClassName(className);
        CtClass findClass = pool.getOrNull(fixedName);

        if (findClass != null) {
            return findClass;
        } else {
            return createDummyIfNotExist ? ClassPoolUtils.getPool().makeClass(fixedName) : null;
        }
    }

    public static CtClass getClass(String className) {
        return getClass(className, false);
    }

    public static CtClass[] parseParams(String descriptor) {
        String[] args = descriptor.split(",");
        CtClass[] result = new CtClass[args.length];

        for (int i = 0; i < args.length; i++) {
            String arg = args[i].trim();
            CtClass item = ClassTypeUtils.primitiveToCtType(arg);

            if (item == null) {
                item = getClass(arg, true);
            }

            result[i] = item;
        }

        return result;
    }
}
