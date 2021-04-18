package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.langapi.LangAPI;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HatCommand extends PeriPlayerCommand {

    private final LangAPI lang;

    public HatCommand(PeriTools plugin) {
        super(plugin, "hat", "/{LABEL}", "Ustawia graczowi przedmiot ktory trzyma w rece jako helm", "peritools.cmd.hat", "czapka");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        ItemStack itemInHand = player.getItemInHand();
        ItemStack helmet = player.getInventory().getHelmet();

        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            this.lang.sendMessage(player, "errors.simple.empty-hand");
            return true;
        }

        player.setItemInHand(helmet);
        player.getInventory().setHelmet(itemInHand);

        this.lang.sendMessage(player, "commands.hat.changed");

        return true;
    }

}
