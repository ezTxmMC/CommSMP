/**
 * Copied from Github
 *
 * @see https://github.com/Lauriichan/SpigotPluginBase
 */

package de.syntaxjason.syntaxjasonapi.minecraft.tick;

import java.util.concurrent.TimeUnit;

public final class TickCounter {
    private final long goal;
    private volatile long count;

    public TickCounter(final long amount, final TimeUnit unit) {
        this.goal = unit.toNanos(amount);
    }

    public boolean tick(final long delta) {
        count += delta;
        if (count >= goal) {
            count -= goal;
            return true;
        }
        return false;
    }

    public void reset() {
        count = 0;
    }

    public long goal() {
        return goal;
    }

    public long count() {
        return count;
    }
}


