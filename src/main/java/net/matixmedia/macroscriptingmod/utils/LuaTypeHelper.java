package net.matixmedia.macroscriptingmod.utils;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;

public class LuaTypeHelper {
    public static HashMap<?, ?> convertTableToMap(LuaTable table) {
        HashMap<Object, Object> map = new HashMap<>();
        LuaValue[] rootKeys = table.keys();
        for (LuaValue k : rootKeys) {
            if (table.get(k).istable()) {
                map.put(k, convertTableToMap(table.get(k).checktable()));
            } else {
                map.put(k, table.get(k).touserdata());
            }
        }
        return map;
    }
}
