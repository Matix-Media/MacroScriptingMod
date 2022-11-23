package net.matixmedia.macroscriptingmod.scripting;

import net.matixmedia.macroscriptingmod.utils.Chat;
import net.matixmedia.macroscriptingmod.utils.RealTimeOutputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.*;

import java.io.*;

public class Runtime {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Runtime");

    private final Globals globals;

    public Runtime() {
        globals = this.createGlobals();
    }

    public Globals createGlobals() {
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new JseMathLib());
        globals.load(new CoroutineLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        LoadState.install(globals);
        LuaC.install(globals);

        // Redirect console
        RealTimeOutputStream outputStream = new RealTimeOutputStream(Chat::sendClientMessage);
        globals.STDOUT = new PrintStream(outputStream, true);

        return globals;
    }

    public LuaValue execute(Script script) throws IOException {
        LuaValue chunk = this.loadScript(script);
        return chunk.call();
    }

    private LuaValue loadScript(Script script) throws IOException {
        return this.globals.load(script.getContent());
    }
}
