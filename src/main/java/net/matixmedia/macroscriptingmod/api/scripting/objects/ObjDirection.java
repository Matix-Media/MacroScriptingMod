package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;

public class ObjDirection extends Obj {
    public double yaw, pitch;

    public ObjDirection(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
