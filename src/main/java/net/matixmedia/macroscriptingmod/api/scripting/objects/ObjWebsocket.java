package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.matixmedia.macroscriptingmod.scripting.helpers.WebsocketClientHandler;
import org.luaj.vm2.LuaFunction;

public class ObjWebsocket extends Obj {

    private final WebsocketClientHandler client;

    public final String UUID;

    public ObjWebsocket(WebsocketClientHandler client) {
        this.client = client;
        this.UUID = client.getUuid().toString();
    }

    public void on_open(LuaFunction function) {
        System.out.println("Registering on open");
        client.addOpenHandler(function);
    }

    public void on_message(LuaFunction function) {
        client.addMessageHandler(function);
    }

    public void on_close(LuaFunction function) {
        client.addCloseHandler(function);
    }

    public void on_error(LuaFunction function) {
        client.addErrorHandler(function);
    }

    public void send(String message) {
        System.out.println("SEND MESSAGE");
        client.send(message);
    }

}
