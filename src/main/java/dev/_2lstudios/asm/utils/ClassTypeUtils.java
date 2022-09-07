package dev._2lstudios.asm.utils;

import java.lang.reflect.Parameter;

import javassist.CtClass;

public class ClassTypeUtils {
    public static CtClass primitiveToCtType(String name) {
        switch(name.trim()) {
            case "boolean":
                return CtClass.booleanType;
            case "byte":
                return CtClass.byteType;
            case "char":
                return CtClass.charType;
            case "double":
                return CtClass.doubleType;
            case "float":
                return CtClass.floatType;
            case "int":
                return CtClass.intType;
            case "long":
                return CtClass.longType;
            case "short":
                return CtClass.shortType;
            case "void":
                return CtClass.voidType;
            case "string":
                return ClassPoolUtils.getPool().getOrNull("java.lang.String");
            default:
                return null;
        }
    }
    
    public static CtClass[] primitiveToCtTypes(String[] names) {
        CtClass[] result = new CtClass[names.length];
        
        for (int i = 0; i < names.length; i++) {
            result[i] = primitiveToCtType(names[i]);
        }

        return result;
    }
    
    public static CtClass classToCtType(Class<?> clazz) {
        if (clazz == Boolean.class) {
            return CtClass.booleanType;
        } else if (clazz == Byte.class) {
            return CtClass.byteType;
        } else if (clazz == Character.class) {
            return CtClass.charType;
        } else if (clazz == Double.class) {
            return CtClass.doubleType;
        } else if (clazz == Float.class) {
            return CtClass.floatType;
        } else if (clazz == Integer.class) {
            return CtClass.intType;
        } else if (clazz == Long.class) {
            return CtClass.longType;
        } else if (clazz == Short.class) {
            return CtClass.shortType;
        } else if (clazz == Void.class) {
            return CtClass.voidType;
        } else if (clazz == String.class) {
            return ClassPoolUtils.getPool().getOrNull("java.lang.String");
        } else {
            return null;
        }
    }

    public static CtClass[] classToCtType(Class<?>[] classes) {
        CtClass[] result = new CtClass[classes.length];
        
        for (int i = 0; i < classes.length; i++) {
            result[i] = classToCtType(classes[i]);
        }

        return result;
    }

    public static CtClass parameterToCtClass(Parameter parameter) {
        CtClass target = classToCtType(parameter.getType());
        if (target == null) {
            target = ClassPoolUtils.getClass(parameter.getType().getName());
        }
        return target;
    }

    public static CtClass[] parameterToCtClass(Parameter[] parameters) {
        CtClass[] result = new CtClass[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            result[i] = parameterToCtClass(parameters[i]);
        }

        return result;
    }
}
