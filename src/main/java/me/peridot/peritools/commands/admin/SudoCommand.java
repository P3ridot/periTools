package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SudoCommand extends PeriCommand {

    private final LangAPI lang;

    public SudoCommand(PeriTools plugin) {
        super(plugin, "sudo", "/{LABEL} [command/chat] [player] [tresc]", "Wysyla komende/wiadomosc jako konkretny gracz", "peritools.cmd.sudo", "wykonaj");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            return false;
        }

        Player executor = getPlugin().getServer().getPlayer(args[1]);
        if (executor == null) {
            this.lang.sendMessage(sender, "errors.simple.player-offline", new Replacement("{PLAYER}", args[1]));
            return true;
        }

        StringBuilder content = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            content.append(" " + args[i]);
        }
        String contentString = content.toString().replaceFirst(" ", "");

        if (contentString == null || contentString.isEmpty() || contentString.equalsIgnoreCase(" ")) {
            this.lang.sendMessage(sender, "errors.commands.sudo.nocontent");
            return true;
        }

        if (args[0].equalsIgnoreCase("command") || args[2].equalsIgnoreCase("komenda")) {
            getPlugin().getServer().dispatchCommand(executor, contentString);
            this.lang.sendMessage(sender, "commands.sudo.command", new Replacement("{PLAYER}", executor.getName()),
                    new Replacement("{COMMAND}", contentString));
        } else if (args[0].equalsIgnoreCase("chat")) {
            executor.chat(contentString);
            this.lang.sendMessage(sender, "commands.sudo.chat", new Replacement("{PLAYER}", executor.getName()),
                    new Replacement("{MESSAGE}", contentString));
        } else {
            return false;
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] arguments = {"command", "chat"};
            StringUtil.copyPartialMatches(args[0], Arrays.asList(arguments), completions);
        } else {
            List<String> arguments = getPlugin().getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            StringUtil.copyPartialMatches(args[1], arguments, completions);
        }

        return completions;
    }

}
