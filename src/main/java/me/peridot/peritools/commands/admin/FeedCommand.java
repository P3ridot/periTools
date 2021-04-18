package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand extends PeriCommand {

    private final LangAPI lang;

    public FeedCommand(PeriTools plugin) {
        super(plugin, "feed", "/{LABEL} <gracz>", "Nasyca konkretnego gracza", "peritools.cmd.feed", "nakarm");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        Player player;
        if (args.length >= 1) {
            if (!sender.hasPermission(getSubPermission("other"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("other")));
                return true;
            }
            player = getPlugin().getServer().getPlayer(args[1]);
            if (player == null) {
                this.lang.sendMessage(sender, "errors.simple.playeroffline", new Replacement("{PLAYER}", args[1]));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                this.lang.sendSimpleMessage(sender, "errors.simple.noplayer");
                return true;
            }
            player = (Player) sender;
        }
        player.setFoodLevel(20);
        player.setSaturation(20F);

        if (sender.equals(player)) {
            this.lang.sendMessage(sender, "commands.feed.fed-own");
        } else {
            this.lang.sendMessage(player, "commands.feed.fed-by-other", new Replacement("{HEALER}", sender.getName()),
                    new Replacement("{FEEDER}", sender.getName()));
            this.lang.sendMessage(sender, "commands.feed.fed-other", new Replacement("{PLAYER}", player.getName()),
                    new Replacement("{PLAYER}", player.getName()));
        }

        return true;
    }

}
