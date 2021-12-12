package com.github.unldenis.helper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public final class CCommand implements CommandExecutor {


    private final String command;
    private BiConsumer<CommandSender,String[]> biConsumer;

    public CCommand(@NotNull String command) {
        this.command = command;
    }


    /**
     * Method that is executed when the command is executed.
     * @param biConsumer the biconsumer from the sender and the arguments
     * @return the same object
     */
    public CCommand handler(@NotNull BiConsumer<CommandSender, String[]> biConsumer) {
        this.biConsumer = biConsumer;
        return this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase(command)) {
            biConsumer.accept(sender, args);
            return true;
        }
        return false;
    }


    /**
     * Method used to register the command
     * @param plugin main class of a plugin
     */
    public void bindWith(@NotNull JavaPlugin plugin) {
        plugin.getCommand(command).setExecutor(this);
    }
}
