package net.matixmedia.macroscriptingmod.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventChatMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public abstract class MixinChatHud {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(Text chatMessage, CallbackInfo cb) {
        EventChatMessage.Receive event = new EventChatMessage.Receive(chatMessage.getString());
        EventManager.fire(event);

        if (event.isCanceled()) {
            cb.cancel();
            return;
        }

        if (event.isModified()) {
            shadow$addMessage(Text.literal(event.getMessage()), null, MessageIndicator.system());
            cb.cancel();
        }
    }

    @Shadow
    public void shadow$addMessage(Text message, @Nullable MessageSignatureData signature, @Nullable MessageIndicator indicator) {}
}
