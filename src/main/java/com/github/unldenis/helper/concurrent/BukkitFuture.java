package com.github.unldenis.helper.concurrent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BukkitFuture {

    private static Plugin plugin;

    /**
     * One-time call method to register your plugin instance. This is to avoid passing it as a parameter for all the methods present in BukkitFuture class.
     * @param plugin your plugin instance
     */
    public static void bindWith(@NotNull Plugin plugin) {
        BukkitFuture.plugin = plugin;
    }

    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule with the value obtained by calling the given Supplier.
     * @param supplier a function returning the value to be used to complete the returned CompletableFuture
     * @param <T> the function's return type
     */
    public static <T> CompletableFuture<T> supplyAsync(@NotNull Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule after it runs the given action.
     * @param runnable the action to run before completing the returned CompletableFuture
     */
    public static CompletableFuture<Void> runAsync(@NotNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                runnable.run();
                future.complete(null);
            }catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule with the value obtained by calling the given Supplier.
     * @param supplier a function returning the value to be used to complete the returned CompletableFuture
     * @param <T> the function's return type
     */
    public static <T> CompletableFuture<T> supplySync(@NotNull Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (Bukkit.isPrimaryThread()) {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    future.complete(supplier.get());
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        }
        return future;
    }

    /**
     * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule after it runs the given action.
     * @param runnable the action to run before completing the returned CompletableFuture
     */
    public static CompletableFuture<Void> runSync(@NotNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if(Bukkit.isPrimaryThread()) {
            try {
                runnable.run();
                future.complete(null);
            }catch (Throwable t) {
                future.completeExceptionally(t);
            }
        }else{
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    runnable.run();
                    future.complete(null);
                }catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        }
        return future;
    }

    /**
     * Helper method to avoid boilerplate in CompletableFuture#whenComplete .
     * @param action the BiConsumer of the whenComplete method that will be executed in the runnable synchronously.
     * @param <T> the function's return type
     */
    public static <T> BiConsumer<T, ? super Throwable> sync(@NotNull BiConsumer<T, ? super Throwable> action) {
        return (BiConsumer<T, Throwable>) (t, throwable) -> runSync(() -> action.accept(t, throwable));
    }
}
