package net.matixmedia.macroscriptingmod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventChatMessage;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class MixinScreen {

    @Inject(method = "handleTextClick", at = @At("HEAD"), cancellable = true)
    public void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cb) {
        if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.RUN_COMMAND) {
            String command = SharedConstants.stripInvalidChars(style.getClickEvent().getValue());
            if (!command.startsWith(".")) return;
            EventChatMessage.Send event = new EventChatMessage.Send(command);
            EventManager.fire(event);
            if (!event.isCanceled()) return;
            cb.cancel();
            cb.setReturnValue(true);
        }
    }
}
