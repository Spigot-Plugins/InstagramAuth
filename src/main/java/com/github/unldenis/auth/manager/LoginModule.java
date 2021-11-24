package com.github.unldenis.auth.manager;

import com.github.unldenis.auth.InstagramAuth;
import com.github.unldenis.auth.actions.Login;
import com.github.unldenis.auth.obj.PlayerClient;
import com.github.unldenis.helper.Commands;
import com.github.unldenis.helper.Events;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.unldenis.helper.util.ChatUtil.color;
import static com.github.unldenis.helper.concurrent.BukkitFuture.sync;

public class LoginModule extends BaseModule{

    private Set<PlayerClient> playerClientSet = new HashSet<>();

    public LoginModule(@NotNull InstagramAuth plugin) {
        super(plugin);
    }

    public Optional<PlayerClient> find(@NotNull Player player) {
        return playerClientSet.stream()
                .filter(p->p.getPlayer().equals(player.getUniqueId()))
                .findFirst();
    }

    public boolean isLogging(@NotNull Player player) {
        Optional<PlayerClient> playerClient = find(player);
        return !playerClient.isPresent() || !playerClient.get().isLogged();
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
    }

    @Override
    protected void registerCommands() {
        Commands.create("login").handler(((sender, args) -> {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                Optional<PlayerClient> playerClient = find(player);
                if(playerClient.isPresent()) {
                    PlayerClient client = playerClient.get();
                    if(!client.isLogged()) {
                        //players is logging
                        if(args.length==2) {
                            String $username = args[0];
                            String $password = args[1];
                            player.sendMessage(color("&7Logging..."));

                            Login
                            .loginPlayer($username, $password)
                            .whenComplete(sync((igClient, throwable) -> {
                                if(throwable==null) {
                                    client.login(igClient);
                                    player.sendMessage(color("&7Login successful, Welcome &a" + igClient.getSelfProfile().getUsername()));
                                    if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
                                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                                }else{
                                    player.kickPlayer(color("&cThe password you entered is incorrect. Please try again or log in with Facebook."));
                                }
                            }));
                        }else{
                            player.sendMessage(color("&4Incorrect Usage. Use /login <username> <password>"));
                        }
                    }
                }
            }else{
                sender.sendMessage(color("&4Only players can run this command"));
            }
        })).bindWith(plugin);
    }


    @Override
    protected void registerEvents() {
        Events.subscribe(PlayerJoinEvent.class)
        .handler(e->{
            e.setJoinMessage(null);
            Player player = e.getPlayer();
            Optional<PlayerClient> playerClient = find(player);
            if(!playerClient.isPresent()) {
                playerClientSet.add(new PlayerClient(player.getUniqueId()));

               sendLoginMessage(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, true, false));
            }else{
                if(!playerClient.get().isLogged()) {
                    sendLoginMessage(player);
                }
            }
        }).bindWith(plugin);

        Events.subscribe(PlayerMoveEvent.class)
        .handler(e->{
            Player player = e.getPlayer();
            if(isLogging(player))
                e.setCancelled(true);
        }).bindWith(plugin);

        Events.subscribe(PlayerDropItemEvent.class)
        .handler(e->{
            Player player = e.getPlayer();
            if(isLogging(player))
                e.setCancelled(true);
        }).bindWith(plugin);

        Events.subscribe(InventoryClickEvent.class)
        .filter(e->e.getWhoClicked() instanceof Player)
        .handler(e->{
            Player player = (Player) e.getWhoClicked();
            if(isLogging(player))
                e.setCancelled(true);
        }).bindWith(plugin);
    }


    public Set<PlayerClient> getPlayerClientSet() {
        return playerClientSet;
    }

    private void sendLoginMessage(@NotNull Player player) {
        player.sendMessage("");
        player.sendMessage(color("&7⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
        player.sendMessage("");
        player.sendMessage(color("&7Login via your &5&lInstagram &7account"));
        player.sendMessage("");
        TextComponent message = new TextComponent(color("&7The plugin guarantees that the login is secure and no credentials are saved.\nAttention, any server can change the code and save the credentials, it is advisable to use a secondary account."));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/unldenis/InstagramAuth"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(color("Click to see the plugin"))));
        player.spigot().sendMessage(message);
        player.sendMessage("");
        player.sendMessage(color("&7⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
        player.sendMessage("");
        player.sendMessage(color("&7Use /login <&cusername&7> <&cpassword&7>"));
    }

}
