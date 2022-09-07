package dev._2lstudios.asm;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.Gson;

import dev._2lstudios.asm.meta.MixinClass;
import dev._2lstudios.asm.utils.ClassPoolUtils;
import dev._2lstudios.asm.utils.JarUtils;

public class ASMLoader {
    private ASMPlatform platform;
    private File[] jars;

    private ASMMixinsLookupResult lookupResult;

    public ASMLoader(ASMPlatform platform, File[] jars) {
        this.platform = platform;
        this.jars = jars;

        this.lookupResult = new ASMMixinsLookupResult();
    }

    public ASMLoader(ASMPlatform platform, File jarsDirectory) {
        this(platform, jarsDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        }));
    };

    public ASMMixinsLookupResult lookupForMixins(ASMMixinsLookupResult result, File jar) throws IOException {
        ZipFile zip = new ZipFile(jar);
        ZipEntry mixinsFileEntry = zip.getEntry("mixins.json");

        if (mixinsFileEntry != null && !mixinsFileEntry.isDirectory()) {
            InputStream in = zip.getInputStream(mixinsFileEntry);        
            Reader reader = new InputStreamReader(in, "UTF-8");
            
            ASMMixinsFile mixinsFile = new Gson().fromJson(reader, ASMMixinsFile.class);
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
