package de.syntaxjason.syntaxjasonapi.minecraft.tick;

public interface TickedAnimation {

    /**
     * Wird in jedem Tick aufgerufen, um den Animationszustand zu aktualisieren.
     * @param delta Zeitdifferenz in Nanosekunden seit dem letzten Tick
     */
    void tick(long delta);

    /**
     * Gibt zurück, ob die Animation beendet ist.
     */
    boolean isFinished();
}
