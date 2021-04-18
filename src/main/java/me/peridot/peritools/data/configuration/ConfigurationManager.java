package me.peridot.peritools.data.configuration;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import me.peridot.peritools.PeriTools;

public class ConfigurationManager {

    private final PeriTools plugin;

    private PluginConfiguration pluginConfiguration;

    private ConfigurationFile chatConfiguration;
    private ConfigurationFile inventoriesConfiguration;

    private ConfigurationFile messagesConfiguration;
    private LangAPI lang;

    public ConfigurationManager(PeriTools plugin) {
        this.plugin = plugin;
    }

    public void reloadConfigurations() {
        this.plugin.saveDefaultConfig();
        this.pluginConfiguration = new PluginConfiguration(this.plugin);
        this.pluginConfiguration.reloadConfiguration();

        if (this.chatConfiguration == null) {
            this.chatConfiguration = new ConfigurationFile(this.plugin, "chat", "chat");
        }
        this.chatConfiguration.reloadConfiguration();

        if (this.inventoriesConfiguration == null) {
            this.inventoriesConfiguration = new ConfigurationFile(this.plugin, "inventories", "inventories");
        }
        this.inventoriesConfiguration.reloadConfiguration();

        if (this.messagesConfiguration == null) {
            this.messagesConfiguration = new ConfigurationFile(this.plugin, "messages", "messages");
        }
        this.messagesConfiguration.reloadConfiguration();
        if (this.lang == null) {
            this.lang = new LangAPI(this.messagesConfiguration.getYamlConfiguration().getConfigurationSection("messages"));
        } else {
            this.lang.setSection(this.messagesConfiguration.getYamlConfiguration().getConfigurationSection("messages"));
        }
        this.lang.reload();
    }

    public PluginConfiguration getPluginConfiguration() {
        if (this.pluginConfiguration == null) {
            reloadConfigurations();
        }
        return this.pluginConfiguration;
    }

    public ConfigurationFile getChatConfiguration() {
        if (this.chatConfiguration == null) {
            reloadConfigurations();
        }
        return this.chatConfiguration;
    }

    public ConfigurationFile getInventoriesConfiguration() {
        if (this.inventoriesConfiguration == null) {
            reloadConfigurations();
        }
        return this.inventoriesConfiguration;
    }

    public ConfigurationFile getMessagesConfiguration() {
        if (this.messagesConfiguration == null) {
            reloadConfigurations();
        }
        return this.messagesConfiguration;
    }

    public LangAPI getLang() {
        if (this.lang == null) {
            reloadConfigurations();
        }
        return this.lang;
    }

}
