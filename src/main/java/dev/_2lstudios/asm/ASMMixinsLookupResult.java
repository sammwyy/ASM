package dev._2lstudios.asm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev._2lstudios.asm.meta.MixinClass;
import dev._2lstudios.asm.utils.ClassPoolUtils;

public class ASMMixinsLookupResult {
    private List<MixinClass> mixinsClasses;

    public ASMMixinsLookupResult() {
        this.mixinsClasses = new ArrayList<>();
    }

    public void addMixinClass(File jarfile, String className) throws IOException {
        if (ClassPoolUtils.addJarToPoolClasspath(jarfile)) {
            try {
                MixinClass mixinClass = new MixinClass(className);
                this.mixinsClasses.add(mixinClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<MixinClass> getMixinsClasses() {
        return this.mixinsClasses;
    }
}
