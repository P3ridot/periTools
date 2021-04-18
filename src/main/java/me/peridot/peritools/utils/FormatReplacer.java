package me.peridot.peritools.utils;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.replacements.ReplacementUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.hooks.HooksManager;
import me.peridot.peritools.user.User;
import me.peridot.peritools.user.UserCache;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class FormatReplacer {

    private final PeriTools plugin;

    private final HooksManager hooksManager;
    private final ConfigurationFile messagesConfiguration;
    private final ConfigurationFile chatConfiguration;
    private final UserCache userCache;

    public FormatReplacer(PeriTools plugin) {
        this.plugin = plugin;
        this.hooksManager = plugin.getHooksManager();
        this.messagesConfiguration = plugin.getConfigurationManager().getMessagesConfiguration();
        this.chatConfiguration = plugin.getChatConfiguration();
        this.userCache = plugin.getUserCache();
    }

    public String getFormat(CommandSender sender) {
        String format = "";

        Permission permission = this.hooksManager.getPermission();
        if (permission == null) {
            if (sender instanceof Player) {
                format = this.chatConfiguration.getColoredString("format.default");
            } else {
                format = this.chatConfiguration.getColoredString("format.console");
            }
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String group = "";
            if (permission != null) {
                group = permission.getPrimaryGroup(player).toLowerCase();
            }
            if (group != null && !group.isEmpty() && this.chatConfiguration.getYamlConfiguration().getConfigurationSection("chat.format").contains(group)) {
                format = this.chatConfiguration.getColoredString("format." + group);
            }
        } else {
            format = this.chatConfiguration.getColoredString("format.console");
        }

        return replaceFormat(sender, format);
    }

    public String replaceFormat(CommandSender sender, String message, String placeholder_prefix) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Permission permission = this.hooksManager.getPermission();
            String group = "";
            if (permission != null) {
                group = permission.getPrimaryGroup(player).toLowerCase();
            }

            message = StringUtils.replace(message, "{" + placeholder_prefix + "DISPLAYNAME}", player.getDisplayName());
            message = StringUtils.replace(message, "{" + placeholder_prefix + "GROUP}", group);

            Chat chat = this.plugin.getHooksManager().getChat();
            if (chat != null) {
                message = StringUtils.replace(message, "{" + placeholder_prefix + "PREFIX}", chat.getPlayerPrefix(player));
                message = StringUtils.replace(message, "{" + placeholder_prefix + "SUFFIX}", chat.getPlayerSuffix(player));
                if (permission != null) {
                    message = StringUtils.replace(message, "{" + placeholder_prefix + "GROUP-PREFIX}", chat.getGroupPrefix(player.getWorld(), group));
                    message = StringUtils.replace(message, "{" + placeholder_prefix + "GROUP-SUFFIX}", chat.getGroupSuffix(player.getWorld(), group));
                }
            }

            Location location = player.getLocation();
            User user = this.userCache.createUser(player);

            String gamemode;
            switch (player.getGameMode()) {
                case SURVIVAL:
                    gamemode = this.messagesConfiguration.getColoredString("status.gamemode.survival");
                    break;
                case CREATIVE:
                    gamemode = this.messagesConfiguration.getColoredString("status.gamemode.gamemode");
                    break;
                case ADVENTURE:
                    gamemode = this.messagesConfiguration.getColoredString("status.gamemode.adventure");
                    break;
                case SPECTATOR:
                    gamemode = this.messagesConfiguration.getColoredString("status.gamemode.spectator");
                    break;
                default:
                    gamemode = "";
            }

            message = ReplacementUtil.replace(message,
                    new Replacement("{" + placeholder_prefix + "HEALTH}", player.getHealth()),
                    new Replacement("{" + placeholder_prefix + "MAX-HEALTH}", player.getMaxHealth()),
                    new Replacement("{" + placeholder_prefix + "FOOD-LEVEL}", player.getFoodLevel()),
                    new Replacement("{" + placeholder_prefix + "SATURATION}", player.getSaturation()),
                    new Replacement("{" + placeholder_prefix + "GAMEMODE}", gamemode),
                    new Replacement("{" + placeholder_prefix + "FLYING}", player.isFlying() ? this.messagesConfiguration.getColoredString("status.flying.true") : this.messagesConfiguration.getColoredString("status.flying.false")),
                    new Replacement("{" + placeholder_prefix + "GOD}", user.getSettings().isGod() ? this.messagesConfiguration.getColoredString("status.god.true") : this.messagesConfiguration.getColoredString("status.god.false")),
                    new Replacement("{" + placeholder_prefix + "SOCIALSPY}", user.getSettings().isSocialSpy() ? this.messagesConfiguration.getColoredString("status.socialspy.true") : this.messagesConfiguration.getColoredString("status.socialspy.false")),
                    new Replacement("{" + placeholder_prefix + "COMMANDSSPY}", user.getSettings().isCommandsSpy() ? this.messagesConfiguration.getColoredString("status.commandsspy.true") : this.messagesConfiguration.getColoredString("status.commandsspy.false")),
                    new Replacement("{" + placeholder_prefix + "LOC-WORLD}", location.getWorld().getName()),
                    new Replacement("{" + placeholder_prefix + "LOC-X}", location.getX()),
                    new Replacement("{" + placeholder_prefix + "LOC-Y}", location.getY()),
                    new Replacement("{" + placeholder_prefix + "LOC-Z}", location.getZ()),
                    new Replacement("{" + placeholder_prefix + "LOC-BLOCK-X}", location.getBlockX()),
                    new Replacement("{" + placeholder_prefix + "LOC-BLOCK-Y}", location.getBlockY()),
                    new Replacement("{" + placeholder_prefix + "LOC-BLOCK-Z}", location.getBlockZ())
            );

            if (this.plugin.getHooksManager().isPlaceholderapiHooked()) {
                Pattern placeholderPattern;
                if (placeholder_prefix != null && !placeholder_prefix.isEmpty()) {
                    placeholderPattern = Pattern.compile("%" + placeholder_prefix.toLowerCase() + "([^%]+)%");
                } else {
                    placeholderPattern = Pattern.compile("[%]([^%]+)[%]");
                }
                message = PlaceholderAPI.setPlaceholders(player, message, placeholderPattern);
            }
        }

        message = StringUtils.replace(message, "{" + placeholder_prefix + "NAME}", sender.getName());

        return message;
    }

    public String replaceFormat(CommandSender sender, String message) {
        return replaceFormat(sender, message, "");
    }

}
