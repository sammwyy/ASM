package dev._2lstudios.example_mixins;

import dev._2lstudios.asm.mixins.Mixin;
import dev._2lstudios.asm.mixins.injections.At;
import dev._2lstudios.asm.mixins.injections.Injection;
import dev._2lstudios.asm.mixins.injections.callback.Callback;

// En la clase ChunkProviderServer.
@Mixin("net.minecraft.server.ChunkProviderServer")
public class ExampleMixin {
    @Injection(
        // En la cabeza del metodo saveChunk.
        at = At.HEAD,
        method = "saveChunk"
    )
    public void saveChunk(Callback callback) {
        // Mostrar un mensaje.
        System.out.println("Hello World from a mixin.");
        // Prevenir que el metodo siga corriendo.
        callback.cancel();
    }
}
