package me.peridot.peritools.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventWaiter {

    private final JavaPlugin plugin;
    private final Map<SimpleImmutableEntry<Class<?>, EventPriority>, Set<WaitingEvent>> waitingEvents;

    public EventWaiter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.waitingEvents = new ConcurrentHashMap<>();
    }

    @SafeVarargs
    public final void addEvents(Class<? extends Event>... eventTypes) {
        if (eventTypes == null || eventTypes.length == 0) {
            return;
        }
        for (Class<? extends Event> eventType : eventTypes) {
            if (eventType == null) {
                continue;
            }
            for (EventPriority priority : EventPriority.values()) {
                this.plugin.getServer().getPluginManager().registerEvent(eventType, new Listener() {
                        },
                        priority, (listener, event) -> checkEvent(event, priority), this.plugin);
            }
        }
    }

    public <T extends Event> void waitForEvent(Class<T> classType, EventPriority priority,
                                               Predicate<T> condition, Consumer<T> action) {
        waitForEvent(classType, priority, condition, action, -1, null);
    }

    public <T extends Event> void waitForEvent(Class<T> classType, EventPriority priority,
                                               Predicate<T> condition, Consumer<T> action,
                                               long timeout, Runnable timeoutAction) {
        Validate.notNull(classType, "The provided class type is null");
        Validate.notNull(condition, "The provided condition predicate is null");
        Validate.notNull(action, "The provided action consumer is null");

        WaitingEvent we = new WaitingEvent<>(condition, action);
        Set<WaitingEvent> set = this.waitingEvents
                .computeIfAbsent(new SimpleImmutableEntry<>(classType, priority), c -> new HashSet<>());
        set.add(we);

        if (timeout > 0) {
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                if (set.remove(we) && timeoutAction != null) {
                    timeoutAction.run();
                }
            }, timeout);
        }
    }

    private void checkEvent(Event event, EventPriority priority) {
        Class clazz = event.getClass();

        SimpleImmutableEntry<Class, EventPriority> c = new SimpleImmutableEntry<>(clazz, priority);

        if (this.waitingEvents.containsKey(c)) {
            Set<WaitingEvent> set = this.waitingEvents.get(c);
            WaitingEvent[] toRemove = set.toArray(new WaitingEvent[0]);
            set.removeAll(Stream.of(toRemove).filter(i -> i.attempt(event)).collect(Collectors.toSet()));
        }
    }

    private class WaitingEvent<T extends Event> {

        final Predicate<T> condition;
        final Consumer<T> action;

        WaitingEvent(Predicate<T> condition, Consumer<T> action) {
            this.condition = condition;
            this.action = action;
        }

        boolean attempt(T event) {
            if (this.condition.test(event)) {
                this.action.accept(event);
                return true;
            }
            return false;
        }

    }

}