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

import java.util.List;

public class ChangeLoreCommand extends PeriPlayerCommand {

    private final LangAPI lang;

    public ChangeLoreCommand(PeriTools plugin) {
        super(plugin, "changelore", "/{LABEL} [linijka ktora ma zostac zmieniona] [tresc na jaka ma zostac zmieniona]", "Zmienia podana linijke w opisie trzymanego w rece przedmiotu", "peritools.cmd.changelore", "zmienopis");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        if (args.length < 2) {
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

        StringBuilder lore = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            lore.append(" " + args[i]);
        }
        String loreString = lore.toString().replaceFirst(" ", "");

        if (player.hasPermission(getSubPermission("color"))) {
            loreString = ColorUtil.color(loreString);
        }

        ItemMeta itemMeta = item.getItemMeta();
        List<String> finalLore = itemMeta.getLore();
        try {
            finalLore.set(line, loreString);
        } catch (Exception ignored) {
        }
        itemMeta.setLore(finalLore);
        item.setItemMeta(itemMeta);
        player.updateInventory();

        this.lang.sendMessage(player, "commands.items.changelore.changed", new Replacement("{LORE}", loreString),
                new Replacement("{LINE}", line + 1));

        return true;
    }

}
