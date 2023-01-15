package net.matixmedia.macroscriptingmod.scripting.helpers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StorageManager {

    private final Path storageFilePath;
    private Map<String, Object> storage = new HashMap<>();

    public StorageManager(Path storageFilePath) {
        this.storageFilePath = storageFilePath;
    }

    private void loadStorage() throws IOException {
        if (!Files.exists(this.storageFilePath)) return;
        String formattedStorage = Files.readString(this.storageFilePath);
        Gson gson = new Gson();
        this.storage = gson.fromJson(formattedStorage, new TypeToken<Map<String, Object>>() {}.getType());
    }

    private void saveStorage() throws IOException {
        Gson gson = new Gson();
        String formattedStorage = gson.toJson(this.storage);
        Files.writeString(this.storageFilePath, formattedStorage);
        this.loadStorage();
    }

    public boolean exists(String key) {
        return this.storage.containsKey(key);
    }

    public Object get(String key) {
        return this.storage.get(key);
    }

    public void save(String key, Object value) throws IOException {
        this.storage.put(key, value);
        this.saveStorage();
    }

    public void delete(String key) throws IOException {
        this.storage.remove(key);
        this.saveStorage();
    }
}
