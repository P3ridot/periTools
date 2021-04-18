package me.peridot.peritools.listeners;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.replacements.ReplacementUtil;
import api.peridot.periapi.utils.simple.ColorUtil;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.data.storage.flat.DataStorage;
import me.peridot.peritools.hooks.HooksManager;
import me.peridot.peritools.utils.FormatReplacer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    private final PeriTools plugin;

    private final HooksManager hooksManager;
    private final ConfigurationFile chatConfiguration;
    private final DataStorage dataStorage;
    private final FormatReplacer formatReplacer;

    public AsyncPlayerChatListener(PeriTools plugin) {
        this.plugin = plugin;
        this.hooksManager = plugin.getHooksManager();
        this.chatConfiguration = plugin.getChatConfiguration();
        this.dataStorage = plugin.getDataStorage();
        this.formatReplacer = plugin.getFormatReplacer();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!this.dataStorage.chatEnabled && !player.hasPermission("peritools.chat.bypass")) {
            event.setCancelled(true);
            return;
        }

        if (player.hasPermission("peritools.chat.color")) {
            event.setMessage(ColorUtil.color(event.getMessage()));
        }

        String format = getChatFormat(player);

        format = ReplacementUtil.replace(format,
                new Replacement("{PLAYER}", "%1$s"),
                new Replacement("{MESSAGE}", "%2$s")
        );

        format = this.formatReplacer.replaceFormat(player, format);

        event.setFormat(format);
    }

    private String getChatFormat(Player player) {
        String format = this.chatConfiguration.getColoredString("chat-format.default");

        Permission permission = this.hooksManager.getPermission();
        if (permission == null) {
            return format;
        }

        String group = permission.getPrimaryGroup(player).toLowerCase();
        if (!group.isEmpty() && this.chatConfiguration.getYamlConfiguration().getConfigurationSection("chat.chat-format").contains(group)) {
            format = this.chatConfiguration.getColoredString("chat-format." + group);
        }

        return format;
    }

}
