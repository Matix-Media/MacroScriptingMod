package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjItem;
import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventRender;
import net.matixmedia.macroscriptingmod.scripting.helpers.ItemSearch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.OptimizeWorldScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

public class LibGui extends Lib implements EventListener {
    private static final Map<Class<? extends Screen>, String> GUI_SCREENS = new HashMap<>() {{
        put(Screen.class, "UNKNOWN");
        put(DemoScreen.class, "demo");
        put(GameMenuScreen.class, "game_menu");
        put(MultiplayerScreen.class, "multiplayer");
        put(AddServerScreen.class, "add_server");
        put(DownloadingTerrainScreen.class, "downloading_terrain");
        put(PresetsScreen.class, "presets");
        put(CreateWorldScreen.class, "create_world");
        put(EditWorldScreen.class, "edit_world");
        put(EditGameRulesScreen.class, "edit_gamerules");
        put(OptimizeWorldScreen.class, "optimize_world");

        put(GameOptionsScreen.class, "options");
        put(ControlsOptionsScreen.class, "controls");
        put(SkinOptionsScreen.class, "skin_options");
        put(LanguageOptionsScreen.class, "language_options");
        put(SoundOptionsScreen.class, "sound_options");
        put(PackScreen.class, "resourcepack_options");
        put(VideoOptionsScreen.class, "video_options");

        put(StatsScreen.class, "stats");
        put(ConfirmLinkScreen.class, "confirm_link");
        put(FatalErrorScreen.class, "fatal_error");
        put(DisconnectedScreen.class, "disconnected");

        put(AdvancementsScreen.class, "advancements");
        put(EnchantmentScreen.class, "enchantment");
        put(ChatScreen.class, "chat");
        put(DeathScreen.class, "death");
        put(HopperScreen.class, "hopper");
        put(MerchantScreen.class, "merchant");
        put(BookScreen.class, "book");
        put(BookEditScreen.class, "edit_book");
        put(BeaconScreen.class, "beacon");
        put(BrewingStandScreen.class, "brewing_stand");
        put(AnvilScreen.class, "anvil");
        put(BlastFurnaceScreen.class, "blast_furnace");
        put(CartographyTableScreen.class, "cartography_table");
        put(CraftingScreen.class, "crafting");
        put(ForgingScreen.class, "forging");
        put(FurnaceScreen.class, "furnace");
        put(GenericContainerScreen.class, "container");
        put(GrindstoneScreen.class, "grindstone");
        put(HorseScreen.class, "horse");
        put(JigsawBlockScreen.class, "jigsaw");
        put(LecternScreen.class, "lectern");
        put(LoomScreen.class, "loom");
        put(CommandBlockScreen.class, "command_block");
        put(MinecartCommandBlockScreen.class, "minecart_command_block");
        put(ShulkerBoxScreen.class, "shulker_box");
        put(SignEditScreen.class, "edit_sign");
        put(SmithingScreen.class, "smithing");
        put(SmokerScreen.class, "smoker");
        put(StonecutterScreen.class, "stonecutter");
        put(StructureBlockScreen.class, "structure_block");
        put(InventoryScreen.class, "inventory");
        put(CreativeInventoryScreen.class, "creative_inventory");
    }};

    private Screen screenToSet;
    private boolean setScreenNull = false;
    private boolean dispose = false;
    private boolean disposed = false;

    public LibGui() {
        super("gui");
    }

    @Override
    public void init() {
        EventManager.registerListener(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.dispose = true;
    }

    @EventHandler
    public void onRender(EventRender event) {
        if (this.disposed) return;

        MinecraftClient mc = MinecraftClient.getInstance();

        if (this.setScreenNull) {
            mc.setScreen(null);
            this.screenToSet = null;
            this.setScreenNull = false;
            System.out.println("Setting screen to null");
        }
        if (this.screenToSet != null) {
            System.out.println("Setting screen: " + this.screenToSet.getClass().getSimpleName());
            mc.setScreen(this.screenToSet);
            this.screenToSet = null;
        }

        if (this.dispose) {
            EventManager.unregisterListener(this);
            this.disposed = true;
        }
    }

    @AutoLibFunction
    public static class GetCurrentGui extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return NIL;
            if (mc.currentScreen == null) return NIL;

            Class<? extends Screen> screenClass = Screen.class;
            Class<? extends Screen> currentScreenClass = mc.currentScreen.getClass();

            for (Map.Entry<Class<? extends Screen>, String> entry : GUI_SCREENS.entrySet()) {
                if (entry.getKey().isAssignableFrom(currentScreenClass) && !(entry.getKey().isAssignableFrom(screenClass)))
                    screenClass = entry.getKey();
            }

            if (screenClass.equals(Screen.class)) {
                return LuaValue.valueOf(mc.currentScreen.getClass().getSimpleName().toLowerCase());
            } else {
                return LuaValue.valueOf(GUI_SCREENS.get(screenClass));
            }
        }
    }

    @AutoLibFunction
    public static class Open extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibGui instance = (LibGui) getInstance(LibGui.class, this.getRunningScript());
            if (instance == null) return null;

            MinecraftClient mc = MinecraftClient.getInstance();
            String guiName = arg.checkjstring();
            Screen currentScreen = mc.currentScreen;

            switch (guiName.toLowerCase()) {
                case "chat" -> instance.screenToSet = (new ChatScreen(""));
                case "menu" -> instance.screenToSet = (new GameMenuScreen(true));
                case "options" -> instance.screenToSet = (new OptionsScreen(null, mc.options));
                case "video_options" -> instance.screenToSet = (new VideoOptionsScreen(currentScreen == null ? new OptionsScreen(null, mc.options) : currentScreen, mc.options));
                case "controls" -> instance.screenToSet = (new ControlsOptionsScreen(currentScreen, mc.options));
                case "inventory" -> {
                    if (mc.player == null) return null;
                    instance.screenToSet = new InventoryScreen(mc.player);
                }
            }

            System.out.println("Target Screen: " + instance.screenToSet.getClass().getSimpleName());

            return null;
        }
    }

    @AutoLibFunction
    public static class CloseCurrentGui extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            LibGui instance = (LibGui) getInstance(LibGui.class, this.getRunningScript());
            if (instance == null) return null;

            MinecraftClient mc = MinecraftClient.getInstance();
            Screen currentScreen = mc.currentScreen;

            if (currentScreen == null) return null;

            if (currentScreen instanceof ChatScreen || currentScreen instanceof GameMenuScreen || currentScreen instanceof InventoryScreen ||
                currentScreen instanceof OptionsScreen || currentScreen instanceof VideoOptionsScreen || currentScreen instanceof ControlsOptionsScreen ||
                currentScreen instanceof GenericContainerScreen || currentScreen instanceof CreativeInventoryScreen) {

                instance.setScreenNull = true;
            }

            return null;
        }
    }

    @AutoLibFunction
    public static class FindItem extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (!(mc.currentScreen instanceof GenericContainerScreen)) return NIL;

            ItemSearch search = new ItemSearch(arg.checkjstring());
            Inventory inventory = ((GenericContainerScreen) mc.currentScreen).getScreenHandler().getInventory();

            for (int i = 0; i < inventory.size(); i++) {
                ItemStack slot = inventory.getStack(i);
                if (search.matches(slot)) return LuaValue.valueOf(i);
            }

            return NIL;
        }
    }

    @AutoLibFunction
    public static class GetItem extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (!(mc.currentScreen instanceof GenericContainerScreen)) return NIL;
            int slot = arg.checkint();
            Inventory inventory = ((GenericContainerScreen) mc.currentScreen).getScreenHandler().getInventory();

            return new ObjItem(inventory.getStack(slot)).toLua();
        }
    }
}
