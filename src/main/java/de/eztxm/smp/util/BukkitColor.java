package de.eztxm.smp.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitColor {

    private static final Pattern GRADIENT_PATTERN = Pattern.compile("\\[([A-Fa-f0-9]{6})-([A-Fa-f0-9]{6})](.*?)(?=\\[|&|$)");
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("&([0-9A-Fa-fLl])");
    private static final Pattern HEX_PATTERN = Pattern.compile("\\[([A-Fa-f0-9]{6})](.*?)(?=\\[|&|$)");

    public static String apply(String text) {
        text = applyGradients(text);
        text = applyHexColors(text);
        return applyColorCodes(text);
    }

    private static String applyGradients(final String text) {
        final Matcher gradientMatcher = GRADIENT_PATTERN.matcher(text);
        final StringBuffer result = new StringBuffer();

        while (gradientMatcher.find()) {
            final String startColor = gradientMatcher.group(1);
            final String endColor = gradientMatcher.group(2);
            final String gradientText = gradientMatcher.group(3);

            final String replacement = applyGradient(startColor, endColor, gradientText);
            gradientMatcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        gradientMatcher.appendTail(result);
        return result.toString();
    }

    private static String applyHexColors(final String text) {
        final Matcher hexMatcher = HEX_PATTERN.matcher(text);
        final StringBuffer result = new StringBuffer();

        while (hexMatcher.find()) {
            final String hexColor = hexMatcher.group(1);
            final String hexText = hexMatcher.group(2);
            final String replacement = ChatColor.of("#" + hexColor).toString() + hexText;
            hexMatcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        hexMatcher.appendTail(result);
        return result.toString();
    }

    private static String applyColorCodes(final String text) {
        final Matcher colorCodeMatcher = COLOR_CODE_PATTERN.matcher(text);
        final StringBuffer result = new StringBuffer();

        while (colorCodeMatcher.find()) {
            final char colorCode = colorCodeMatcher.group(1).charAt(0);
            final String replacement = ChatColor.getByChar(colorCode).toString();
            colorCodeMatcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        colorCodeMatcher.appendTail(result);
        return result.toString();
    }

    public static String hex(final String color) {
        if (color.startsWith("#") && color.length() == 7) {
            return ChatColor.of(color).toString();
        }
        return color;
    }

    public static String gradient(final String startColor, final String endColor, final String text) {
        return applyGradient(startColor, endColor, text);
    }

    private static String applyGradient(final String startColor, final String endColor, final String text) {
        final int length = text.length();
        final StringBuilder result = new StringBuilder();
        final int r1 = Integer.parseInt(startColor.substring(0, 2), 16);
        final int g1 = Integer.parseInt(startColor.substring(2, 4), 16);
        final int b1 = Integer.parseInt(startColor.substring(4, 6), 16);

        final int r2 = Integer.parseInt(endColor.substring(0, 2), 16);
        final int g2 = Integer.parseInt(endColor.substring(2, 4), 16);
        final int b2 = Integer.parseInt(endColor.substring(4, 6), 16);

        for (int i = 0; i < length; i++) {
            final double ratio = (double) i / (length - 1);
            final int red = (int) (r1 + ratio * (r2 - r1));
            final int green = (int) (g1 + ratio * (g2 - g1));
            final int blue = (int) (b1 + ratio * (b2 - b1));

            final String hexColor = String.format("#%02X%02X%02X", red, green, blue);
            result.append(ChatColor.of(hexColor)).append(text.charAt(i));
        }

        return result.toString();
    }

    public String unapply(final String text) {
        return ChatColor.stripColor(text);
    }
}
