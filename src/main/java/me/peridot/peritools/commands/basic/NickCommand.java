package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.simple.ColorUtil;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand extends PeriCommand {

    private final LangAPI lang;
    private final ConfigurationFile messagesConfiguration;
    private final UserCache userCache;

    public NickCommand(PeriTools plugin) {
        super(plugin, "nick", "/nick [kolory][nick] <gracz>", "Zmienia nick dla konkretnego gracza", "peritools.cmd.nick", "nickname");
        this.lang = plugin.getLang();
        this.messagesConfiguration = plugin.getConfigurationManager().getMessagesConfiguration();
        this.userCache = plugin.getUserCache();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Player player;
        if (args.length >= 2) {
            if (!sender.hasPermission(getSubPermission("other"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("other")));
                return true;
            }
            player = getPlugin().getServer().getPlayer(args[1]);
            if (player == null) {
                this.lang.sendMessage(sender, "errors.simple.player-offline", new Replacement("{PLAYER}", args[1]));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                this.lang.sendSimpleMessage(sender, "errors.simple.noplayer");
                return true;
            }
            player = (Player) sender;
        }
        String newDisplayName = ColorUtil.color(args[0] + ChatColor.RESET);

        if (!ChatColor.stripColor(newDisplayName).equalsIgnoreCase(player.getName())) {
            if (args.length >= 2) {
                if (!sender.hasPermission(getSubPermission("other.fake"))) {
                    this.lang.sendMessage(sender, "errors.commands.nick.fake");
                    return true;
                }
            } else {
                if (!sender.hasPermission(getSubPermission("fake"))) {
                    this.lang.sendMessage(sender, "errors.commands.nick.fake-other");
                    return true;
                }
            }
        }

        User user = this.userCache.createUser(player);

        player.setDisplayName(newDisplayName);
        user.setDisplayName(newDisplayName);

        if (sender.equals(player)) {
            this.lang.sendMessage(sender, "commands.nick.changed-own", new Replacement("{NEWNICKNAME}", newDisplayName));
        } else {
            this.lang.sendMessage(player, "commands.nick.changed-by-other", new Replacement("{NEWNICKNAME}", newDisplayName),
                    new Replacement("{CHANGER}", sender.getName()));
            this.lang.sendMessage(sender, "commands.nick.changed-other", new Replacement("{NEWNICKNAME}", newDisplayName),
                    new Replacement("{PLAYER}", player.getName()));
        }

        return true;
    }

}
