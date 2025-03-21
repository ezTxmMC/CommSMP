package de.syntaxjason.syntaxjasonapi.minecraft.tick;

import java.util.ArrayList;
import java.util.List;

public class CompositeAnimation implements TickedAnimation {

    private final List<TickedAnimation> animations = new ArrayList<>();

    public void addAnimation(TickedAnimation animation) {
        animations.add(animation);
    }

    @Override
    public void tick(long delta) {
        animations.removeIf(TickedAnimation::isFinished);
        for (TickedAnimation anim : animations) {
            anim.tick(delta);
        }
    }

    @Override
    public boolean isFinished() {
        return animations.isEmpty();
    }
}
