package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventTick;
import net.matixmedia.macroscriptingmod.mixins.AccessorKeyBinding;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.LuaValue;

import java.util.*;

public class LibInput extends Lib implements EventListener {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Lib/Input");

    private static InputUtil.Key getKeyFromName(String name) {
        GameOptions options = MinecraftClient.getInstance().options;

        Map<String, KeyBinding> keyMappings = new HashMap<>() {{
            put("forward", options.forwardKey);
            put("back", options.backKey);
            put("left", options.leftKey);
            put("right", options.rightKey);
            put("jump", options.jumpKey);
            put("sneak", options.sneakKey);
            put("player_list", options.playerListKey);
            put("sprint", options.sprintKey);
            put("attack", options.attackKey);
            put("use", options.useKey);
        }};
        KeyBinding keyBinding = keyMappings.get(name);
        if (keyBinding != null) return ((AccessorKeyBinding) keyBinding).getBoundKey();

        InputUtil.Key key = InputUtil.fromTranslationKey(name);
        if (key == null) key = InputUtil.fromTranslationKey("key.keyboard." + name);
        if (key == null) key = InputUtil.fromTranslationKey("key." + name);
        return key;
    }

    private static InputUtil.Key getKeyFromCode(int keyCode) {
        if (keyCode > 0 && keyCode < 255) return InputUtil.fromKeyCode(keyCode, -1);
        else return null;
    }


    // ========================== Members ==========================


    private final List<KeyModifier> keyModifiers = new ArrayList<>();

    public LibInput() {
        super("input");
        EventManager.registerListener(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        EventManager.unregisterListener(this);

        for (KeyModifier modifier : this.keyModifiers) {
            KeyBinding.setKeyPressed(modifier.key, false);
        }
    }

    @AutoLibFunction
    public static class KeyDown extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibInput instance = (LibInput) getInstance(LibInput.class, this.getRunningScript());
            if (instance == null) return null;

            InputUtil.Key key;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else key = LibInput.getKeyFromCode(arg.checkint());
            if (key == null) return null;

            for (KeyModifier modifier : instance.keyModifiers) {
                if (!modifier.matches(key)) continue;
                return null;
            }
            instance.keyModifiers.add(new KeyModifier(key));
            return null;
        }
    }

    @AutoLibFunction
    public static class KeyUp extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibInput instance = (LibInput) getInstance(LibInput.class, this.getRunningScript());
            if (instance == null) return null;

            InputUtil.Key key;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else key = LibInput.getKeyFromCode(arg.checkint());
            if (key == null) return null;

            for (KeyModifier modifier : instance.keyModifiers) {
                if (!modifier.matches(key)) continue;
                modifier.unPress = true;
            }
            return null;
        }
    }

    @AutoLibFunction
    public static class PressKey extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibInput instance = (LibInput) getInstance(LibInput.class, this.getRunningScript());
            if (instance == null) return null;

            InputUtil.Key key;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else key = LibInput.getKeyFromCode(arg.checkint());
            if (key == null) return null;

            instance.keyModifiers.add(new KeyModifier(key, true));
            return null;
        }
    }

    private static class KeyModifier {
        private final InputUtil.Key key;
        private final boolean once;
        private int ticks = 0;
        private boolean unPress = false;

        public KeyModifier(InputUtil.Key key, boolean once) {
            this.key = key;
            this.once = once;
        }

        public KeyModifier(InputUtil.Key key) {
            this(key, false);
        }

        public boolean isActive() {
            return (!this.once && !this.unPress);
        }

        public boolean matches(InputUtil.Key key) {
            return (this.isActive() && key.getCode() == this.key.getCode());
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        ListIterator<KeyModifier> iterator = this.keyModifiers.listIterator();

        while (iterator.hasNext()) {
            KeyModifier modifier = iterator.next();

            if (modifier.unPress || (modifier.once && modifier.ticks > 3)) {
                KeyBinding.setKeyPressed(modifier.key, false);
                iterator.remove();
            } else {
                modifier.ticks++;
                KeyBinding.setKeyPressed(modifier.key, true);
            }
        }
    }

}
