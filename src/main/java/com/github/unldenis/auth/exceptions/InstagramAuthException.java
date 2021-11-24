package com.github.unldenis.auth.exceptions;

import org.jetbrains.annotations.NotNull;

public class InstagramAuthException extends RuntimeException{

    public InstagramAuthException(@NotNull Throwable throwable) {
        super(throwable);
    }
    public InstagramAuthException(@NotNull String message, @NotNull Throwable throwable) {
        super(message, throwable);
    }
}
