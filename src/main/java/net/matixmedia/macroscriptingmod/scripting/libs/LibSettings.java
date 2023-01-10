package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.minecraft.client.option.ChatVisibility;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.luaj.vm2.LuaValue;

public class LibSettings extends Lib {

    public LibSettings() {
        super("settings");
    }

    @AutoLibFunction
    public static class SetFov extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            GameOptions options = this.getMinecraft().options;
            int fov = arg.checkint();
            if (fov < 30 || fov > 110) return argerror(1, "FOV needs to be between 30 and 110");
            options.getFov().setValue(fov);
            options.sendClientSettings();
            return null;
        }
    }

    @AutoLibFunction
    public static class SetChatVisibility extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            GameOptions options = this.getMinecraft().options;
            switch (arg.checkjstring()) {
                case "FULL" -> options.getChatVisibility().setValue(ChatVisibility.FULL);
                case "SYSTEM" -> options.getChatVisibility().setValue(ChatVisibility.SYSTEM);
                case "HIDDEN" -> options.getChatVisibility().setValue(ChatVisibility.HIDDEN);
            }
            options.sendClientSettings();
            return null;
        }
    }

    @AutoLibFunction
    public static class SetPerspective extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            GameOptions options = this.getMinecraft().options;
            switch (arg.checkjstring()) {
                case "FIRST_PERSON" -> options.setPerspective(Perspective.FIRST_PERSON);
                case "BACK" -> options.setPerspective(Perspective.THIRD_PERSON_BACK);
                case "FRONT" -> options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
            options.sendClientSettings();
            return null;
        }
    }


}
