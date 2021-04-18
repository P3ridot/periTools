package me.peridot.peritools.user;

import lombok.Getter;
import lombok.Setter;
import me.peridot.peritools.modifiable.Modifiable;
import me.peridot.peritools.user.data.Settings;
import me.peridot.peritools.user.data.TemporaryData;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User extends Modifiable {

    @Getter
    private final UUID uuid;
    @Getter
    private final String name;
    @Getter
    private final Settings settings;
    @Getter
    private final TemporaryData temporaryData;
    @Getter
    @Setter
    private String displayName;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.displayName = name;
        this.settings = new Settings(this);
        this.temporaryData = new TemporaryData(this);
    }

    public User(Player player) {
        this(player.getUniqueId(), player.getName());
    }


}
