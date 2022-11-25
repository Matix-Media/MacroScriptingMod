package net.matixmedia.macroscriptingmod.api.scripting;

import com.google.common.base.CaseFormat;
import net.matixmedia.macroscriptingmod.exceptions.LibTypeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Lib extends TwoArgFunction {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Lib");
    private static final Pattern CASE_CONVERTER_PATTERN = Pattern.compile("(\\B)([A-Z])", Pattern.MULTILINE);
    private static final String CASE_CONVERTER_SUBST = "_\\2";

    private final String libraryName;

    public Lib(String libraryName) {
        super();
        this.libraryName = libraryName;
    }

    @Override
    public LuaValue call(LuaValue modName, LuaValue env) {
        LuaValue lib = tableOf();
        try {
            Class<LibFunction> libFunctionClass = LibFunction.class;
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
                LOGGER.info("Registered " + libraryName + "#" + functionName);
                lib.set(functionName, instance);
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new LibTypeException(e);
        }

        env.set(libraryName, lib);
        env.get("package").get("loaded").set(libraryName, lib);
        return lib;
    }
}
