package dev._2lstudios.asm.meta.impl;

import java.lang.reflect.Method;

import dev._2lstudios.asm.meta.MixinClass;
import dev._2lstudios.asm.meta.MixinMethod;
import dev._2lstudios.asm.mixins.injections.Injection;
import dev._2lstudios.asm.utils.ClassTypeUtils;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class MixinMethodInject implements MixinMethod {
    private MixinClass mixinClass;
    private Method javaMethod;
    private Injection meta;

    public MixinMethodInject(MixinClass mixinClass, Method javaMethod) {
        this.mixinClass = mixinClass;
        this.javaMethod = javaMethod;
        this.meta = javaMethod.getAnnotation(Injection.class);
    }

    public CtMethod getTargetMethod() {
        return this.mixinClass.getTargetMethod(this.meta.method());
    }

    public CtMethod getSourceMethod() {
        CtClass[] params = ClassTypeUtils.parameterToCtClass(this.javaMethod.getParameters());
        return this.mixinClass.getSourceMethod(this.javaMethod.getName(), params);
    }

    private String injectMetaAux() throws CannotCompileException {
        CtClass declaring = this.mixinClass.getTargetClass();
        CtMethod source = this.getSourceMethod();

        CtMethod auxMethod = CtNewMethod.copy(source, declaring, null);

        String id = "__AUX_" + auxMethod.getName();
        auxMethod.setName(id);

        try {
            declaring.addMethod(auxMethod);
            return id;
        } catch (CannotCompileException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void patch() throws Exception {
        String auxID = this.injectMetaAux();
        this.getTargetMethod().insertBefore(auxID + "(new dev._2lstudios.asm.mixins.injections.callback.Callback());");
    }
}
