package me.peridot.peritools.commands;

import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;

import java.util.Arrays;

public abstract class PeriCommand extends Command implements PluginIdentifiableCommand {

    protected final PeriTools plugin;

    protected final String name;
    protected final String usage;
    protected final String description;
    protected final String permission;

    protected PeriCommand(PeriTools plugin, String name, String usage, String description, String permission, String... aliases) {
        super(name, description, usage, Arrays.asList(aliases));
        this.plugin = plugin;
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.permission = permission;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(this.permission)) {
            this.plugin.getLang().sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", this.permission));
            return true;
        }

        boolean success = this.onExecute(sender, label, args);

        if (!success && this.usage.length() > 0) {
            this.correctUsage(sender, label);
        }

        return success;
    }

    public abstract boolean onExecute(CommandSender sender, String label, String[] args);

    @Override
    public PeriTools getPlugin() {
        return this.plugin;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUsage() {
        return StringUtils.replace(this.usage, "{LABEL}", getName());
    }

    public String getUsage(String label) {
        return StringUtils.replace(this.usage, "{LABEL}", label);
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    public String getSubPermission(String suffix) {
        return this.permission + "." + suffix;
    }

    public void correctUsage(CommandSender sender, String label) {
        this.plugin.getLang().sendMessage(sender, "errors.simple.correctusage", new Replacement("{USAGE}", getUsage(label)));
    }

}
