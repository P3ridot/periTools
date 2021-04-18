package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GamemodeCommand extends PeriCommand {

    private final LangAPI lang;
    private final ConfigurationFile messagesConfiguration;

    public GamemodeCommand(PeriTools plugin) {
        super(plugin, "gamemode", "/gamemode [0/1/2/3] <gracz>", "Ustawia tryb gry dla konkretnego gracza", "peritools.cmd.gamemode", "gm");
        this.lang = plugin.getLang();
        this.messagesConfiguration = plugin.getConfigurationManager().getMessagesConfiguration();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        GameMode gameMode;
        String gameModeString;
        if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival")) {
            if (!sender.hasPermission(getSubPermission("survival"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("survival")));
                return true;
            }
            gameMode = GameMode.SURVIVAL;
            gameModeString = this.messagesConfiguration.getColoredString("commands.gamemode.mode.survival");
        } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative")) {
            if (!sender.hasPermission(getSubPermission("creative"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("creative")));
                return true;
            }
            gameMode = GameMode.CREATIVE;
            gameModeString = this.messagesConfiguration.getColoredString("commands.gamemode.mode.creative");
        } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure")) {
            if (!sender.hasPermission(getSubPermission("adventure"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("adventure")));
                return true;
            }
            gameMode = GameMode.ADVENTURE;
            gameModeString = this.messagesConfiguration.getColoredString("commands.gamemode.mode.adventure");
        } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")) {
            if (!sender.hasPermission(getSubPermission("spectator"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("spectator")));
                return true;
            }
            gameMode = GameMode.SPECTATOR;
            gameModeString = this.messagesConfiguration.getColoredString("commands.gamemode.mode.spectator");
        } else {
            this.lang.sendMessage(sender, "errors.commands.gamemode.invalidgamemode");
            return true;
        }

        Player player;
        if (args.length >= 2) {
            if (!sender.hasPermission(getSubPermission("other"))) {
                this.lang.sendMessage(sender, "errors.simple.noperm", new Replacement("{PERMISSION}", getSubPermission("other")));
                return true;
            }
            player = getPlugin().getServer().getPlayer(args[1]);
            if (player == null) {
                this.lang.sendMessage(sender, "errors.simple.player-offline", new Replacement("{PLAYER}", args[1]));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                this.lang.sendSimpleMessage(sender, "errors.simple.noplayer");
                return true;
            }
            player = (Player) sender;
        }

        player.setGameMode(gameMode);

        if (sender.equals(player)) {
            this.lang.sendMessage(sender, "commands.gamemode.changed-own", new Replacement("{GAMEMODE}", gameModeString));
        } else {
            this.lang.sendMessage(player, "commands.gamemode.changed-by-other", new Replacement("{GAMEMODE}", gameModeString),
                    new Replacement("{CHANGER}", sender.getName()));
            this.lang.sendMessage(sender, "commands.gamemode.changed-other", new Replacement("{GAMEMODE}", gameModeString),
                    new Replacement("{PLAYER}", player.getName()));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] arguments = {"survival", "creative", "adventure", "spectator"};
            StringUtil.copyPartialMatches(args[0], Arrays.asList(arguments), completions);

            if (!sender.hasPermission(getSubPermission("survival"))) {
                completions.remove("survival");
            }
            if (!sender.hasPermission(getSubPermission("creative"))) {
                completions.remove("creative");
            }
            if (!sender.hasPermission(getSubPermission("adventure"))) {
                completions.remove("adventure");
            }
            if (!sender.hasPermission(getSubPermission("spectator"))) {
                completions.remove("spectator");
            }
        } else if (args.length == 2) {
            if (sender.hasPermission(getSubPermission("other"))) {
                List<String> arguments = getPlugin().getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                StringUtil.copyPartialMatches(args[1], arguments, completions);
            }
        }

        return completions;
    }

}
