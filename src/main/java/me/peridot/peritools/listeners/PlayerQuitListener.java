package me.peridot.peritools.listeners;

import me.peridot.peritools.PeriTools;
import me.peridot.peritools.user.UserCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PeriTools plugin;

    private final UserCache userCache;

    public PlayerQuitListener(PeriTools plugin) {
        this.plugin = plugin;
        this.userCache = plugin.getUserCache();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.userCache.createUser(player).getTemporaryData().clearTemporaryData();
        this.userCache.removeUser(player);
    }

}
