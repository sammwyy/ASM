package dev._2lstudios.asm;

import com.google.gson.Gson;
import dev._2lstudios.asm.meta.MixinClass;
import dev._2lstudios.asm.utils.ClassPoolUtils;
import dev._2lstudios.asm.utils.JarUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ASMLoader {
    private static final Gson DEFAULT_GSON = new Gson();

    private final Gson gson;
    private final ASMPlatform platform;
    private final File[] jars;

    private final ASMMixinsLookupResult lookupResult;

    public ASMLoader(ASMPlatform platform, File[] jars, Gson gson) {
        this.gson = gson;
        this.platform = platform;
        this.jars = jars;

        this.lookupResult = new ASMMixinsLookupResult();
    }

    public ASMLoader(ASMPlatform platform, File[] jars) {
        this(platform, jars, DEFAULT_GSON);
    }

    public ASMLoader(ASMPlatform platform, File jarsDirectory, Gson gson) {
        this(platform, jarsDirectory.listFiles((dir, name) -> name.endsWith(".jar")), gson);
    };

    public ASMLoader(ASMPlatform platform, File jarsDirectory) {
        this(platform, jarsDirectory, DEFAULT_GSON);
    }

    public ASMMixinsLookupResult lookupForMixins(ASMMixinsLookupResult result, File jar) throws IOException {
        ZipFile zip = new ZipFile(jar);
        ZipEntry mixinsFileEntry = zip.getEntry("mixins.json");

        if (mixinsFileEntry != null && !mixinsFileEntry.isDirectory()) {
            InputStream in = zip.getInputStream(mixinsFileEntry);        
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            
            ASMMixinsFile mixinsFile = gson.fromJson(reader, ASMMixinsFile.class);
            MixinsFileEntry mixins = mixinsFile.getForPlatform(this.platform);

            for (String mixinClassName : mixins.getClasses()) {
                String fullMixinClassName = mixins.getPackage() + "." + mixinClassName;
                result.addMixinClass(jar, fullMixinClassName);
            }
        }
        
        zip.close();
        return result;
    }

    public int lookupForMixins() {
        for (File jar : this.jars) {
            try {
                this.lookupForMixins(this.lookupResult, jar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this.lookupResult.getMixinsClasses().size();
    }
    
    public int discover() {
        int result = 0;

        for (MixinClass mixinClass : this.lookupResult.getMixinsClasses()) {
            result += mixinClass.discovery();
        }

        return result;
    }

    public void patch() throws Exception {
        String jarFile = JarUtils.findPathJar(ASMLoader.class);
        ClassPoolUtils.getPool().appendClassPath(jarFile);

        for (MixinClass mixinClass : this.lookupResult.getMixinsClasses()) {
            mixinClass.patch();
        }
    }
}
