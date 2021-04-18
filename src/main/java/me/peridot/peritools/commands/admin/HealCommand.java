package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class HealCommand extends PeriCommand {

    private final LangAPI lang;

    public HealCommand(PeriTools plugin) {
        super(plugin, "heal", "/{LABEL} <gracz>", "Ulecza konkretnego gracza, daje mu pelne zycie, nasycenie oraz usuwa z niego wszystkie efekty", "peritools.cmd.heal", "ulecz");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        Player player;
        if (args.length >= 1) {
            if (!sender.hasPermission(getSubPermission("other"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("other")));
                return true;
            }
            player = getPlugin().getServer().getPlayer(args[1]);
            if (player == null) {
                this.lang.sendMessage(sender, "errors.simple.playeroffline", new Replacement("{PLAYER}", args[1]));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                this.lang.sendSimpleMessage(sender, "errors.simple.noplayer");
                return true;
            }
            player = (Player) sender;
        }

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20F);
        player.setFireTicks(0);

        for (PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }

        if (sender.equals(player)) {
            this.lang.sendMessage(sender, "commands.heal.healed-own");
        } else {
            this.lang.sendMessage(player, "commands.heal.healed-by-other", new Replacement("{HEALER}", sender.getName()),
                    new Replacement("{HEALER}", sender.getName()));
            this.lang.sendMessage(sender, "commands.heal.heal-other", new Replacement("{PLAYER}", player.getName()),
                    new Replacement("{PLAYER}", player.getName()));
        }

        return true;
    }

}
