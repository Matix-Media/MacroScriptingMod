package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.CancellableEvent;
import net.matixmedia.macroscriptingmod.eventsystem.Event;
import net.minecraft.client.network.ServerInfo;

public class EventConnectToServer extends CancellableEvent {
    private final ServerInfo serverInfo;

    public EventConnectToServer(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }
}
