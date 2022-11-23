package net.matixmedia.macroscriptingmod.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;

public class RealTimeOutputStream extends OutputStream {

    public static interface OutputCallback {
        void pipe(String text);
    }

    private StringBuilder stringBuilder;
    private OutputCallback callback;

    public RealTimeOutputStream(OutputCallback callback) {
        this.stringBuilder = new StringBuilder();
        this.callback = callback;
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
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
