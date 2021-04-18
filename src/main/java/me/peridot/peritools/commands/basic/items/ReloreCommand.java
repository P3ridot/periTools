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

import java.util.Arrays;
import java.util.List;

public class ReloreCommand extends PeriPlayerCommand {

    private final LangAPI lang;

    public ReloreCommand(PeriTools plugin) {
        super(plugin, "relore", "/{LABEL} [nowy opis przedmiotu]", "Zmienia opis trzymanego w rece przedmiotu", "peritools.cmd.relore", "setlore", "lore", "zmienopis");
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

        StringBuilder lore = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            lore.append(" " + args[i]);
        }
        String loreString = lore.toString().replaceFirst(" ", "");

        List<String> loreList = Arrays.asList(loreString.split("%nl%"));

        if (player.hasPermission(getSubPermission("color"))) {
            loreString = ColorUtil.color(loreString);
            loreList = ColorUtil.color(loreList);
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        player.updateInventory();

        this.lang.sendMessage(player, "commands.items.relore.relored", new Replacement("{LORE}", loreString));

        return true;
    }

}
