package net.matixmedia.macroscriptingmod.utils;

import java.io.OutputStream;

public class RealTimeOutputStream extends OutputStream {

    public interface OutputCallback {
        void pipe(String text);
    }

    private StringBuilder stringBuilder;
    private final OutputCallback callback;

    public RealTimeOutputStream(OutputCallback callback) {
        this.stringBuilder = new StringBuilder();
        this.callback = callback;
    }

    @Override
    public void write(int b) {
        char c = (char) (Byte.toUnsignedInt((byte) b));
        if ((c == '\r' || c == '\n')) {
            if (this.stringBuilder.length() > 0) {
                this.callback.pipe(this.stringBuilder.toString());
                this.stringBuilder = new StringBuilder();
            }
        } else {
            this.stringBuilder.append(c);
        }
    }
}
