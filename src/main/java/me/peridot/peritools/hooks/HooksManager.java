package me.peridot.peritools.hooks;

import lombok.Getter;
import me.peridot.peritools.PeriTools;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

public class HooksManager {

    private final PeriTools plugin;

    @Getter
    private boolean vaultHooked = false;
    @Getter
    private boolean placeholderapiHooked = false;

    @Getter
    private Permission permission;
    @Getter
    private Chat chat;

    public HooksManager(PeriTools plugin) {
        this.plugin = plugin;
    }

    public void hook() {
        Logger logger = this.plugin.getLogger();
        PluginManager pluginManager = this.plugin.getServer().getPluginManager();

        if (pluginManager.getPlugin("Vault") != null) {
            logger.info("Vault has been hooked!");
            if (!setupVaultPermission()) {
                logger.info("Permission plugin (Vault) is missing! Will be ignored");
                this.vaultHooked = true;
            }
            if (!setupVaultChat()) {
                logger.info("Chat plugin (Vault) is missing! Will be ignored");
                this.vaultHooked = true;
            }
        }

        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            logger.info("PlaceholderAPI has been hooked!");
            this.placeholderapiHooked = true;
        }
    }

    private boolean setupVaultPermission() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        this.permission = rsp.getProvider();
        return this.permission != null;
    }

    private boolean setupVaultChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        this.chat = rsp.getProvider();
        return this.permission != null;
    }

}
