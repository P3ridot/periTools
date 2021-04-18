package me.peridot.peritools.inventories.storage;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peritools.PeriTools;
import me.peridot.peritools.data.storage.flat.DataStorage;
import org.bukkit.entity.Player;

public class ChatInventory implements InventoryProvider {

    private final PeriTools plugin;

    private final LangAPI lang;
    private final ConfigurationFile inventoriesConfiguration;
    private final DataStorage dataStorage;

    public ChatInventory(PeriTools plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLang();
        this.inventoriesConfiguration = plugin.getInventoriesConfiguration();
        this.dataStorage = plugin.getDataStorage();
    }

    @Override
    public void init(Player player, InventoryContent content) {
        content.clear();
        content.fill(InventoryItem.builder()
                .item(this.inventoriesConfiguration.getItemBuilder("chat.buttons.background").clone())
                .build());
        content.setItem(this.inventoriesConfiguration.getInt("chat.buttons.clear.slot"), InventoryItem.builder()
                .item(this.inventoriesConfiguration.getItemBuilder("chat.buttons.clear").clone())
                .consumer(event -> {
                    for (Player loopPlayer : this.plugin.getServer().getOnlinePlayers()) {
                        for (int i = 0; i < 101; i++) {
                            loopPlayer.sendMessage(" ");
                        }
                    }
                    this.lang.broadcast("commands.chat.cleared", new Replacement("{ADMIN}", player.getName()));
                    this.lang.sendMessage(this.plugin.getServer().getConsoleSender(), "commands.chat.cleared", new Replacement("{ADMIN}", player.getName()));
                })
                .build());
        content.setItem(this.inventoriesConfiguration.getInt("chat.buttons.enable.slot"), InventoryItem.builder()
                .item(this.inventoriesConfiguration.getItemBuilder("chat.buttons.enable").clone())
                .consumer(event -> {
                    this.dataStorage.chatEnabled = true;
                    this.lang.broadcast("commands.chat.enabled", new Replacement("{ADMIN}", player.getName()));
                    this.lang.sendMessage(this.plugin.getServer().getConsoleSender(), "commands.chat.enabled", new Replacement("{ADMIN}", player.getName()));
                })
                .build());
        content.setItem(this.inventoriesConfiguration.getInt("chat.buttons.disable.slot"), InventoryItem.builder()
                .item(this.inventoriesConfiguration.getItemBuilder("chat.buttons.disable").clone())
                .consumer(event -> {
                    this.dataStorage.chatEnabled = false;
                    this.lang.broadcast("commands.chat.disabled", new Replacement("{ADMIN}", player.getName()));
                    this.lang.sendMessage(this.plugin.getServer().getConsoleSender(), "commands.chat.disabled", new Replacement("{ADMIN}", player.getName()));
                })
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }

}
