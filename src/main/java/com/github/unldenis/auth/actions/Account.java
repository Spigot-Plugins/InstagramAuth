package com.github.unldenis.auth.actions;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.users.UserAction;
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsActionRequest;
import com.github.instagram4j.instagram4j.responses.friendships.FriendshipStatusResponse;
import com.github.unldenis.helper.concurrent.BukkitFuture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Account {

    public static CompletableFuture<UserAction> findUserAction(@NotNull IGClient client, @NotNull String username) {
        return BukkitFuture.supplyAsync(()-> client.actions().users().findByUsername(username).join());
    }

    public static CompletableFuture<FriendshipStatusResponse> follow(@NotNull IGClient client, long pk) {
        return BukkitFuture.supplyAsync(()-> new FriendshipsActionRequest(pk, FriendshipsActionRequest.FriendshipsAction.CREATE).execute(client).join());
    }
}
