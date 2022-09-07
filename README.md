# ASM

Bukkit ASM/Mixin loader for easy bytecode manipulation from plugins.  

**Warning:** The debug print will not be removed until the plugin is ready for production.  

Inspired in Sponge's Mixin Framework (It is not a fork nor an implementation, this tool is made from scratch and may not be the same to use.)

## Why i need this?

Let's take an example situation. You want to change a function of your Bukkit/Spigot/Paper server but there is no way to do it with the API. You only have to fork the server software but you would lose access to updates, and the change may be so small that it is not worth the effort.  
  
With this tool you can modify the source code of the server software without having to fork.  
  
Also, all changes are temporary and do not affect the original file.  

## How to use

Create a bukkit plugin and inside the jar place a file called mixins.json with the following format:  

```json
{
    "bukkit": {
        "package": "com.example.yourplugin.mixins",
        "classes": [
            "ExampleMixin"
        ]
    }
}
```

We will create a package at our whim and place it in the "package" field. In said package we are going to create a class whose name is listed in the array of the "classes" field.  
  
The created class must have this structure:

```java
package com.example.yourplugin.mixins;

import dev._2lstudios.asm.mixins.Mixin;
import dev._2lstudios.asm.mixins.injections.At;
import dev._2lstudios.asm.mixins.injections.Injection;

// Class to be altered.
@Mixin("net.minecraft.server.ChunkProviderServer")
public class ExampleMixin {
    @Injection(
        // Name of the method to modify.
        method = "saveChunk",
        // Position in the method where the code will be injected.
        at = At.HEAD
    )
    public void saveChunk() {
        // Code to be injected into the method.
        System.out.println("Hello World from a mixin.");
    }
}
```

## To Do

- [ ] Documentation.  
- [X] Mixin loader.  
- [ ] At variable in injector.  
- [X] Method modifier.  
- [ ] Field modifier.  
- [ ] Return injector.  
- [ ] Method injector.  
- [ ] Method mutation.  
- [ ] Method proxy.  
- [ ] Enum injector.  
- [ ] Enum mutation.
- [ ] Field injector.  
- [ ] Field mutation.  
- [ ] Call plugin classes and methods from NMS.  
- [ ] NMS Mapper (Like Yarn)  
- [ ] OB Mapper.  
