package com.github.unldenis.auth.manager;

import com.github.unldenis.auth.InstagramAuth;
import com.github.unldenis.auth.actions.Account;
import com.github.unldenis.auth.obj.PlayerClient;
import com.github.unldenis.helper.Commands;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


import static com.github.unldenis.helper.concurrent.BukkitFuture.sync;
import static com.github.unldenis.helper.util.ChatUtil.color;


public class FollowServerModule extends BaseModule{

    private String serverAccount;

    public FollowServerModule(@NotNull InstagramAuth plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        if(plugin.getConfig().getBoolean("server-account.enabled")) {

            serverAccount = plugin.getConfig().getString("server-account.profile");
            registerCommands();

            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getLoginModule().getPlayerClientSet()
                    .stream()
                    .filter(PlayerClient::isLogged)
                    .forEach(client-> {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(client.getPlayer());
                        if(offlinePlayer.isOnline()) {
                            Player player = offlinePlayer.getPlayer();
                            TextComponent message = new TextComponent(color("&7Follow our &aofficial &r&7page, in the future you will have some benefits!"));
                            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/followserverpage"));
                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(color("Click to follow automatically"))));
                            player.spigot().sendMessage(message);
                        }
                    });
                }
            }
            .runTaskTimer(plugin, 0L, 20L*plugin.getConfig().getInt("server-account.timer-message"));
        }
    }

    @Override
    public void registerCommands() {
        Commands.create("followserverpage")
        .handler(((sender, args) -> {
            if(sender instanceof Player && args.length==0) {
                Player player = (Player) sender;
                PlayerClient client = plugin.getLoginModule().find(player).get();

                player.sendMessage(color("&7Following..."));

                Account
                .findUserAction(client.getIgClient(), serverAccount)
                //.thenCompose(userAction -> BukkitFuture.supplyAsync(()-> userAction.getFriendship().join()))
                .thenCompose(userAction -> Account.follow(client.getIgClient(), userAction.getUser().getPk()) )
                .whenComplete(sync((friendshipStatusResponse, throwable) -> {
                    if(throwable==null) {
                        player.sendMessage(color("&aPage followed correctly"));
                    }else{
                        player.sendMessage(color("&cError following account"));
                    }
                }));
            }
        })).bindWith(plugin);
    }

}
