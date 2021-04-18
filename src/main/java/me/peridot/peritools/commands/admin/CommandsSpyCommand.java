package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandsSpyCommand extends PeriPlayerCommand {

    private final LangAPI lang;
    private final ConfigurationFile messagesConfiguration;
    private final UserCache userCache;

    public CommandsSpyCommand(PeriTools plugin) {
        super(plugin, "commandsspy", "/{LABEL} <on/off>", "Wlacza/Wylacza podsluchiwanie komend jakie wykonuja gracze", "peritools.cmd.commandsspy", "cs");
        this.lang = plugin.getLang();
        this.messagesConfiguration = plugin.getConfigurationManager().getMessagesConfiguration();
        this.userCache = plugin.getUserCache();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        User user = this.userCache.createUser(player);

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("wlacz")) {
                user.getSettings().setCommandsSpy(true);
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("wylacz")) {
                user.getSettings().setCommandsSpy(false);
            } else {
                return false;
            }
        } else {
            user.getSettings().toggleCommandsSpy();
        }

        String status = user.getSettings().isCommandsSpy() ? this.messagesConfiguration.getColoredString("commands.commandsspy.status.enabled") : this.messagesConfiguration.getColoredString("commands.commandsspy.status.disabled");

        this.lang.sendMessage(player, "commands.commandsspy.changed", new Replacement("{STATUS}", status));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] arguments = {"on", "off"};
            StringUtil.copyPartialMatches(args[0], Arrays.asList(arguments), completions);
        }

        return completions;
    }

}
