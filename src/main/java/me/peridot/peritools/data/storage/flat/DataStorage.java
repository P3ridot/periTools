package me.peridot.peritools.data.storage.flat;

import api.peridot.periapi.configuration.ConfigurationFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class DataStorage extends ConfigurationFile {

    public Location spawnLocation;

    public boolean chatEnabled;

    public DataStorage(Plugin plugin) {
        super(plugin, "data", "data");
    }

    @Override
    public void reloadConfiguration() {
        super.reloadConfiguration();
        YamlConfiguration configuration = getYamlConfiguration();

        if (configuration.getConfigurationSection("data.locations.spawn") != null) {
            this.spawnLocation = new Location(
                    Bukkit.getWorld(configuration.getString("data.locations.spawn.world")),
                    configuration.getDouble("data.locations.spawn.x"),
                    configuration.getDouble("data.locations.spawn.y"),
                    configuration.getDouble("data.locations.spawn.z"),
                    (float) configuration.getDouble("data.locations.spawn.yaw"),
                    (float) configuration.getDouble("data.locations.spawn.pitch")
            );
        }
    }

}
