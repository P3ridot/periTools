package me.peridot.peritools.commands;

import me.peridot.peritools.PeriTools;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PeriPlayerCommand extends PeriCommand {

    protected PeriPlayerCommand(PeriTools plugin, String name, String usage, String description, String permission, String... aliases) {
        super(plugin, name, usage, description, permission, aliases);
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.plugin.getLang().sendSimpleMessage(sender, "errors.simple.noplayer");
            return true;
        }
        Player player = (Player) sender;
        return this.onExecute(player, label, args);
    }

    public abstract boolean onExecute(Player player, String label, String[] args);

}
