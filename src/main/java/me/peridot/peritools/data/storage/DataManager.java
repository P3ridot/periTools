package me.peridot.peritools.data.storage;

import me.peridot.peritools.PeriTools;
import me.peridot.peritools.data.storage.flat.DataStorage;

public class DataManager {

    private final PeriTools plugin;

    private DataStorage dataStorage;

    public DataManager(PeriTools plugin) {
        this.plugin = plugin;
    }

    public void reloadData() {
        this.dataStorage = new DataStorage(this.plugin);
        this.dataStorage.reloadConfiguration();
    }

    public DataStorage getDataStorage() {
        if (this.dataStorage == null) {
            reloadData();
        }
        return this.dataStorage;
    }

}
