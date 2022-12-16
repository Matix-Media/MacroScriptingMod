package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;

public class ObjPlayer extends Obj {

    public final String name;
    public final String display_name;
    public final String UUID;

    public ObjPlayer(ClientPlayerEntity player) {
        this.name = player.getGameProfile().getName();
        this.UUID = player.getUuid().toString();
        this.display_name = player.getDisplayName().getString();
    }

    public ObjPlayer(PlayerListEntry playerListEntry) {
        MinecraftClient mc = MinecraftClient.getInstance();
        String displayName = mc.inGameHud.getPlayerListHud().getPlayerName(playerListEntry).getString();

        this.name = playerListEntry.getProfile().getName();
        this.UUID = playerListEntry.getProfile().getId().toString();
        this.display_name = displayName;
    }
}
