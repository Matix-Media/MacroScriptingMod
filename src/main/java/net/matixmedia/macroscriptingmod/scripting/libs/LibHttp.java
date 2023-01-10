package net.matixmedia.macroscriptingmod.scripting.libs;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjWebsocket;
import net.matixmedia.macroscriptingmod.scripting.helpers.WebsocketClientHandler;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class LibHttp extends Lib {
    private static final String USER_AGENT = "MacroScriptingMod/1.0 (Using Lua)";
    private static final String CHARSET = "UTF-8";

    public LibHttp() {
        super("http");
    }

    private HttpURLConnection openConnection(String url, String method) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return connection;
    }

    private HttpURLConnection attachRequestBody(HttpURLConnection connection, String contentType, String body) throws IOException {
        byte[] out = body.getBytes(CHARSET);
        int length = out.length;
        connection.setFixedLengthStreamingMode(length);
        connection.setRequestProperty("Content-Type", contentType + "; charset=" + CHARSET);
        connection.connect();
        OutputStream stream = connection.getOutputStream();
        stream.write(out);
        return connection;
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in;
        if (connection.getResponseCode() < 400) in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    @AutoLibFunction
    public static class Get extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibHttp instance = (LibHttp) getInstance(LibHttp.class, this.getRunningScript());
            if (instance == null) return null;

            try {
                HttpURLConnection connection = instance.openConnection(arg.checkjstring(), "GET");
                String response = instance.readResponse(connection);
                connection.disconnect();
                return LuaValue.valueOf(response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AutoLibFunction
    public static class Post extends LibArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibHttp instance = (LibHttp) getInstance(LibHttp.class, this.getRunningScript());
            if (instance == null) return null;

            try {
                HttpURLConnection connection = instance.openConnection(arg.checkjstring(), "POST");
                String response = instance.readResponse(connection);
                connection.disconnect();
                return LuaValue.valueOf(response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            LibHttp instance = (LibHttp) getInstance(LibHttp.class, this.getRunningScript());
            if (instance == null) return null;

            if (!arg2.isstring() && !arg2.istable()) return argerror(2, "Expected string or table");

            try {
                HttpURLConnection connection = instance.openConnection(arg1.checkjstring(), "POST");
                connection.setDoOutput(true);

                if (arg2.istable()) {
                    LuaTable table = arg2.checktable();
                    StringJoiner joiner = new StringJoiner("&");
                    for (LuaValue key : table.keys()) {
                        LuaValue value = table.get(key);
                        if (!key.isstring()) return argerror(2, "Table keys must be strings");
                        if (!value.isstring() && !value.isboolean()&& !value.isnumber())
                            return argerror(2, "Table values must be strings, booleans or numbers");
                        joiner.add(URLEncoder.encode(key.tojstring(), CHARSET) + "="
                                    + URLEncoder.encode(value.tojstring(), CHARSET));
                    }

                    instance.attachRequestBody(connection, "application/x-www-form-urlencoded", joiner.toString());
                } else {
                    String mimeType = "text/plain";
                    String body = arg2.tojstring();

                    // Test if JSON
                    try {
                        JsonParser.parseString(body);
                        mimeType = "application/json";
                    } catch (JsonSyntaxException ignored) {}
                    // Test if XML
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        builder.parse(body);
                        mimeType = "application/xml";
                    } catch (ParserConfigurationException | SAXException ignored) {}

                    instance.attachRequestBody(connection, mimeType, arg2.tojstring());
                }

                String response = instance.readResponse(connection);
                connection.disconnect();
                return LuaValue.valueOf(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AutoLibFunction
    public static class Websocket extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            try {
                WebsocketClientHandler client = new WebsocketClientHandler(new URI(arg.checkjstring()), this.getRunningScript());
                client.connect();
                return new ObjWebsocket(client).toLua();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
