package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import me.peridot.peritools.data.configuration.PluginConfiguration;
import me.peridot.peritools.data.storage.flat.DataStorage;
import me.peridot.peritools.schedulers.timers.SpawnTeleportTimer;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import me.peridot.peritools.utils.EventWaiter;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

public class SpawnCommand extends PeriCommand {

    private final LangAPI lang;
    private final PluginConfiguration pluginConfiguration;
    private final DataStorage dataStorage;
    private final UserCache userCache;
    private final EventWaiter eventWaiter;

    public SpawnCommand(PeriTools plugin) {
        super(plugin, "spawn", "/{LABEL} <gracz>", "Teleportuje konkretnego gracza na spawn", "peritools.cmd.spawn");
        this.lang = plugin.getLang();
        this.pluginConfiguration = plugin.getPluginConfiguration();
        this.dataStorage = plugin.getDataStorage();
        this.userCache = plugin.getUserCache();
        this.eventWaiter = plugin.getEventWaiter();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (this.dataStorage.spawnLocation == null) {
            this.lang.sendMessage(sender, "errors.commands.spawn.no-spawn-location");
            return true;
        }

        Player player;
        if (args.length >= 1) {
            if (!sender.hasPermission(getSubPermission("other"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("other")));
                return true;
            }
            player = getPlugin().getServer().getPlayer(args[0]);
            if (player == null) {
                this.lang.sendMessage(sender, "errors.simple.player-offline", new Replacement("{PLAYER}", args[0]));
                return true;
            }
            this.lang.sendMessage(sender, "commands.spawn.teleport-other", new Replacement("{PLAYER}", player.getName()));
        } else {
            if (!(sender instanceof Player)) {
                this.lang.sendSimpleMessage(sender, "errors.simple.noplayer");
                return true;
            }
            player = (Player) sender;
        }

        int time = this.pluginConfiguration.getInt("teleport-delay.spawn");
        if (sender.hasPermission(getSubPermission("nodelay"))) {
            time = 0;
        }

        User user = this.userCache.createUser(player);

        BukkitTask teleportationTimer = new SpawnTeleportTimer(getPlugin(), this.dataStorage.spawnLocation, player, sender, time).runTaskTimerAsynchronously(getPlugin(), 0, 20);
        user.getTemporaryData().safeSetTeleportationTimer(teleportationTimer);

        this.eventWaiter.waitForEvent(PlayerMoveEvent.class, EventPriority.NORMAL,
                event -> {
                    if (!event.getPlayer().equals(player)) {
                        return false;
                    }
                    Location from = event.getFrom();
                    Location to = event.getTo();
                    return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
                },
                event -> {
                    if (!getPlugin().getServer().getScheduler().isCurrentlyRunning(user.getTemporaryData().getTeleportationTimer().getTaskId())) {
                        return;
                    }
                    user.getTemporaryData().getTeleportationTimer().cancel();
                    this.lang.sendMessage(sender, "commands.spawn.teleport-cancelled", new Replacement("{PLAYER}", player.getName()));
                });

        return true;
    }

}
