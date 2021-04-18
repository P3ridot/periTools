package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.Pair;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import me.peridot.peritools.user.data.TemporaryData;
import org.bukkit.entity.Player;

public class TpdenyCommand extends PeriPlayerCommand {

    private final LangAPI lang;
    private final UserCache userCache;

    public TpdenyCommand(PeriTools plugin) {
        super(plugin, "tpdeny", "/{LABEL} <gracz>", "Odrzuca prosbe o teleportacje od podanego gracza", "peritools.cmd.tpdeny", "tpadeny");
        this.lang = plugin.getLang();
        this.userCache = plugin.getUserCache();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        User user = this.userCache.createUser(player);

        Player askingPlayer = null;
        if (args.length >= 1) {
            Player target = getPlugin().getServer().getPlayer(args[0]);
            if (target == null) {
                this.lang.sendMessage(player, "errors.simple.player-offline", new Replacement("{PLAYER}", args[0]));
                return true;
            }
            if (!user.getTemporaryData().getAsksForTeleportation().containsKey(target.getUniqueId())) {
                this.lang.sendMessage(player, "errors.commands.tpdeny.target-not-asking", new Replacement("{PLAYER}", target.getName()));
                return true;
            }
            askingPlayer = target;
        } else {
            Pair<Player, TemporaryData.TeleportationType> teleportationAsk = user.getTemporaryData().getFirstPendingAskForTeleportation();
            if (teleportationAsk != null) {
                askingPlayer = teleportationAsk.getKey();
            }
        }

        if (askingPlayer == null) {
            this.lang.sendMessage(player, "errors.commands.tpdeny.no-asking-player");
            return true;
        }

        this.lang.sendMessage(player, "commands.tpdeny.denied", new Replacement("{PLAYER}", askingPlayer.getName()));
        this.lang.sendMessage(askingPlayer, "commands.tpdeny.denied-asker", new Replacement("{PLAYER}", player.getName()));

        user.getTemporaryData().removeAskForTeleportation(askingPlayer);

        return true;
    }

}
