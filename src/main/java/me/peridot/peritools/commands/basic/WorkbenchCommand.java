package me.peridot.peritools.commands.basic;

import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import org.bukkit.entity.Player;

public class WorkbenchCommand extends PeriPlayerCommand {

    public WorkbenchCommand(PeriTools plugin) {
        super(plugin, "workbench", "/{LABEL}", "Otwiera wirutalny crafting", "peritools.cmd.workbench", "wb", "wbench", "craft");
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        player.openWorkbench(null, true);
        return true;
    }

}
