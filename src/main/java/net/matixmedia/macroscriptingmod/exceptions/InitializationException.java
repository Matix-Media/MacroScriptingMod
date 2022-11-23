package net.matixmedia.macroscriptingmod.exceptions;

public class InitializationException extends RuntimeException{
    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Exception subError) {
        super(message, subError);
    }
}
