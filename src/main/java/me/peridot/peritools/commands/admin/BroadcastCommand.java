package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.simple.ColorUtil;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BroadcastCommand extends PeriCommand {

    private final LangAPI lang;

    public BroadcastCommand(PeriTools plugin) {
        super(plugin, "broadcast", "/{LABEL} [chat/title/subtitle/actionbar] [wiadomosc]", "Powiadamia wszystkich w konkretny sposób", "peritools.cmd.broadcast", "bc", "uwaga", "alert");
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            return false;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(" ").append(args[i]);
        }
        String messageString = message.toString().replaceFirst(" ", "");

        if (messageString.isEmpty() || messageString.equalsIgnoreCase(" ")) {
            this.lang.sendMessage(sender, "errors.commands.broadcast.nomessage");
            return true;
        }

        if (sender.hasPermission(getSubPermission("color"))) {
            messageString = ColorUtil.color(messageString);
        }

        if (args[0].equalsIgnoreCase("chat") || args[0].equalsIgnoreCase("c")) {
            this.lang.broadcast("commands.broadcast.chat-format", new Replacement("{MESSAGE}", messageString));
        } else if (args[0].equalsIgnoreCase("title") || args[0].equalsIgnoreCase("t")) {
            this.lang.broadcast("commands.broadcast.title-format", new Replacement("{MESSAGE}", messageString));
        } else if (args[0].equalsIgnoreCase("subtitle") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("st")) {
            this.lang.broadcast("commands.broadcast.subtitle-format", new Replacement("{MESSAGE}", messageString));
        } else if (args[0].equalsIgnoreCase("actionbar") || args[0].equalsIgnoreCase("a")) {
            this.lang.broadcast("commands.broadcast.actionbar-format", new Replacement("{MESSAGE}", messageString));
        } else {
            return false;
        }

        this.lang.sendMessage(sender, "commands.broadcast.sent", new Replacement("{MESSAGE}", messageString));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] arguments = {"chat", "title", "subtitle", "actionbar"};
            StringUtil.copyPartialMatches(args[0], Arrays.asList(arguments), completions);
        } else {
            List<String> arguments = getPlugin().getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            StringUtil.copyPartialMatches(args[1], arguments, completions);
        }

        return completions;
    }

}
