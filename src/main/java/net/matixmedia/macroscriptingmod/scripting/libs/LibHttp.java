package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import org.luaj.vm2.LuaValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LibHttp extends Lib {
    public static final String USER_AGENT = "MacroScriptingMod/1.0 (Using Lua)";

    public LibHttp() {
        super("http");
    }

    @AutoLibFunction
    public static class Get extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            try {
                URL url = new URL(arg.checkjstring());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", USER_AGENT);
                int statusCode = connection.getResponseCode();

                BufferedReader in;
                if (statusCode < 400) in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                else in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                connection.disconnect();
                return LuaValue.valueOf(content.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
