package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import me.peridot.peritools.data.configuration.PluginConfiguration;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import me.peridot.peritools.user.data.TemporaryData;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TpaCommand extends PeriPlayerCommand {

    private final LangAPI lang;
    private final PluginConfiguration pluginConfiguration;
    private final UserCache userCache;

    public TpaCommand(PeriTools plugin) {
        super(plugin, "tpa", "/{LABEL} [gracz]", "Wysyla prosbe o teleportacje do podanego gracza", "peritools.cmd.tpa", "tpask");
        this.lang = plugin.getLang();
        this.pluginConfiguration = plugin.getPluginConfiguration();
        this.userCache = plugin.getUserCache();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Player target = getPlugin().getServer().getPlayer(args[0]);
        if (target == null) {
            this.lang.sendMessage(player, "errors.simple.player-offline", new Replacement("{PLAYER}", args[0]));
            return true;
        }

        User targetUser = this.userCache.createUser(target);

        targetUser.getTemporaryData().askForTeleportation(player, TemporaryData.TeleportationType.NORMAL);

        this.lang.sendMessage(player, "commands.tpa.sender", new Replacement("{PLAYER}", target.getName()));
        this.lang.sendMessage(target, "commands.tpa.receiver", new Replacement("{PLAYER}", player.getName()),
                new Replacement("{TIME}", this.pluginConfiguration.getInt("accept-time.tpa")));

        UUID targetUuid = player.getUniqueId();
        getPlugin().getServer().getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> {
            try {
                if (targetUser.getTemporaryData().getAsksForTeleportation().containsKey(targetUuid)) {
                    targetUser.getTemporaryData().removeAskForTeleportation(targetUuid);
                }
            } catch (Exception ignored) {
            }
        }, this.pluginConfiguration.getInt("accept-time.tpa") * 20);

        return true;
    }

}
