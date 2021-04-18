package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.Pair;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import me.peridot.peritools.data.configuration.PluginConfiguration;
import me.peridot.peritools.schedulers.timers.TpaTeleportTimer;
import me.peridot.peritools.schedulers.timers.TpahereTeleportTimer;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import me.peridot.peritools.user.data.TemporaryData;
import me.peridot.peritools.utils.EventWaiter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

public class TpacceptCommand extends PeriPlayerCommand {

    private final LangAPI lang;
    private final PluginConfiguration pluginConfiguration;
    private final UserCache userCache;
    private final EventWaiter eventWaiter;

    public TpacceptCommand(PeriTools plugin) {
        super(plugin, "tpaccept", "/{LABEL} <gracz>", "Akceptuje prosbe o teleportacje od podanego gracza", "peritools.cmd.tpaccept", "tpaaccept");
        this.lang = plugin.getLang();
        this.pluginConfiguration = plugin.getPluginConfiguration();
        this.userCache = plugin.getUserCache();
        this.eventWaiter = plugin.getEventWaiter();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        User user = this.userCache.createUser(player);

        Player askingPlayer;
        TemporaryData.TeleportationType teleportationType;
        if (args.length >= 1) {
            Player target = getPlugin().getServer().getPlayer(args[0]);
            if (target == null) {
                this.lang.sendMessage(player, "errors.simple.player-offline", new Replacement("{PLAYER}", args[0]));
                return true;
            }
            if (!user.getTemporaryData().getAsksForTeleportation().containsKey(target.getUniqueId())) {
                this.lang.sendMessage(player, "errors.commands.tpaccept.target-not-asking", new Replacement("{PLAYER}", target.getName()));
                return true;
            }
            askingPlayer = target;
            teleportationType = user.getTemporaryData().getAsksForTeleportation().get(target.getUniqueId());
        } else {
            Pair<Player, TemporaryData.TeleportationType> teleportationAsk = user.getTemporaryData().getFirstPendingAskForTeleportation();
            if (teleportationAsk != null) {
                askingPlayer = teleportationAsk.getKey();
                teleportationType = teleportationAsk.getValue();
            } else {
                askingPlayer = null;
                teleportationType = null;
            }
        }

        if (askingPlayer == null) {
            this.lang.sendMessage(player, "errors.commands.tpaccept.no-asking-player");
            return true;
        }

        this.lang.sendMessage(player, "commands.tpaccept.accepted", new Replacement("{PLAYER}", askingPlayer.getName()));

        user.getTemporaryData().removeAskForTeleportation(askingPlayer.getUniqueId());

        if (teleportationType == TemporaryData.TeleportationType.NORMAL) {
            int time = this.pluginConfiguration.getInt("teleport-delay.tpa");
            if (askingPlayer.hasPermission("peritools.cmd.tpa.nodelay")) {
                time = 0;
            }

            BukkitTask teleportationTimer = new TpaTeleportTimer(getPlugin(), player.getLocation(), askingPlayer, player, time).runTaskTimerAsynchronously(getPlugin(), 0, 20);
            user.getTemporaryData().safeSetTeleportationTimer(teleportationTimer);

            this.eventWaiter.waitForEvent(PlayerMoveEvent.class, EventPriority.NORMAL,
                    event -> {
                        if (!event.getPlayer().equals(askingPlayer)) {
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
                        this.lang.sendMessage(askingPlayer, "commands.tpa.teleport-cancelled", new Replacement("{PLAYER}", player.getName()));
                    });
        } else if (teleportationType == TemporaryData.TeleportationType.HERE) {
            int time = this.pluginConfiguration.getInt("teleport-delay.tpahere");
            if (player.hasPermission("peritools.cmd.tpahere.nodelay")) {
                time = 0;
            }

            BukkitTask teleportationTimer = new TpahereTeleportTimer(getPlugin(), askingPlayer.getLocation(), player, askingPlayer, time).runTaskTimerAsynchronously(getPlugin(), 0, 20);
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
                        this.lang.sendMessage(player, "commands.tpahere.teleport-cancelled", new Replacement("{PLAYER}", askingPlayer.getName()));
                    });
        }
        return true;
    }

}
