package net.matixmedia.macroscriptingmod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventDisconnect;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(DisconnectedScreen.class)
public abstract class MixinDisconnectedScreen {

    @Shadow @Final private Text reason;

    @Inject(method = "init", at = @At("HEAD"))
    protected void init(CallbackInfo cb) {
        EventManager.fire(new EventDisconnect(this.reason.getString()));
    }

}
