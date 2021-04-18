package me.peridot.peritools.listeners;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    private final PeriTools plugin;

    private final LangAPI lang;
    private final UserCache userCache;

    public PlayerCommandPreprocessListener(PeriTools plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLang();
        this.userCache = plugin.getUserCache();
    }

    @EventHandler(ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            for (Player loopPlayer : this.plugin.getServer().getOnlinePlayers()) {
                if (loopPlayer.equals(player)) continue;
                if (!loopPlayer.hasPermission("peritools.cmd.commandsspy.see")) continue;
                User user = this.userCache.createUser(loopPlayer);
                if (!user.getSettings().isCommandsSpy()) continue;

                this.lang.sendMessage(loopPlayer, "commands.commandsspy.log", new Replacement("{PLAYER}", player.getName()),
                        new Replacement("{COMMAND}", event.getMessage()));
            }
        });
    }

}
