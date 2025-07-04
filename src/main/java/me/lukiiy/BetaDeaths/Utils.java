package me.lukiiy.BetaDeaths;

import org.bukkit.entity.Entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String getEntityName(Entity e) {
        return e.getClass().getSimpleName().replace("Craft", "");
    }

    public static int getMCVersion(String version) {
        Matcher matcher = Pattern.compile("\\(MC: (\\d)\\.(\\d)\\.(\\d)\\)").matcher(version);
        if (matcher.find()) {
            String patch = matcher.group(3) != null ? matcher.group(3) : "0";

            return Integer.parseInt(matcher.group(1) + matcher.group(2) + patch);
        }
        return 0;
    }
}