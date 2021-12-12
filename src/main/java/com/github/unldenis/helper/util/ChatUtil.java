package com.github.unldenis.helper.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ChatUtil {

    /**
     * Method that returns a message colored by &
     * @param text original message
     * @return converted message
     */
    public static String color(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }


}
