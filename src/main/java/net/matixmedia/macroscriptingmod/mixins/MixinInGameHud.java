package net.matixmedia.macroscriptingmod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventTitle;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Shadow @Nullable private Text title;

    @Shadow private int titleRemainTicks;

    @Shadow private int titleFadeInTicks;

    @Shadow private int titleStayTicks;

    @Shadow private int titleFadeOutTicks;

    @Shadow @Nullable private Text subtitle;

    @Shadow public abstract void setCanShowChatDisabledScreen(boolean canShowChatDisabledScreen);

    @Shadow @Nullable private Text overlayMessage;

    @Shadow private int overlayRemaining;

    @Shadow private boolean overlayTinted;

    @Inject(method = "setTitle", at = @At("HEAD"), cancellable = true)
    public void setTitle(Text title, CallbackInfo cb) {
        EventTitle event = new EventTitle(title.getString(), EventTitle.TitleType.TITLE);
        EventManager.fire(event);
        if (event.isCancelled()) {
            cb.cancel();
            return;
        }
        if (event.isModified()) {
            cb.cancel();
            this.title = Text.literal(event.getContent());
            this.titleRemainTicks = this.titleFadeInTicks + this.titleStayTicks + this.titleFadeOutTicks;
        }
    }

    @Inject(method = "setSubtitle", at = @At("HEAD"), cancellable = true)
    public void setSubtitle(Text subtitle, CallbackInfo cb) {
        EventTitle event = new EventTitle(subtitle.getString(), EventTitle.TitleType.SUBTITLE);
        EventManager.fire(event);
        if (event.isCancelled()) {
            cb.cancel();
            return;
        }
        if (event.isModified()) {
            cb.cancel();
            this.subtitle = Text.literal(event.getContent());
        }
    }

    @Inject(method = "setOverlayMessage", at = @At("HEAD"), cancellable = true)
    public void setOverlayMessage(Text message, boolean tinted, CallbackInfo cb) {
        EventTitle event = new EventTitle(message.getString(), EventTitle.TitleType.ACTIONBAR);
        EventManager.fire(event);
        if (event.isCancelled()) {
            cb.cancel();
            return;
        }
        if (event.isModified()) {
            cb.cancel();
            this.setCanShowChatDisabledScreen(false);
            this.overlayMessage = Text.literal(event.getContent());
            this.overlayRemaining = 60;
            this.overlayTinted = tinted;
        }
    }
}
