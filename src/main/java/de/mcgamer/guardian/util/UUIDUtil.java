package de.mcgamer.guardian.util;

import com.google.common.reflect.TypeToken;
import de.mcgamer.guardian.Main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class UUIDUtil {
    public static UUID getOfflinePlayer(String name) {
        String API_URL = "https://api.mojang.com/users/profiles/minecraft/";
            try {
                final URL url = new URL(API_URL + name);
                HashMap<String, String> out = Main.gson.fromJson(new Scanner(url.openConnection().getInputStream()).nextLine(), new TypeToken<HashMap<String, String>>(){}.getType());
                return getUniqueIdFromString(out.get("id"));
            } catch (IOException ignored) {
            }
        return null;
    }
    public static UUID getUniqueIdFromString(String uuid) {
        return UUID.fromString(uuid.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
