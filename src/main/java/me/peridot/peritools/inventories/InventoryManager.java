package me.peridot.peritools.inventories;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.inventories.CustomInventory;
import api.peridot.periapi.inventories.PeriInventoryManager;
import lombok.Getter;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.inventories.storage.ChatInventory;

public class InventoryManager {

    private final PeriTools plugin;
    @Getter
    private final PeriInventoryManager manager;

    @Getter
    private final CustomInventory chatInventory;

    public InventoryManager(PeriTools plugin) {
        this.plugin = plugin;
        this.manager = plugin.getPeriAPI().getInventoryManager();

        ConfigurationFile config = plugin.getInventoriesConfiguration();

        this.chatInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(this.manager)
                .provider(new ChatInventory(plugin))
                .rows(config.getInt("chat.size"))
                .title(config.getColoredString("chat.title"))
                .updateDelay(-1)
                .build();
    }

}
