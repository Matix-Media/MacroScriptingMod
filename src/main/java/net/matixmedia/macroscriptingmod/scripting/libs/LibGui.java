package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.*;
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
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.luaj.vm2.LuaValue;
import org.lwjgl.glfw.GLFW;

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
            if (this.getMinecraft().player == null) return NIL;
            ItemSearch search = new ItemSearch(arg.checkjstring());

            for (Slot slot : this.getMinecraft().player.currentScreenHandler.slots) {
                if (search.matches(slot.getStack())) return LuaValue.valueOf(slot.id);
            }

            return NIL;
        }
    }

    @AutoLibFunction
    public static class GetItem extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (this.getMinecraft().player == null) return NIL;
            int slot = arg.checkint();

            return new ObjItem(this.getMinecraft().player.currentScreenHandler.slots.get(slot).getStack()).toLua();
        }
    }

    @AutoLibFunction
    public static class ClickSlot extends LibArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            int slot = arg.checkint();
            this.clickSlot(slot, false, GLFW.GLFW_MOUSE_BUTTON_LEFT);
            return null;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            int slot = arg1.checkint();
            boolean shift = arg2.checkboolean();
            this.clickSlot(slot, shift, GLFW.GLFW_MOUSE_BUTTON_LEFT);
            return null;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            int slot = arg1.checkint();
            boolean shift = arg2.checkboolean();
            String buttonName = arg3.checkjstring();
            int button;
            switch (buttonName) {
                case "LEFT" -> button = GLFW.GLFW_MOUSE_BUTTON_LEFT;
                case "RIGHT" -> button = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
                case "MIDDLE" -> button = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
                default -> {
                    return argerror(3, "Mouse button must either be LEFT, RIGHT or MIDDLE");
                }
            }
            this.clickSlot(slot, shift, button);
            return null;
        }

        private void clickSlot(int slot, boolean shift, int button) {
            if (this.getMinecraft().player == null || this.getMinecraft().interactionManager == null || this.getMinecraft().currentScreen == null) return;
            SlotActionType actionType = SlotActionType.PICKUP;

            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                actionType = shift ? SlotActionType.QUICK_MOVE : SlotActionType.PICKUP;
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) actionType = SlotActionType.CLONE;

            this.getMinecraft().interactionManager.clickSlot(this.getMinecraft().player.currentScreenHandler.syncId, slot, button, actionType, this.getMinecraft().player);
        }
    }
}
