package net.matixmedia.macroscriptingmod.scripting.helpers;

import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventScriptStop;
import net.matixmedia.macroscriptingmod.exceptions.ScriptInterruptedException;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebsocketClientHandler extends WebSocketClient implements EventListener {

    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Lib/Http/WebSocket");

    private final UUID uuid;
    private final RunningScript runningScript;
    private final List<LuaFunction> openHandlers = new ArrayList<>();
    private final List<LuaFunction> messageHandlers = new ArrayList<>();
    private final List<LuaFunction> closeHandlers = new ArrayList<>();
    private final List<LuaFunction> errorHandlers = new ArrayList<>();

    public WebsocketClientHandler(URI serverUri, RunningScript runningScript) {
        super(serverUri);
        this.runningScript = runningScript;
        this.uuid = UUID.randomUUID();
        EventManager.registerListener(this);
    }

    @EventHandler
    public void onScriptStop(EventScriptStop.Pre event) {
        if (event.getRunningScript() == this.runningScript) event.cancel();
    }

    @EventHandler
    public void onScriptStopPost(EventScriptStop.Post event) {
        if (event.getRunningScript() == this.runningScript) this.close();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addOpenHandler(LuaFunction handler) {
        this.openHandlers.add(handler);
    }

    public void addMessageHandler(LuaFunction handler) {
        this.messageHandlers.add(handler);
    }

    public void addCloseHandler(LuaFunction handler) {
        this.closeHandlers.add(handler);
    }

    public void addErrorHandler(LuaFunction handler) {
        this.errorHandlers.add(handler);
    }

    private void handleEventException(Exception e) {
        if (e instanceof LuaError luaError) {
            if (!(luaError.getCause() instanceof ScriptInterruptedException)) {
                Chat.sendClientSystemMessage(Chat.Color.RED + "Error calling websocket event listener:\n" + e.getMessage());
                LOGGER.error("Error calling websocket event listener:");
                LOGGER.error(e.getMessage());
            }
        } else throw new RuntimeException(e);
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        try {
            for (LuaFunction handler : this.openHandlers) {
                handler.invoke();
            }
        } catch (Exception e) {
            this.handleEventException(e);
        }
    }

    @Override
    public void onMessage(String message) {
        try {
            for (LuaFunction handler : this.messageHandlers) {
                LOGGER.info("Invoking received handler");
                handler.invoke(LuaValue.valueOf(message));
                LOGGER.info("Called received handler");
            }
        } catch (Exception e) {
            this.handleEventException(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        try {
            for (LuaFunction handler : this.closeHandlers) {
                handler.invoke();
            }
        } catch (Exception e) {
            this.handleEventException(e);
        }
        EventManager.unregisterListener(this);
        this.runningScript.requestStop();
    }

    @Override
    public void onError(Exception ex) {
        try {
            for (LuaFunction handler : this.errorHandlers) {
                handler.invoke(LuaValue.valueOf(ex.toString()));
            }
        } catch (Exception e) {
            this.handleEventException(e);
        }
    }
}
