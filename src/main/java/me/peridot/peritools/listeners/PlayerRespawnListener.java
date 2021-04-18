package me.peridot.peritools.listeners;

import me.peridot.peritools.PeriTools;
import me.peridot.peritools.data.storage.flat.DataStorage;
import me.peridot.peritools.user.UserCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final PeriTools plugin;

    private final DataStorage dataStorage;
    private final UserCache userCache;

    public PlayerRespawnListener(PeriTools plugin) {
        this.plugin = plugin;
        this.dataStorage = plugin.getDataStorage();
        this.userCache = plugin.getUserCache();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (this.dataStorage.spawnLocation != null) {
            event.setRespawnLocation(this.dataStorage.spawnLocation);
        }
    }

}
