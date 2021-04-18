package me.peridot.peritools.commands.basic.items;

import api.peridot.periapi.configuration.langapi.LangAPI;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ResetNameCommand extends PeriPlayerCommand {

    private final LangAPI lang;

    public ResetNameCommand(PeriTools plugin) {
        super(plugin, "resetname", "/{LABEL}", "Usuwa nazwe z trzmanego w rece przedmiotu", "peritools.cmd.resetname", "usunnazwe");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            this.lang.sendMessage(player, "errors.simple.empty-hand");
            return true;
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(null);
        item.setItemMeta(itemMeta);
        player.updateInventory();

        this.lang.sendMessage(player, "commands.items.resetname.reseted");

        return true;
    }

}
