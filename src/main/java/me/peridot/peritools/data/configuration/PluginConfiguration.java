package me.peridot.peritools.data.configuration;

import api.peridot.periapi.configuration.ConfigurationProvider;
import me.peridot.peritools.PeriTools;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class PluginConfiguration extends ConfigurationProvider {

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private final PeriTools plugin;

    public PluginConfiguration(PeriTools plugin) {
        super(plugin, plugin.getConfig().getConfigurationSection("config"));
        this.plugin = plugin;
    }

    public void reloadConfiguration() {
        FileConfiguration configuration = this.plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("config");

        dateFormat = DateTimeFormatter.ofPattern(configurationSection.getString("format.date"));
        decimalFormat = new DecimalFormat(configurationSection.getString("format.decimal"));
    }

}
