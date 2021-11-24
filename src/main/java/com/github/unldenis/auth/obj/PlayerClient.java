package com.github.unldenis.auth.obj;

import com.github.instagram4j.instagram4j.IGClient;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public class PlayerClient {

    private final UUID player;
    private IGClient igClient;
    private long loginTime;


    public PlayerClient(@NotNull UUID player) {
        this.player = player;
    }

    public void login(@NotNull IGClient igClient) {
        this.igClient = igClient;
        loginTime = Instant.now().toEpochMilli();
    }

    public boolean isLogged() {
        return loginTime != 0;
    }

    public UUID getPlayer() {
        return player;
    }

    public IGClient getIgClient() {
        return igClient;
    }
}
