package dev._2lstudios.example_mixins;

import dev._2lstudios.asm.mixins.Mixin;
import dev._2lstudios.asm.mixins.injections.At;
import dev._2lstudios.asm.mixins.injections.Injection;

@Mixin("net.minecraft.server.ChunkProviderServer")
public class ExampleMixin {
    @Injection(
        at = At.HEAD,
        method = "saveChunk"
    )
    public void saveChunk() {
        System.out.println("Hello World from a mixin.");
    }
}
