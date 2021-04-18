package me.peridot.peritools.commands;

import api.peridot.periapi.packets.Reflection;
import me.peridot.peritools.PeriTools;
import org.bukkit.command.SimpleCommandMap;

public class CommandsRegistry {

    private final SimpleCommandMap commandMap;

    public CommandsRegistry(PeriTools plugin) {
        Reflection.FieldAccessor<?> bukkitCommandMapField = Reflection.getField(plugin.getServer().getClass(), "commandMap", SimpleCommandMap.class);
        this.commandMap = (SimpleCommandMap) bukkitCommandMapField.get(plugin.getServer());
    }

    public void register(PeriCommand command) {
        this.commandMap.register(command.getName(), command);
    }

}
