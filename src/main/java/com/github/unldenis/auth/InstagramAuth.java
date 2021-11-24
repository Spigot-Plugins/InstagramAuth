package com.github.unldenis.auth;

import com.github.unldenis.auth.manager.BaseModule;
import com.github.unldenis.auth.manager.FollowServerModule;
import com.github.unldenis.auth.manager.LoginModule;
import com.github.unldenis.helper.concurrent.BukkitFuture;
import com.github.unldenis.helper.data.DataManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class InstagramAuth extends JavaPlugin {

    private DataManager config;

    private LoginModule loginModule;
    private Set<BaseModule> modules = new HashSet<>();

    @Override
    public void onEnable() {
        //config.yml
        config = new DataManager(this, "config.yml");

        //bind futures
        BukkitFuture.bindWith(this);

        //add modules
        loginModule = new LoginModule(this);

        modules.add(loginModule);
        modules.add(new FollowServerModule(this));

        //load modules
        modules.forEach(BaseModule::onEnable);

    }

    @Override
    public void onDisable() {

    }

    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    public LoginModule getLoginModule() {
        return loginModule;
    }
}
