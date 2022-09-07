package dev._2lstudios.asm.mixins.injections.callback;

public class CallbackReturnable<T> extends Callback {
    private T returnValue;

    public T getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(T value) {
        this.returnValue = value;
    }
}
