package me.peridot.peritools;

import api.peridot.periapi.PeriAPI;
import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import lombok.Getter;
import me.peridot.peritools.commands.CommandsRegistry;
import me.peridot.peritools.commands.admin.*;
import me.peridot.peritools.commands.basic.*;
import me.peridot.peritools.commands.basic.items.*;
import me.peridot.peritools.data.configuration.ConfigurationManager;
import me.peridot.peritools.data.configuration.PluginConfiguration;
import me.peridot.peritools.data.storage.DataManager;
import me.peridot.peritools.data.storage.flat.DataStorage;
import me.peridot.peritools.hooks.HooksManager;
import me.peridot.peritools.inventories.InventoryManager;
import me.peridot.peritools.listeners.*;
import me.peridot.peritools.user.UserCache;
import me.peridot.peritools.utils.EventWaiter;
import me.peridot.peritools.utils.FormatReplacer;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PeriTools extends JavaPlugin {

    private static PeriTools INSTANCE;
    @Getter
    private PeriAPI periAPI;
    @Getter
    private HooksManager hooksManager;
    @Getter
    private ConfigurationManager configurationManager;
    @Getter
    private DataManager dataManager;
    @Getter
    private UserCache userCache;
    @Getter
    private FormatReplacer formatReplacer;
    @Getter
    private EventWaiter eventWaiter;
    @Getter
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        this.periAPI = new PeriAPI(this);
        this.periAPI.init();

        this.hooksManager = new HooksManager(this);
        this.hooksManager.hook();

        this.configurationManager = new ConfigurationManager(this);
        this.configurationManager.reloadConfigurations();

        this.dataManager = new DataManager(this);
        this.dataManager.reloadData();

        this.userCache = new UserCache(this);

        this.formatReplacer = new FormatReplacer(this);

        this.eventWaiter = new EventWaiter(this);
        this.eventWaiter.addEvents(PlayerMoveEvent.class);

        initInventoryManager();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(this), this);
        pluginManager.registerEvents(new PlayerCommandPreprocessListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerRespawnListener(this), this);

        CommandsRegistry commandsRegistry = new CommandsRegistry(this);
        /* Basic */
        commandsRegistry.register(new HatCommand(this));
        commandsRegistry.register(new MessageCommand(this));
        commandsRegistry.register(new NickCommand(this));
        commandsRegistry.register(new ReplyCommand(this));
        commandsRegistry.register(new SpawnCommand(this));
        commandsRegistry.register(new TpacceptCommand(this));
        commandsRegistry.register(new TpaCommand(this));
        commandsRegistry.register(new TpdenyCommand(this));
        commandsRegistry.register(new TpahereCommand(this));
        commandsRegistry.register(new TrashCommand(this));
        commandsRegistry.register(new WorkbenchCommand(this));
        /* Items */
        commandsRegistry.register(new AddLoreCommand(this));
        commandsRegistry.register(new ChangeLoreCommand(this));
        commandsRegistry.register(new ReloreCommand(this));
        commandsRegistry.register(new RemoveLoreCommand(this));
        commandsRegistry.register(new RenameCommand(this));
        commandsRegistry.register(new ResetLoreCommand(this));
        commandsRegistry.register(new ResetNameCommand(this));
        /* Admin */
        commandsRegistry.register(new BroadcastCommand(this));
        commandsRegistry.register(new ChatCommand(this));
        commandsRegistry.register(new CommandsSpyCommand(this));
        commandsRegistry.register(new FeedCommand(this));
        commandsRegistry.register(new GamemodeCommand(this));
        commandsRegistry.register(new GodCommand(this));
        commandsRegistry.register(new HealCommand(this));
        commandsRegistry.register(new SetSpawnCommand(this));
        commandsRegistry.register(new SocialSpyCommand(this));
        commandsRegistry.register(new StaffCommand(this));
        commandsRegistry.register(new SudoCommand(this));

        INSTANCE = this;
    }

    public LangAPI getLang() {
        return this.configurationManager.getLang();
    }

    public PluginConfiguration getPluginConfiguration() {
        return this.configurationManager.getPluginConfiguration();
    }

    public ConfigurationFile getChatConfiguration() {
        return this.configurationManager.getChatConfiguration();
    }

    public ConfigurationFile getInventoriesConfiguration() {
        return this.configurationManager.getInventoriesConfiguration();
    }

    public DataStorage getDataStorage() {
        return this.dataManager.getDataStorage();
    }

    public void initInventoryManager() {
        this.inventoryManager = new InventoryManager(this);
    }

    /*
        Only use for plugins hooks
     */
    public static PeriTools getInstance() {
        return INSTANCE;
    }

}
