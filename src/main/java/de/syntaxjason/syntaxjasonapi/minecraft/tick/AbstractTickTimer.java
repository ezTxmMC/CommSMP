/**
 * Copied from Github
 *
 * @see https://github.com/Lauriichan/SpigotPluginBase
 * Slightly modified to use Virtual Threads
 */

package de.syntaxjason.syntaxjasonapi.minecraft.tick;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractTickTimer {

    private static final long SEC_IN_NANOS = TimeUnit.SECONDS.toNanos(1);
    private static final long MILLI_IN_NANOS = TimeUnit.MILLISECONDS.toNanos(1);

    private final AtomicInteger state = new AtomicInteger(0);
    private volatile Thread timerThread;
    private volatile long length = MILLI_IN_NANOS * 50L;
    private volatile long pauseLength = MILLI_IN_NANOS * 250L;
    private volatile String name = null;

    public final void setName(final String name) {
        this.name = name;
        updateName();
    }

    private final void updateName() {
        if (timerThread != null && name != null && !name.isBlank()) {
            timerThread.setName("TickTimer - " + name);
        }
    }

    public final void setPauseLength(final long pauseLength, final TimeUnit unit) {
        this.pauseLength = Math.max(unit.toNanos(pauseLength), MILLI_IN_NANOS * 10L);
    }

    public final void setLength(final long length, final TimeUnit unit) {
        this.length = Math.max(unit.toNanos(length), MILLI_IN_NANOS);
    }

    public final void start() {
        if (timerThread != null) {
            state.compareAndSet(2, 1);
            return;
        }
        state.compareAndSet(0, 1);
        timerThread = Thread.ofVirtual().unstarted(this::tickThread);
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public final void stop() {
        if (timerThread == null) return;
        state.set(0);
        timerThread.interrupt();
        timerThread = null;
    }

    private final void tickThread() {
        long nextLength = this.length;
        long prevNanoTime = System.nanoTime();
        long nanoTime = prevNanoTime;
        long delta;
        while (state.get() != 0) {
            try {
                if (state.get() == 2) {
                    Thread.sleep(this.pauseLength / 1_000_000, (int) (this.pauseLength % 1_000_000));
                    continue;
                }
                prevNanoTime = nanoTime;
                nanoTime = System.nanoTime();
                delta = nanoTime - prevNanoTime;
                tick(delta);
                long sleepTime = nextLength - (System.nanoTime() - nanoTime);
                if (sleepTime > 0) {
                    Thread.sleep(TimeUnit.NANOSECONDS.toMillis(sleepTime),
                            (int) (sleepTime % TimeUnit.MILLISECONDS.toNanos(1)));
                } else {
                    Thread.yield();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    protected abstract void tick(long delta);
}


