package com.github.unldenis.auth.manager;

import com.github.unldenis.auth.InstagramAuth;
import org.jetbrains.annotations.NotNull;

public abstract class BaseModule {

    protected final InstagramAuth plugin;

    public BaseModule(@NotNull InstagramAuth plugin) {
        this.plugin = plugin;
    }

    public abstract void onEnable();

    public abstract void onDisable();


    protected void registerCommands() {}

    protected void registerEvents() {}

}
