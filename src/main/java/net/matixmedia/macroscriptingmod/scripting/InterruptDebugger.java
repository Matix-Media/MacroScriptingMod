package net.matixmedia.macroscriptingmod.scripting;

import net.matixmedia.macroscriptingmod.exceptions.ScriptInterruptedException;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.DebugLib;

public class InterruptDebugger extends DebugLib {

    private boolean interrupt = false;

    public void interrupt() {
        this.interrupt = true;
    }

    @Override
    public void onInstruction(int pc, Varargs v, int top) {
        if (this.interrupt) {
            throw new ScriptInterruptedException("The script got interrupted");
        }
        super.onInstruction(pc, v, top);
    }

    public boolean isInterrupted() {
        return interrupt;
    }
}
