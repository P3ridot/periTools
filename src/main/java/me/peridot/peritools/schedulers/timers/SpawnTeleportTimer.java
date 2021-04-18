package me.peridot.peritools.schedulers.timers;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnTeleportTimer extends BukkitRunnable {

    private final PeriTools plugin;
    private final LangAPI lang;

    private final Location location;
    private final Player player;
    private final CommandSender teleporter;
    private int timeLeft;

    public SpawnTeleportTimer(PeriTools plugin, Location location, Player player, CommandSender teleporter, int time) {
        this.plugin = plugin;
        this.lang = plugin.getLang();

        this.location = location;
        this.player = player;
        this.teleporter = teleporter;
        this.timeLeft = time;
    }

    @Override
    public void run() {
        if (this.timeLeft <= 0) {
            this.cancel();
            this.player.teleport(this.location, PlayerTeleportEvent.TeleportCause.COMMAND);
            if (this.teleporter.equals(this.player)) {
                this.lang.sendMessage(this.player, "commands.spawn.teleported-own", new Replacement("{PLAYER}", this.player.getName()));
            } else {
                this.lang.sendMessage(this.player, "commands.spawn.teleported-by-other", new Replacement("{TELEPORTER}", this.teleporter.getName()));
            }
            return;
        }
        this.lang.sendMessage(this.player, "commands.spawn.teleporting", new Replacement("{TIME}", this.timeLeft));
        this.timeLeft--;
    }

}
