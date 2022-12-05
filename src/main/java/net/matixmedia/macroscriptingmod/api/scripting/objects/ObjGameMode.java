package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.minecraft.world.GameMode;

public class ObjGameMode extends Obj {
    public String name;
    public int ID;

    public ObjGameMode(GameMode gameMode) {
        this.name = gameMode.getName();
        this.ID = gameMode.getId();
    }
}
