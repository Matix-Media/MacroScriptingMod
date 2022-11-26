package net.matixmedia.macroscriptingmod.mixins;

import com.mojang.authlib.GameProfile;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventChatMessage;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventTick;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    @Shadow
    private float lastYaw;
    @Shadow
    private float lastPitch;
    @Final
    @Shadow
    public ClientPlayNetworkHandler networkHandler;
    @Shadow
    @Final
    protected MinecraftClient client;

    @Shadow protected abstract void sendChatMessageInternal(String message, @Nullable Text preview);

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(at = @At("HEAD"), method = "sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V", cancellable = true)
    private void onSendChatMessage(String message, @Nullable Text preview, CallbackInfo cb)
    {
        EventChatMessage.Send event = new EventChatMessage.Send(message);
        EventManager.fire(event);

        if(event.isCanceled())
        {
            cb.cancel();
            return;
        }

        if(!event.isModified())
            return;

        this.sendChatMessageInternal(event.getMessage(), preview);

        cb.cancel();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", ordinal = 0), method = "tick()V")
    public void onTick(CallbackInfo cb) {
        EventManager.fire(new EventTick());
    }
}