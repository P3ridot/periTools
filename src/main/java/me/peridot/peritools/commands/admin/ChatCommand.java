package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import me.peridot.peritools.data.storage.flat.DataStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatCommand extends PeriCommand {

    private final LangAPI lang;
    private final DataStorage dataStorage;

    public ChatCommand(PeriTools plugin) {
        super(plugin, "chat", "/{LABEL} clear/on/off", "Czysci/Wlacza/Wylacza chat na serwerze", "peritools.cmd.chat", "c");
        this.lang = plugin.getLang();
        this.dataStorage = plugin.getDataStorage();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                getPlugin().getInventoryManager().getChatInventory().open(player);
                return true;
            } else {
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("wyczysc")) {
            for (Player loopPlayer : this.plugin.getServer().getOnlinePlayers()) {
                for (int i = 0; i < 101; i++) {
                    loopPlayer.sendMessage(" ");
                }
            }
            this.lang.broadcast("commands.chat.cleared", new Replacement("{ADMIN}", sender.getName()));
            this.lang.sendMessage(this.plugin.getServer().getConsoleSender(), "commands.chat.cleared", new Replacement("{ADMIN}", sender.getName()));
        } else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("wlacz")) {
            this.dataStorage.chatEnabled = true;
            this.lang.broadcast("commands.chat.enabled", new Replacement("{ADMIN}", sender.getName()));
            this.lang.sendMessage(this.plugin.getServer().getConsoleSender(), "commands.chat.enabled", new Replacement("{ADMIN}", sender.getName()));
        } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("wylacz")) {
            this.dataStorage.chatEnabled = false;
            this.lang.broadcast("commands.chat.disabled", new Replacement("{ADMIN}", sender.getName()));
            this.lang.sendMessage(this.plugin.getServer().getConsoleSender(), "commands.chat.disabled", new Replacement("{ADMIN}", sender.getName()));
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] arguments = {"clear", "on", "off"};
            StringUtil.copyPartialMatches(args[0], Arrays.asList(arguments), completions);
        }

        return completions;
    }

}
