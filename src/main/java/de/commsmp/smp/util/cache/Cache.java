package de.commsmp.smp.util.cache;

import java.util.function.Consumer;

public interface Cache<K, V> {

    void put(K key, V value);

    boolean has(K key);

    V get(K key);

    V remove(K key);

    void setRemoveAction(Consumer<V> removeAction);

    Consumer<V> getRemoveAction();

    void clear();

    void tick();

}
