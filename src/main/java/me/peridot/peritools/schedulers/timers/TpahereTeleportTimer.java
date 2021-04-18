package me.peridot.peritools.schedulers.timers;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TpahereTeleportTimer extends BukkitRunnable {

    private final PeriTools plugin;
    private final LangAPI lang;

    private final Location location;
    private final Player player;
    private final Player target;
    private int timeLeft;

    public TpahereTeleportTimer(PeriTools plugin, Location location, Player player, Player target, int time) {
        this.plugin = plugin;
        this.lang = plugin.getLang();

        this.location = location;
        this.player = player;
        this.target = target;
        this.timeLeft = time;
    }

    @Override
    public void run() {
        if (this.timeLeft <= 0) {
            this.cancel();
            this.player.teleport(this.location, PlayerTeleportEvent.TeleportCause.COMMAND);
            this.lang.sendMessage(this.player, "commands.tpahere.teleported", new Replacement("{PLAYER}", this.target.getName()));
            return;
        }
        this.lang.sendMessage(this.player, "commands.tpahere.teleporting", new Replacement("{TIME}", this.timeLeft),
                new Replacement("{PLAYER}", this.target.getName()));
        this.timeLeft--;
    }

}
