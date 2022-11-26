package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventTick;
import net.matixmedia.macroscriptingmod.mixins.AccessorKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibInput extends Lib implements EventListener {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Lib/Input");
    private static LibInput INSTANCE;

    private final List<InputUtil.Key> pressedKeys = new ArrayList<>();

    public LibInput() {
        super("input");
        EventManager.registerListener(this);
        INSTANCE = this;
    }

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
        }};
        KeyBinding key = keyMappings.get(name);
        if (key != null) return ((AccessorKeyBinding) key).getBoundKey();
        else return null;
    }

    private static boolean isMouseButton(InputUtil.Key key) {
        GameOptions options = MinecraftClient.getInstance().options;
        return key.getCode() == ((AccessorKeyBinding) options.attackKey).getBoundKey().getCode() ||
                key.getCode() == ((AccessorKeyBinding) options.useKey).getBoundKey().getCode();
    }

    public static class KeyDown extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (INSTANCE == null) {
                LOGGER.info("Cannot press key, no instance");
                return null;
            }

            InputUtil.Key key = null;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else {
                int keyCode = arg.checkint();
                if (keyCode > 0 && keyCode < 255) key = InputUtil.fromKeyCode(keyCode, -1);
            }
            System.out.println("KEY: " + key);
            if (key == null) return null;
            System.out.println("Key Code: " + key.getCode());

            if (!INSTANCE.pressedKeys.contains(key))  INSTANCE.pressedKeys.add(key);
            return null;
        }
    }

    public static class KeyUp extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (INSTANCE == null) {
                LOGGER.info("Cannot press key, no instance");
                return null;
            }

            InputUtil.Key key = null;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else {
                int keyCode = arg.checkint();
                if (keyCode > 0 && keyCode < 255) key = InputUtil.fromKeyCode(keyCode, -1);
            }
            if (key == null) return null;

            INSTANCE.pressedKeys.remove(key);
            return null;
        }
    }

    public static class PressKey extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            InputUtil.Key key = null;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else {
                int keyCode = arg.checkint();
                if (keyCode > 0 && keyCode < 255) key = InputUtil.fromKeyCode(keyCode, -1);
            }
            System.out.println("KEY: " + key);
            if (key == null) return null;
            System.out.println("Key Code: " + key.getCode());

            KeyBinding.setKeyPressed(key, true);
            return null;
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (INSTANCE == null) return;

        for (InputUtil.Key key : INSTANCE.pressedKeys) {
            KeyBinding.setKeyPressed(key, true);
        }
    }
}
