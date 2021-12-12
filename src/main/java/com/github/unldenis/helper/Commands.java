package com.github.unldenis.helper;


import org.jetbrains.annotations.NotNull;

public class Commands {

    /**
     * Method used to create a new command
     * @param command name of the command
     * @return CCommand type object
     */
    public static CCommand create(@NotNull String command) { return new CCommand(command); }
}
