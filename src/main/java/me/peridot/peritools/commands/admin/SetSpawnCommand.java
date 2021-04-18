package me.peridot.peritools.commands.admin;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.commands.PeriPlayerCommand;
import me.peridot.peritools.data.storage.flat.DataStorage;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetSpawnCommand extends PeriPlayerCommand {

    private final LangAPI lang;
    private final DataStorage dataStorage;

    public SetSpawnCommand(PeriTools plugin) {
        super(plugin, "setspawn", "/{LABEL}", "Ustawia lokalizacje spawna", "peritools.cmd.setspawn", "ustawspawn");
        this.lang = plugin.getLang();
        this.dataStorage = plugin.getDataStorage();
    }

    @Override
    public boolean onExecute(Player player, String label, String[] args) {
        Location location = player.getLocation();

        YamlConfiguration configuration = this.dataStorage.getYamlConfiguration();

        configuration.set("data.locations.spawn.world", location.getWorld().getName());
        configuration.set("data.locations.spawn.x", location.getX());
        configuration.set("data.locations.spawn.y", location.getY());
        configuration.set("data.locations.spawn.z", location.getZ());
        configuration.set("data.locations.spawn.yaw", location.getYaw());
        configuration.set("data.locations.spawn.pitch", location.getPitch());

        try {
            configuration.save(this.dataStorage.getDirectory());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        location.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        this.dataStorage.reloadConfiguration();

        this.dataStorage.spawnLocation = location;

        this.lang.sendMessage(player, "commands.setspawn.setted", new Replacement("{X}", location.getBlockX()),
                new Replacement("{Y}", location.getBlockY()),
                new Replacement("{Z}", location.getBlockZ()),
                new Replacement("{WORLD}", location.getWorld().getName()));

        return true;
    }

}
