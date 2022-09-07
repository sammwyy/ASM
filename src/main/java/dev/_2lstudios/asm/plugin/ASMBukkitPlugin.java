package dev._2lstudios.asm.plugin;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.asm.ASMLoader;
import dev._2lstudios.asm.ASMPlatform;

public class ASMBukkitPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        File jarDirectory = this.getDataFolder().getParentFile();
        ASMLoader loader = new ASMLoader(ASMPlatform.BUKKIT, jarDirectory);

        long startTime = System.currentTimeMillis();

        this.getLogger().info(""); 
        this.getLogger().info("ASM Mixin loader by 2LStudios (Sammwy/LinsaFTW)");
        this.getLogger().info(""); 

        // STEP 1/3
        this.getLogger().info("[1/3] Scanning plugin directory for mixins...");
        int mixins = loader.lookupForMixins();
        this.getLogger().info("    Lookup result: " + mixins + " mixins ready to discover.");

        // STEP 2/3
        this.getLogger().info("[2/3] Discovering patches in found mixins...");
        int discover = loader.discover();
        this.getLogger().info("    Discover result: " + discover + " patches ready to be injected.");
        
        // STEP 3/3
        this.getLogger().info("[3/3] Starting patch process...");

        try {
            loader.patch();

            long endTime = System.currentTimeMillis();
            long took = endTime - startTime;
            this.getLogger().info("    Patch success! (took " + took + "ms)");
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long took = endTime - startTime;
            this.getLogger().info("    Failed to apply all patched (took " + took + "ms)");
            e.printStackTrace();
        }
    }
}
