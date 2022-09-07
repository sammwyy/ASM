package dev._2lstudios.asm.utils;

import org.bukkit.Bukkit;

public class ClassNameUtils {
    private static String version;

    public static String getVersion() {
        if (version == null) {
            version = Bukkit.getServer().getClass()
                .getPackage()
                .getName()
                .replace(".", ",")
                .split(",")[3];
        }

        return version;
    }

    public static String getNMSPackage() {
        return "net.minecraft.server." + getVersion();
    }

    public static String fixClassName(String className) {
        if (className.startsWith("net.minecraft.server")) {
            return className.replace("net.minecraft.server", getNMSPackage());
        }

        return className;
    }
}
