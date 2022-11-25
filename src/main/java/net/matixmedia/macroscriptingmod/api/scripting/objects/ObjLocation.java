package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;

public class ObjLocation extends Obj {
    public double x, y, z;

    public ObjLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
