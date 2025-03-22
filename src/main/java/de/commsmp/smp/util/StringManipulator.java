package de.commsmp.smp.util;

public class StringManipulator {

    public static String manipulateString(String input) {
        String[] words = input.split(" ");
        StringBuilder resultBuilder = new StringBuilder();

        for (String word : words) {
            if (word.contains("_")) {
                String[] parts = word.split("_");
                for (String part : parts) {
                    resultBuilder.append(capitalizeFirstLetter(part)).append(" ");
                }
                continue;
            }
            resultBuilder.append(capitalizeFirstLetter(word)).append(" ");
        }

        return resultBuilder.toString().trim();
    }

    private static String capitalizeFirstLetter(String word) {
        if (word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

}
