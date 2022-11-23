package net.matixmedia.macroscriptingmod.eventsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class EventManager {
    private static final Logger logger = LogManager.getLogger("MacroScripting/Events");
    private static final List<EventListener> listeners = new ArrayList<>();

    public static void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public static void unregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public static void fire(Event event) {
        // Create copy of list to prevent concurrent modification exception
        List<EventListener> listeners = new ArrayList<>(EventManager.listeners);

        // Iterate over EventListener objects to get methods
        for (EventListener listener : listeners) {

            // Get methods from EventListener objects
            for (Method method : listener.getClass().getMethods()) {

                // Check if parameter amount and annotation amount fits
                if (method.getParameterCount() != 1) continue;
                if (method.getAnnotations().length == 0) continue;

                // Check if method has EventHandler annotation
                boolean isEventHandler = method.getAnnotation(EventHandler.class) != null;
                if (!isEventHandler) continue;

                // Check if method is of event type
                boolean isOfEvent = method.getParameterTypes()[0].equals(event.getClass());
                if (!isOfEvent) continue;

                // Execute event handler
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    logger.error("Error calling event listener (" + method.getName() + "/" + event.getClass().getName() + ")", exception);
                }
            }
        }
    }
}
