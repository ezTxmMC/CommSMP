package de.eztxm.smp.listener.filter;

import java.util.*;
import java.util.regex.Pattern;

public class Blacklist {

    private static final Map<FilterCategory, List<Pattern>> filters = new HashMap<>();

    static {
        for (FilterCategory category : FilterCategory.values()) {
            filters.put(category, new ArrayList<>());
        }

        add(FilterCategory.HATE, "idiot", "depp", "arsch", "bastard");
        add(FilterCategory.SEXUAL, "nackt", "porno", "sex", "fetisch");
        add(FilterCategory.HATE, "hasse dich", "du bist nix wert");
        add(FilterCategory.EXTREME_RIGHTWING, "hitler", "nazi", "heil", "sieg", "14/88");
    }

    public static void add(FilterCategory category, String... keywords) {
        for (String keyword : keywords) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword.toLowerCase()) + "\\b", Pattern.CASE_INSENSITIVE);
            filters.get(category).add(pattern);
        }
    }

    public static EnumSet<FilterCategory> checkMessage(String message) {
        EnumSet<FilterCategory> matched = EnumSet.noneOf(FilterCategory.class);
        String cleaned = message.toLowerCase();

        for (Map.Entry<FilterCategory, List<Pattern>> entry : filters.entrySet()) {
            for (Pattern pattern : entry.getValue()) {
                if (pattern.matcher(cleaned).find()) {
                    matched.add(entry.getKey());
                    break;
                }
            }
        }

        return matched;
    }
}

