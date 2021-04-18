package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.simple.ColorUtil;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriCommand;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import me.peridot.peritools.utils.FormatReplacer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand extends PeriCommand {

    private final LangAPI lang;
    private final UserCache userCache;
    private final FormatReplacer formatReplacer;

    public MessageCommand(PeriTools plugin) {
        super(plugin, "message", "/{LABEL} [gracz] [wiadomosc]", "Wysyla prywatna wiadomosc do gracza", "peritools.cmd.message", "msg", "m", "tell", "t", "whisper");
        this.lang = plugin.getLang();
        this.userCache = plugin.getUserCache();
        this.formatReplacer = plugin.getFormatReplacer();
    }

    @Override
    public boolean onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            return false;
        }

        Player receiver = getPlugin().getServer().getPlayer(args[0]);
        if (receiver == null) {
            this.lang.sendMessage(sender, "errors.simple.player-offline", new Replacement("{PLAYER}", args[0]));
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(" ").append(args[i]);
        }
        String messageString = message.toString().replaceFirst(" ", "");

        if (messageString.isEmpty() || messageString.equalsIgnoreCase(" ")) {
            this.lang.sendMessage(sender, "errors.commands.message.nomessage");
            return true;
        }

        if (sender.hasPermission(getSubPermission("color"))) {
            messageString = ColorUtil.color(messageString);
        }

        this.lang.sendMessage(sender, "commands.message.sender", new Replacement("{SENDER-FORMAT}", this.formatReplacer.getFormat(sender)),
                new Replacement("{RECEIVER-FORMAT}", this.formatReplacer.getFormat(receiver)),
                new Replacement("{MESSAGE}", messageString));

        this.lang.sendMessage(receiver, "commands.message.receiver", new Replacement("{SENDER-FORMAT}", this.formatReplacer.getFormat(sender)),
                new Replacement("{RECEIVER-FORMAT}", this.formatReplacer.getFormat(receiver)),
                new Replacement("{MESSAGE}", messageString));

        String finalMessageString = messageString;
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            for (Player loopPlayer : this.plugin.getServer().getOnlinePlayers()) {
                if (loopPlayer.equals(sender)) continue;
                if (!loopPlayer.hasPermission("peritools.cmd.socialspy.see")) continue;
                User user = this.userCache.createUser(loopPlayer);
                if (!user.getSettings().isSocialSpy()) continue;

                this.lang.sendMessage(loopPlayer, "commands.socialspy.log", new Replacement("{FROM}", sender.getName()),
                        new Replacement("{TO}", receiver.getName()),
                        new Replacement("{MESSAGE}", finalMessageString));
            }
        });

        if (sender instanceof Player) {
            Player player = (Player) sender;
            this.userCache.createUser(player.getUniqueId()).getTemporaryData().setLastPrivateMessagePlayer(receiver.getUniqueId());
            this.userCache.createUser(receiver.getUniqueId()).getTemporaryData().setLastPrivateMessagePlayer(player.getUniqueId());
        }
        return true;
    }

}
