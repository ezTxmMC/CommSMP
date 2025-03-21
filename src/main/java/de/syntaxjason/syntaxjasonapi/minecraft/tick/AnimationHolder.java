package de.syntaxjason.syntaxjasonapi.minecraft.tick;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimationHolder extends AbstractTickTimer {

    // Wir verwenden hier einen Thread-sicheren Container
    private final List<TickedAnimation> animations = new CopyOnWriteArrayList<>();

    /**
     * Fügt eine tickbare Animation hinzu.
     */
    public void addAnimation(TickedAnimation animation) {
        animations.add(animation);
    }

    @Override
    protected void tick(long delta) {
        animations.removeIf(TickedAnimation::isFinished);
        for (TickedAnimation animation : animations) {
            animation.tick(delta);
        }
    }
}
