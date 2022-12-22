package net.matixmedia.macroscriptingmod.api.scripting;

import com.google.common.base.CaseFormat;
import net.matixmedia.macroscriptingmod.exceptions.LibTypeException;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public abstract class Lib extends TwoArgFunction {
    // private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Lib");
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
        try {
            for (Class<?> implementedClasses : this.getClass().getClasses())  {
                if (!Modifier.isStatic(implementedClasses.getModifiers())) continue;
                if (!LibFunction.class.isAssignableFrom(implementedClasses)) continue;
                if (implementedClasses.getAnnotation(AutoLibFunction.class) == null) continue;

                String functionName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, implementedClasses.getSimpleName());

                LibFunction instance = null;
                for (Constructor<?> constructor : implementedClasses.getConstructors()) {
                    if (constructor.getTypeParameters().length != 0) continue;
                    instance = (LibFunction) constructor.newInstance();
                    break;
                }
                if (instance == null) continue;
                if (LibArgFunction.class.isAssignableFrom(implementedClasses))
                    ((LibArgFunction) instance).setRunningScript(this.runningScript);

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
