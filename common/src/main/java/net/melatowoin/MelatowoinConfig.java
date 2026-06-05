package net.melatowoin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.architectury.platform.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Client-side config for Create: MelatOwOin. Saved to
 * {@code <gameDir>/config/melatowoin.json}.
 *
 * <p>Currently exposes one knob: {@link #fullSetSoundReduction}, controlling how
 * much the player's own sounds (footsteps, eating, splashing, etc.) are muffled
 * while wearing the full cat outfit. {@code 1.0} = total silence,
 * {@code 0.0} = no muffling. Default is {@code 1.0}.
 */
public class MelatowoinConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static double fullSetSoundReduction = 0.8;
    private static boolean loaded = false;

    public static double getFullSetSoundReduction() {
        if (!loaded) load();
        return fullSetSoundReduction;
    }

    public static void setFullSetSoundReduction(double value) {
        fullSetSoundReduction = Math.max(0.0, Math.min(1.0, value));
        save();
    }

    public static Path file() {
        return Platform.getConfigFolder().resolve("melatowoin.json");
    }

    public static synchronized void load() {
        loaded = true;
        Path p = file();
        if (!Files.exists(p)) {
            save();
            return;
        }
        try {
            String json = Files.readString(p);
            JsonObject root = GSON.fromJson(json, JsonObject.class);
            if (root != null && root.has("fullSetSoundReduction")) {
                fullSetSoundReduction = Math.max(0.0, Math.min(1.0,
                        root.get("fullSetSoundReduction").getAsDouble()));
            }
        } catch (IOException | JsonSyntaxException e) {
            MelatowoinMod.LOGGER.warn("Failed to read melatowoin.json, using defaults", e);
        }
    }

    public static synchronized void save() {
        Path p = file();
        try {
            Files.createDirectories(p.getParent());
            JsonObject root = new JsonObject();
            root.addProperty("fullSetSoundReduction", fullSetSoundReduction);
            Files.writeString(p, GSON.toJson(root));
        } catch (IOException e) {
            MelatowoinMod.LOGGER.warn("Failed to write melatowoin.json", e);
        }
    }
}
