package de.commsmp.smp.util.cache;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Ein spezialisierter Cache, bei dem der Schlüssel immer eine {@link UUID} ist.
 * Der Wert ist generisch und es wird eine Tick-Methode bereitgestellt, um abgelaufene
 * Einträge basierend auf einer vorgegebenen Cache-Zeit automatisch zu entfernen.
 *
 * @param <V> Typ des Wertes, der im Cache gespeichert wird.
 */
public final class UUIDCache<V> implements Cache<UUID, V> {

    private final HashMap<UUID, CacheEntry<V>> entries = new HashMap<>();
    private final int cacheTime;
    private Consumer<V> removeAction;

    /**
     * Erzeugt einen neuen UUIDCache mit der angegebenen Cache-Zeit.
     *
     * @param cacheTime die Anzahl der Ticks, nach denen ein Eintrag als abgelaufen gilt.
     */
    public UUIDCache(final int cacheTime) {
        this.cacheTime = cacheTime;
    }

    @Override
    public Consumer<V> getRemoveAction() {
        return removeAction;
    }

    @Override
    public void setRemoveAction(final Consumer<V> removeAction) {
        this.removeAction = removeAction;
    }

    @Override
    public void put(final UUID key, final V value) {
        if (key == null || value == null) {
            return;
        }
        entries.put(key, new CacheEntry<>(value));
    }

    @Override
    public boolean has(final UUID key) {
        return key != null && entries.containsKey(key);
    }

    @Override
    public V get(final UUID key) {
        final CacheEntry<V> entry = entries.get(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V remove(final UUID key) {
        final CacheEntry<V> entry = entries.remove(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public void clear() {
        entries.clear();
    }

    /**
     * Erhöht den internen Zähler jedes Eintrags und entfernt abgelaufene Einträge,
     * deren Zeit den festgelegten cacheTime-Wert erreicht oder überschritten hat.
     * Wird auch die optionale removeAction asynchron ausgeführt.
     */
    @Override
    public void tick() {
        UUID[] keys = entries.keySet().toArray(new UUID[0]);
        for (final UUID key : keys) {
            final CacheEntry<V> entry = entries.get(key);
            if (entry == null) {
                continue;
            }
            entry.update();
            if (entry.getTime() >= cacheTime) {
                entries.remove(key, entry);
                if (removeAction != null) {
                    CompletableFuture.runAsync(() -> removeAction.accept(entry.getValue()));
                }
            }
        }
    }
}

