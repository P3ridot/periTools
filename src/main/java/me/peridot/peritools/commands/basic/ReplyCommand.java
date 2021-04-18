package me.peridot.peritools.commands.basic;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.simple.ColorUtil;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import me.peridot.peritools.utils.FormatReplacer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand extends PeriPlayerCommand {

    private final LangAPI lang;
    private final UserCache userCache;
    private final FormatReplacer formatReplacer;

    public ReplyCommand(PeriTools plugin) {
        super(plugin, "reply", "/{LABEL} [wiadomosc]", "Odpowiada na ostatnia otrzymana prywatna wiadomosc", "peritools.cmd.reply", "r");
        this.lang = plugin.getLang();
        this.userCache = plugin.getUserCache();
        this.formatReplacer = plugin.getFormatReplacer();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        User senderUser = this.userCache.createUser(player);
        UUID lastPrivateMessagePlayer = senderUser.getTemporaryData().getLastPrivateMessagePlayer();
        if (lastPrivateMessagePlayer == null || getPlugin().getServer().getPlayer(lastPrivateMessagePlayer) == null) {
            this.lang.sendMessage(player, "errors.commands.message.noreceiver");
            senderUser.getTemporaryData().setLastPrivateMessagePlayer(null);
            return true;
        }
        Player receiver = getPlugin().getServer().getPlayer(lastPrivateMessagePlayer);

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            message.append(" ").append(args[i]);
        }
        String messageString = message.toString().replaceFirst(" ", "");

        if (messageString.isEmpty() || messageString.equalsIgnoreCase(" ")) {
            this.lang.sendMessage(player, "errors.commands.message.nomessage");
            return true;
        }

        if (player.hasPermission(getSubPermission("color"))) {
            messageString = ColorUtil.color(messageString);
        }

        this.lang.sendMessage(player, "commands.message.sender", new Replacement("{SENDER-FORMAT}", this.formatReplacer.getFormat(player)),
                new Replacement("{RECEIVER-FORMAT}", this.formatReplacer.getFormat(receiver)),
                new Replacement("{MESSAGE}", messageString));

        this.lang.sendMessage(receiver, "commands.message.receiver", new Replacement("{SENDER-FORMAT}", this.formatReplacer.getFormat(player)),
                new Replacement("{RECEIVER-FORMAT}", this.formatReplacer.getFormat(receiver)),
                new Replacement("{MESSAGE}", messageString));

        String finalMessageString = messageString;
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            for (Player loopPlayer : this.plugin.getServer().getOnlinePlayers()) {
                if (loopPlayer.equals(player)) continue;
                if (!loopPlayer.hasPermission("peritools.cmd.socialspy.see")) continue;
                User user = this.userCache.createUser(loopPlayer);
                if (!user.getSettings().isSocialSpy()) continue;

                this.lang.sendMessage(loopPlayer, "commands.socialspy.log", new Replacement("{FROM}", player.getName()),
                        new Replacement("{TO}", receiver.getName()),
                        new Replacement("{MESSAGE}", finalMessageString));
            }
        });

        this.userCache.createUser(player.getUniqueId()).getTemporaryData().setLastPrivateMessagePlayer(receiver.getUniqueId());
        this.userCache.createUser(receiver.getUniqueId()).getTemporaryData().setLastPrivateMessagePlayer(player.getUniqueId());

        return true;
    }

}
