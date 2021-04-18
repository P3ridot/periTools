package me.peridot.peritools.user.data;

import api.peridot.periapi.utils.Pair;
import lombok.Getter;
import lombok.Setter;
import me.peridot.peritools.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TemporaryData {

    @Getter
    private final User user;

    private final Map<UUID, TeleportationType> asksForTeleportation = new LinkedHashMap<>();

    @Getter
    @Setter
    private UUID lastPrivateMessagePlayer;
    @Getter
    @Setter
    private BukkitTask teleportationTimer;

    public TemporaryData(User user) {
        this.user = user;
    }

    public void safeSetTeleportationTimer(BukkitTask teleportationTimer) {
        if (this.teleportationTimer != null) {
            this.teleportationTimer.cancel();
        }
        this.teleportationTimer = teleportationTimer;
    }

    public Map<UUID, TeleportationType> getAsksForTeleportation() {
        return new HashMap<>(this.asksForTeleportation);
    }

    public Pair<Player, TeleportationType> getFirstPendingAskForTeleportation() {
        LinkedList<Map.Entry<UUID, TeleportationType>> list = new LinkedList<>(this.asksForTeleportation.entrySet());
        Iterator<Map.Entry<UUID, TeleportationType>> iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, TeleportationType> value = iterator.next();
            UUID playerUuid = value.getKey();
            this.asksForTeleportation.remove(value);
            Player player = Bukkit.getPlayer(playerUuid);
            if (player != null) {
                return Pair.of(player, value.getValue());
            }
        }
        return null;
    }

    public void askForTeleportation(UUID uuid, TeleportationType teleportationType) {
        removeAskForTeleportation(uuid);
        this.asksForTeleportation.put(uuid, teleportationType);
    }

    public void removeAskForTeleportation(UUID uuid) {
        this.asksForTeleportation.remove(uuid);
    }

    public void askForTeleportation(Player player, TeleportationType teleportationType) {
        askForTeleportation(player.getUniqueId(), teleportationType);
    }

    public void removeAskForTeleportation(Player player) {
        removeAskForTeleportation(player.getUniqueId());
    }

    public void clearTemporaryData() {
        this.asksForTeleportation.clear();

        setLastPrivateMessagePlayer(null);
        safeSetTeleportationTimer(null);
    }

    public enum TeleportationType {
        NORMAL,
        HERE
    }

}
