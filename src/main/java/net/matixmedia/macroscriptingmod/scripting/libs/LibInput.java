package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.mixins.AccessorKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.HashMap;
import java.util.Map;

public class LibInput extends Lib {
    public LibInput() {
        super("input");
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
            InputUtil.Key key = null;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else {
                int keyCode = arg.checkint();
                if (keyCode > 0 && keyCode < 255) key = InputUtil.fromKeyCode(keyCode, -1);
            }
            System.out.println("KEY: " + key);
            if (key == null) return null;

            KeyBinding.setKeyPressed(key, true);
            return null;
        }
    }

    public static class KeyUp extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            InputUtil.Key key = null;
            if (arg.isstring()) key = LibInput.getKeyFromName(arg.checkjstring());
            else {
                int keyCode = arg.checkint();
                if (keyCode > 0 && keyCode < 255) key = InputUtil.fromKeyCode(keyCode, -1);
            }
            if (key == null) return null;

            KeyBinding.setKeyPressed(key, false);
            return null;
        }
    }

    public static class PressKey extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {

            return null;
        }
    }
}
