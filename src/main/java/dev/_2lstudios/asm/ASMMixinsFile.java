package dev._2lstudios.asm;

import com.google.gson.annotations.SerializedName;

class MixinsFileEntry {
    @SerializedName("package")
    private String _package;

    @SerializedName("classes")
    private String[] _classes;

    public String getPackage() {
        return this._package;
    }

    public String[] getClasses() {
        return this._classes;
    }
}

public class ASMMixinsFile {
    private MixinsFileEntry bukkit;
    private MixinsFileEntry bungee;

    public MixinsFileEntry getBukkit() {
        return this.bukkit;
    }

    public MixinsFileEntry getBungee() {
        return this.bungee;
    }

    public MixinsFileEntry getForPlatform(ASMPlatform platform) {
        switch (platform) {
            case BUKKIT:
                return this.bukkit;
            case BUNGEE:
                return this.bungee;
        }

        return null;
    }
}
