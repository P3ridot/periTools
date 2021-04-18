package me.peridot.peritools.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.peridot.peritools.PeriTools;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UserCache {

    private final PeriTools plugin;

    private final Map<UUID, User> userMap = new HashMap<>();
    private final Cache<UUID, User> userCache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();

    public UserCache(PeriTools plugin) {
        this.plugin = plugin;
    }

    public void removeUser(UUID uuid) {
        User user = this.userMap.get(uuid);
        if (user != null) {
            this.userCache.put(uuid, user);
        }
        this.userMap.remove(uuid);
    }

    public void removeUser(Player player) {
        removeUser(player.getUniqueId());
    }

    public User createUser(Player player) {
        UUID uuid = player.getUniqueId();
        User user = this.userMap.get(uuid);
        if (user == null) {
            User cachedUser = this.userCache.getIfPresent(uuid);
            if (cachedUser != null) {
                this.userMap.put(uuid, user = cachedUser);
                return user;
            }
            this.userMap.put(uuid, user = new User(player));
        }
        return user;
    }

    public User createUser(UUID uuid) {
        return createUser(this.plugin.getServer().getPlayer(uuid));
    }

    public Set<User> getModifiedUsers() {
        return this.userMap.values().stream()
                .filter(user -> user.isModified() || user.getSettings().isModified())
                .collect(Collectors.toSet());
    }

}
