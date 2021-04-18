package me.peridot.peritools.commands.basic.items;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.simple.ColorUtil;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand extends PeriPlayerCommand {

    private final LangAPI lang;

    public RenameCommand(PeriTools plugin) {
        super(plugin, "rename", "/{LABEL} [nowa nazwa przedmiotu]", "Zmienia nazwe trzymanego w rece przedmiotu", "peritools.cmd.rename", "setname", "name", "zmiennazwe");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            this.lang.sendMessage(player, "errors.simple.empty-hand");
            return true;
        }

        StringBuilder name = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            name.append(" " + args[i]);
        }
        String nameString = name.toString().replaceFirst(" ", "");

        if (player.hasPermission(getSubPermission("color"))) {
            nameString = ColorUtil.color(nameString);
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(nameString);
        item.setItemMeta(itemMeta);
        player.updateInventory();

        this.lang.sendMessage(player, "commands.items.rename.renamed", new Replacement("{NAME}", nameString));

        return true;
    }

}
