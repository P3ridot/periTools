package me.peridot.peritools.commands.basic.items;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RemoveLoreCommand extends PeriPlayerCommand {

    private final LangAPI lang;

    public RemoveLoreCommand(PeriTools plugin) {
        super(plugin, "removelore", "/{LABEL} [numer linijki]", "Usuwa konkretna linijke z opisu przedmiotu trzymanego w rece", "peritools.cmd.removelore", "usunopis");
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

        int line = 1;
        try {
            line = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            this.lang.sendMessage(player, "errors.simple.not-number", new Replacement("{VALUE}", args[0]));
            return true;
        }
        line = line - 1;

        ItemMeta itemMeta = item.getItemMeta();
        List<String> finalLore = itemMeta.getLore();
        try {
            finalLore.remove(line);
        } catch (Exception ignored) {
        }
        itemMeta.setLore(finalLore);
        item.setItemMeta(itemMeta);
        player.updateInventory();

        this.lang.sendMessage(player, "commands.items.removelore.removed", new Replacement("{LINE}", line));

        return true;
    }

}
