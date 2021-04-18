package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.ConfigurationFile;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import org.bukkit.entity.Player;

public class TrashCommand extends PeriPlayerCommand {

    private final ConfigurationFile inventoriesConfiguration;

    public TrashCommand(PeriTools plugin) {
        super(plugin, "trash", "/{LABEL}", "Otwiera smietnik do ktorego mozna wyrzucic niepotrzebne przedmioty", "peritools.cmd.trash", "smietnik");
        this.inventoriesConfiguration = plugin.getInventoriesConfiguration();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        player.openInventory(getPlugin().getServer().createInventory(null, this.inventoriesConfiguration.getInt("trash.size") * 9, this.inventoriesConfiguration.getColoredString("trash.title")));
        return true;
    }

}
