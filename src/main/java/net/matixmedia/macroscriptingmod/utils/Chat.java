package net.matixmedia.macroscriptingmod.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Pattern;

public class Chat {

    // Credits to md_5
    public static enum Color {
        /**
         * Represents black
         */
        BLACK('0', 0x00),
        /**
         * Represents dark blue
         */
        DARK_BLUE('1', 0x1),
        /**
         * Represents dark green
         */
        DARK_GREEN('2', 0x2),
        /**
         * Represents dark blue (aqua)
         */
        DARK_AQUA('3', 0x3),
        /**
         * Represents dark red
         */
        DARK_RED('4', 0x4),
        /**
         * Represents dark purple
         */
        DARK_PURPLE('5', 0x5),
        /**
         * Represents gold
         */
        GOLD('6', 0x6),
        /**
         * Represents gray
         */
        GRAY('7', 0x7),
        /**
         * Represents dark gray
         */
        DARK_GRAY('8', 0x8),
        /**
         * Represents blue
         */
        BLUE('9', 0x9),
        /**
         * Represents green
         */
        GREEN('a', 0xA),
        /**
         * Represents aqua
         */
        AQUA('b', 0xB),
        /**
         * Represents red
         */
        RED('c', 0xC),
        /**
         * Represents light purple
         */
        LIGHT_PURPLE('d', 0xD),
        /**
         * Represents yellow
         */
        YELLOW('e', 0xE),
        /**
         * Represents white
         */
        WHITE('f', 0xF),
        /**
         * Represents magical characters that change around randomly
         */
        MAGIC('k', 0x10, true),
        /**
         * Makes the text bold.
         */
        BOLD('l', 0x11, true),
        /**
         * Makes a line appear through the text.
         */
        STRIKETHROUGH('m', 0x12, true),
        /**
         * Makes the text appear underlined.
         */
        UNDERLINE('n', 0x13, true),
        /**
         * Makes the text italic.
         */
        ITALIC('o', 0x14, true),
        /**
         * Resets all previous chat colors or formats.
         */
        RESET('r', 0x15);

        public static final char COLOR_CHAR = '\u00A7';
        private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-ORX]");

        private final int intCode;
        private final char code;
        private final boolean isFormat;
        private final String toString;

        private static final Map<Integer, Color> BY_ID = Maps.newHashMap();
        private static final Map<Character, Color> BY_CHAR = Maps.newHashMap();

        private Color(char code, int intCode) {
            this(code, intCode, false);
        }

        private Color(char code, int intCode, boolean isFormat) {
            this.code = code;
            this.intCode = intCode;
            this.isFormat = isFormat;
            this.toString = new String(new char[] {COLOR_CHAR, code});
        }

        public char getChar() {
            return code;
        }
        public static Color getByChar(char code) {
            return BY_CHAR.get(code);
        }
        public static Color getByChar(@NotNull String code) {
            Preconditions.checkArgument(code != null, "Code cannot be null");
            Preconditions.checkArgument(code.length() > 0, "Code must have at least one char");

            return BY_CHAR.get(code.charAt(0));
        }


        @Override
        public String toString() {
            return toString;
        }
        public boolean isFormat() {
            return isFormat;
        }

        @Contract("!null -> !null; null -> null")
        public static String stripColor(@Nullable final String input) {
            if (input == null) {
                return null;
            }

            return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
        }

        public static String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
            Preconditions.checkArgument(textToTranslate != null, "Cannot translate null text");

            char[] b = textToTranslate.toCharArray();
            for (int i = 0; i < b.length - 1; i++) {
                if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                    b[i] = Color.COLOR_CHAR;
                    b[i + 1] = Character.toLowerCase(b[i + 1]);
                }
            }
            return new String(b);
        }

        private static final Pattern HEX_COLOR_PATTERN = Pattern.compile(COLOR_CHAR + "x(?>" + COLOR_CHAR + "[0-9a-f]){6}", Pattern.CASE_INSENSITIVE);

        static {
            for (Color color : values()) {
                BY_ID.put(color.intCode, color);
                BY_CHAR.put(color.code, color);
            }
        }
    }


    public static void sendClientSystemMessage(String message) {
        Chat.sendClientMessage(MacroScriptingMod.getChatPrefix() + message);
    }

    public static void sendClientMessage(String message) {
        Chat.sendClientMessage(Text.literal(message));
    }

    public static void sendClientMessage(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }
}
