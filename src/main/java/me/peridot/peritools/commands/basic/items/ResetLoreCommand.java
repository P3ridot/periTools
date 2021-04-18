package me.peridot.peritools.commands.basic.items;

import api.peridot.periapi.configuration.langapi.LangAPI;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ResetLoreCommand extends PeriPlayerCommand {

    private final LangAPI lang;

    public ResetLoreCommand(PeriTools plugin) {
        super(plugin, "resetlore", "/{LABEL}", "Usuwa opis z trzmanego w rece przedmiotu", "peritools.cmd.resetlore", "usunopis");
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
        itemMeta.setLore(null);
        item.setItemMeta(itemMeta);
        player.updateInventory();

        this.lang.sendMessage(player, "commands.items.resetlore.reseted");

        return true;
    }

}
