package net.matixmedia.macroscriptingmod.api.scripting;

import com.google.common.base.CaseFormat;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import net.matixmedia.macroscriptingmod.exceptions.LibTypeException;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.matixmedia.macroscriptingmod.scripting.Script;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class Lib extends TwoArgFunction {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Lib");
    private static final Map<Lib, RunningScript> INSTANCES = new HashMap<>();

    protected static Lib getInstance(Class<? extends Lib> type, RunningScript runningScript) {
        for (Map.Entry<Lib, RunningScript> entry : INSTANCES.entrySet()) {
            if (entry.getValue() != runningScript) continue;
            if (type.equals(entry.getKey().getClass())) return entry.getKey();
        }
        return null;
    }

    private RunningScript runningScript;

    private final String libraryName;
    private LuaValue env;

    public Lib(String libraryName) {
        super();
        this.libraryName = libraryName;
    }

    public void setRunningScript(RunningScript runningScript) {
        this.runningScript = runningScript;
    }

    public RunningScript getRunningScript() {
        return this.runningScript;
    }

    @Override
    public LuaValue call(LuaValue modName, LuaValue env) {
        LuaValue lib = tableOf();
        this.env = env;
        try {
            Class<LibFunction> libFunctionClass = LibFunction.class;
            Class<LibArgFunction> libArgFunctionClass = LibArgFunction.class;
            for (Class<?> implementedClasses : this.getClass().getClasses())  {
                if (!Modifier.isStatic(implementedClasses.getModifiers())) continue;
                if (!libFunctionClass.isAssignableFrom(implementedClasses)) continue;

                String functionName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, implementedClasses.getSimpleName());

                LibFunction instance = null;
                for (Constructor<?> constructor : implementedClasses.getConstructors()) {
                    if (constructor.getTypeParameters().length != 0) continue;
                    instance = (LibFunction) constructor.newInstance();
                    break;
                }
                if (instance == null) continue;
                if (libArgFunctionClass.isAssignableFrom(implementedClasses))
                    ((LibArgFunction) instance).setRunningScript(this.runningScript);

                LOGGER.info("Registered " + libraryName + "#" + functionName);
                lib.set(functionName, instance);
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new LibTypeException(e);
        }

        env.set(libraryName, lib);
        env.get("package").get("loaded").set(libraryName, lib);

        INSTANCES.put(this, this.runningScript);
        this.init();

        return lib;
    }

    public void init() {}

    public void dispose() {
        INSTANCES.remove(this);
    }
}
