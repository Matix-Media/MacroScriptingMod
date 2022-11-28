package net.matixmedia.macroscriptingmod.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.LibFunction;

import java.util.List;

public class GlobalsHolder {
    private final Globals globals;
    private final List<LibFunction> libraries;

    public GlobalsHolder(Globals globals, List<LibFunction> libraries) {
        this.globals = globals;
        this.libraries = libraries;
    }

    public Globals getGlobals() {
        return globals;
    }

    public List<LibFunction> getLibraries() {
        return libraries;
    }
}
