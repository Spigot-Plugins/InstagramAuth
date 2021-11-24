package com.github.unldenis.auth.actions;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.unldenis.auth.exceptions.InstagramAuthException;
import com.github.unldenis.helper.concurrent.BukkitFuture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Login {

    public static CompletableFuture<IGClient> loginPlayer(@NotNull String username, @NotNull String password) {
        return BukkitFuture.supplyAsync(()-> {
            try {
                return IGClient.builder()
                        .username(username)
                        .password(password)
                        .login();
            } catch (IGLoginException e) {
                throw new InstagramAuthException(e.getLoginResponse().getMessage(), e.getCause());
            }
        });
    }
}
