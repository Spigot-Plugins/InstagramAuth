package com.github.unldenis.helper;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class Events {

    /**
     * Method used to create a new event
     * @param clazz class of type org.bukkit.event.Event
     * @return CEvent type object
     */
    public static <T extends Event> CEvent<T> subscribe(@NotNull Class<T> clazz) {
        return new CEvent<T>(clazz);
    }
}
