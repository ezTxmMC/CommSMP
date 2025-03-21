package de.eztxm.smp.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdventureColor {

    private static final Map<String, String> LEGACY_MAP = Map.ofEntries(Map.entry("0", "black"), Map.entry("1", "dark_blue"), Map.entry("2", "dark_green"), Map.entry("3", "dark_aqua"), Map.entry("4", "dark_red"), Map.entry("5", "dark_purple"), Map.entry("6", "gold"), Map.entry("7", "gray"), Map.entry("8", "dark_gray"), Map.entry("9", "blue"), Map.entry("a", "green"), Map.entry("b", "aqua"), Map.entry("c", "red"), Map.entry("d", "light_purple"), Map.entry("e", "yellow"), Map.entry("f", "white"), Map.entry("k", "obfuscated"), Map.entry("l", "bold"), Map.entry("m", "strikethrough"), Map.entry("n", "underlined"), Map.entry("o", "italic"), Map.entry("r", "reset"));
    private static final Pattern LEGACY_PATTERN = Pattern.compile("&([0-9A-Fa-fk-orK-OR])");

    public static Component deserializeMixed(String input) {
        Matcher matcher = LEGACY_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String code = matcher.group(1).toLowerCase();
            String tag = LEGACY_MAP.getOrDefault(code, "");
            matcher.appendReplacement(sb, "<" + tag + ">");
        }
        matcher.appendTail(sb);
        return MiniMessage.miniMessage().deserialize(sb.toString());
    }

    private static final Pattern GRADIENT_PATTERN = Pattern.compile("\\[([A-Fa-f0-9]{6})-([A-Fa-f0-9]{6})](.*?)(?=\\[|&|$)");
    private static final Pattern HEX_PATTERN = Pattern.compile("\\[([A-Fa-f0-9]{6})](.*?)(?=\\[|&|$)");

    public static Component apply(String text) {
        return processAll(text);
    }

    private static Component processAll(String text) {
        List<Component> parts = new ArrayList<>();
        int lastIndex = 0;
        Matcher gradientMatcher = GRADIENT_PATTERN.matcher(text);
        while (gradientMatcher.find()) {
            if (gradientMatcher.start() > lastIndex) {
                String before = text.substring(lastIndex, gradientMatcher.start());
                parts.add(processPlain(before));
            }
            String startHex = gradientMatcher.group(1);
            String endHex = gradientMatcher.group(2);
            String gradText = gradientMatcher.group(3);
            parts.add(applyGradient(startHex, endHex, gradText));
            lastIndex = gradientMatcher.end();
        }
        if (lastIndex < text.length()) {
            String tail = text.substring(lastIndex);
            parts.add(processPlain(tail));
        }
        return Component.join(JoinConfiguration.separator(Component.empty()), parts);
    }

    private static Component processPlain(String text) {
        return deserializeMixed(text);
    }

    private static Component applyGradient(String startHex, String endHex, String text) {
        int length = text.length();
        List<Component> letters = new ArrayList<>(length);
        int r1 = Integer.parseInt(startHex.substring(0, 2), 16);
        int g1 = Integer.parseInt(startHex.substring(2, 4), 16);
        int b1 = Integer.parseInt(startHex.substring(4, 6), 16);
        int r2 = Integer.parseInt(endHex.substring(0, 2), 16);
        int g2 = Integer.parseInt(endHex.substring(2, 4), 16);
        int b2 = Integer.parseInt(endHex.substring(4, 6), 16);

        for (int i = 0; i < length; i++) {
            double ratio = length > 1 ? (double) i / (length - 1) : 0;
            int red = (int) (r1 + ratio * (r2 - r1));
            int green = (int) (g1 + ratio * (g2 - g1));
            int blue = (int) (b1 + ratio * (b2 - b1));
            TextColor color = TextColor.fromHexString(String.format("#%02X%02X%02X", red, green, blue));
            letters.add(Component.text(String.valueOf(text.charAt(i))).color(color));
        }
        return Component.join(JoinConfiguration.separator(Component.empty()), letters);
    }

    public static TextColor hex(String color) {
        if (color.startsWith("#") && color.length() == 7) {
            return TextColor.fromHexString(color);
        }
        throw new IllegalArgumentException("Ungültiger Hex-Farbcode: " + color);
    }
}
