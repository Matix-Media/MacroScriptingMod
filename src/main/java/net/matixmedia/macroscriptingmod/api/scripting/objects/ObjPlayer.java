package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;

public class ObjPlayer extends Obj {

    private String name;
    private String UUID;

    public ObjPlayer(ClientPlayerEntity player) {
        this.name = player.getGameProfile().getName();
        this.UUID = player.getUuid().toString();
    }

    public ObjPlayer(PlayerListEntry playerListEntry) {
        this.name = playerListEntry.getProfile().getName();
        this.UUID = playerListEntry.getProfile().getId().toString();
    }
}
