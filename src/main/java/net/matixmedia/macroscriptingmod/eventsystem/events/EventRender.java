package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.Event;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class EventRender extends Event {
    private final float tickDelta;
    private final long startTime;
    private final boolean tick;

    public EventRender(float tickDelta, long startTime, boolean tick) {
        this.tickDelta = tickDelta;
        this.startTime = startTime;
        this.tick = tick;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isTick() {
        return tick;
    }
}
