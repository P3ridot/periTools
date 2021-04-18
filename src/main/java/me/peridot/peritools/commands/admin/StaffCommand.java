package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import me.peridot.peritools.data.configuration.ConfigurationManager;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffCommand extends PeriCommand {

    private final LangAPI lang;
    private final ConfigurationManager configurationManager;

    public StaffCommand(PeriTools plugin) {
        super(plugin, "staff", "/{LABEL} reload", "Narzedzia do zarzadzania pluginem dla administratora", "peritools.cmd.staff", "peritools");
        this.lang = plugin.getLang();
        this.configurationManager = plugin.getConfigurationManager();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (args[0].equalsIgnoreCase("help")) {
            this.lang.sendMessage(sender, "commands.staff.help", new Replacement("{LABEL}", label));
            return true;
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            if (!sender.hasPermission(getSubPermission("reload"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("reload")));
                return true;
            }
            getPlugin().reloadConfig();
            this.configurationManager.reloadConfigurations();
            getPlugin().initInventoryManager();
            this.lang.sendMessage(sender, "commands.staff.configuration_reloaded");
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] arguments = {"help", "reload"};

            StringUtil.copyPartialMatches(args[0], Arrays.asList(arguments), completions);

            if (!sender.hasPermission(getSubPermission("reload"))) {
                completions.remove("reload");
            }
        }

        return completions;
    }

    @Override
    public void correctUsage(CommandSender sender, String label) {
        this.lang.sendMessage(sender, "commands.staff.help", new Replacement("{LABEL}", label));
    }

}
