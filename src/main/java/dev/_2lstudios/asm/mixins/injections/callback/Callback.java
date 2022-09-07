package dev._2lstudios.asm.mixins.injections.callback;

public class Callback {
    private boolean cancelled = false;

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}
