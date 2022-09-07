package dev._2lstudios.asm.utils;

public class ClassUtils {
    public static void loadClass(String name) {
        try {
            Class.forName(name);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static void loadImportantClasses() {
        loadClass("dev._2lstudios.asm.mixins.injections.callback.Callback");
        loadClass("dev._2lstudios.asm.mixins.injections.callback.CallbackReturnable");
    }
}
