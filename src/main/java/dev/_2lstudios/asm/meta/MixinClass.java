package dev._2lstudios.asm.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev._2lstudios.asm.meta.impl.MixinMethodInject;
import dev._2lstudios.asm.mixins.Mixin;
import dev._2lstudios.asm.mixins.injections.Injection;
import dev._2lstudios.asm.utils.ClassPoolUtils;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class MixinClass {
    private final Class<?> javaClass;

    private final List<MixinMethod> methods;
    private final CtClass sourceClass;
    private final CtClass targetClass;

    public MixinClass (String javaClassName) throws ClassNotFoundException {
        this.methods = new ArrayList<>();
        this.sourceClass = ClassPoolUtils.getClass(javaClassName);
        this.javaClass = Class.forName(javaClassName);

        Mixin mixin = javaClass.getAnnotation(Mixin.class);
        this.targetClass = ClassPoolUtils.getClass(mixin.value());
    }

    public void addMethod(MixinMethod method) {
        this.methods.add(method);
    }

    public int discovery() {
        for (Method method : this.javaClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Injection.class)) {
                MixinMethodInject mod = new MixinMethodInject(this, method);
                this.addMethod(mod);
            }
        }

        return this.methods.size();
    }

    public Class<?> getJavaClass() {
        return this.javaClass;
    }

    public CtMethod getMethod(CtClass clazz, String methodName, CtClass[] params) {

        try {
            return clazz.getDeclaredMethod(methodName, params);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public CtMethod getMethod(CtClass clazz, final String methodDescription) {
        if (methodDescription.contains("(")) {
            String rawParams = methodDescription.split("(")[1].split(")")[0];
            String methodName = methodDescription.split("(")[0];
            CtClass[] params = ClassPoolUtils.parseParams(rawParams);
            return this.getMethod(clazz, methodName, params);
        } else { 
            return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals(methodDescription))
                .findAny()
                .orElse(null);
        }
    }

    public CtClass getSourceClass() {
        return this.sourceClass;
    }

    public CtMethod getSourceMethod(String methodName, CtClass[] params) {
        return this.getMethod(sourceClass, methodName, params);
    }

    public CtClass getTargetClass() {
        return this.targetClass;
    }

    public CtMethod getTargetMethod(String methodName, CtClass[] params) {
        return this.getMethod(targetClass, methodName, params);
    }

    public CtMethod getTargetMethod(String methodDescription) {
        return this.getMethod(targetClass, methodDescription);
    }

    public void patch() throws Exception {
        for (MixinMethod mod : this.methods) {
            mod.patch();
        }

        this.targetClass.toClass();
    }
}
