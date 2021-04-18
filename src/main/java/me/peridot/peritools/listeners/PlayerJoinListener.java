package me.peridot.peritools.listeners;

import me.peridot.peritools.PeriTools;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PeriTools plugin;

    private final UserCache userCache;

    public PlayerJoinListener(PeriTools plugin) {
        this.plugin = plugin;
        this.userCache = plugin.getUserCache();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.userCache.createUser(player);
        player.setDisplayName(user.getDisplayName());
    }

}
