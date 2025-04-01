package de.commsmp.smp.util.cache;

import java.util.function.Consumer;

public interface Cache<K, V> {

    void put(K key, V value);

    boolean has(K key);

    V get(K key);

    V remove(K key);

    Consumer<V> getRemoveAction();

    void setRemoveAction(Consumer<V> removeAction);

    void clear();

    void tick();

}
