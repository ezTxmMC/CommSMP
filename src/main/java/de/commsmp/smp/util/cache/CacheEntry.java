package de.commsmp.smp.util.cache;

public class CacheEntry<E> {

    private final E value;
    private int time = 0;

    public CacheEntry(final E value) {
        this.value = value;
    }

    public E getValue() {
        time = 0;
        return value;
    }

    public int getTime() {
        return time;
    }

    public void update() {
        time += 1;
    }

}
