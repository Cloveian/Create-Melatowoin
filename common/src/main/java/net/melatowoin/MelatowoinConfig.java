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
 */
public class MelatowoinConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static double fullSetSoundReduction = 0.8;
    private static boolean earsLiftUnderHelmet   = true;
    private static boolean hideHelmetWithCatEars = false;
    private static boolean hideChestplateWithPaws = false;
    private static boolean loaded = false;

    // ── fullSetSoundReduction ────────────────────────────────────────────────
    public static double getFullSetSoundReduction() {
        if (!loaded) load();
        return fullSetSoundReduction;
    }
    public static void setFullSetSoundReduction(double value) {
        fullSetSoundReduction = Math.max(0.0, Math.min(1.0, value));
        save();
    }

    // ── earsLiftUnderHelmet ──────────────────────────────────────────────────
    public static boolean getEarsLiftUnderHelmet() {
        if (!loaded) load();
        return earsLiftUnderHelmet;
    }
    public static void setEarsLiftUnderHelmet(boolean value) {
        earsLiftUnderHelmet = value;
        save();
    }

    // ── hideHelmetWithCatEars ────────────────────────────────────────────────
    public static boolean getHideHelmetWithCatEars() {
        if (!loaded) load();
        return hideHelmetWithCatEars;
    }
    public static void setHideHelmetWithCatEars(boolean value) {
        hideHelmetWithCatEars = value;
        save();
    }

    // ── hideChestplateWithPaws ───────────────────────────────────────────────
    public static boolean getHideChestplateWithPaws() {
        if (!loaded) load();
        return hideChestplateWithPaws;
    }
    public static void setHideChestplateWithPaws(boolean value) {
        hideChestplateWithPaws = value;
        save();
    }

    // ── persistence ──────────────────────────────────────────────────────────
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
            if (root == null) return;
            if (root.has("fullSetSoundReduction")) {
                fullSetSoundReduction = Math.max(0.0, Math.min(1.0,
                        root.get("fullSetSoundReduction").getAsDouble()));
            }
            if (root.has("earsLiftUnderHelmet")) {
                earsLiftUnderHelmet = root.get("earsLiftUnderHelmet").getAsBoolean();
            }
            if (root.has("hideHelmetWithCatEars")) {
                hideHelmetWithCatEars = root.get("hideHelmetWithCatEars").getAsBoolean();
            }
            if (root.has("hideChestplateWithPaws")) {
                hideChestplateWithPaws = root.get("hideChestplateWithPaws").getAsBoolean();
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
            root.addProperty("fullSetSoundReduction",   fullSetSoundReduction);
            root.addProperty("earsLiftUnderHelmet",     earsLiftUnderHelmet);
            root.addProperty("hideHelmetWithCatEars",   hideHelmetWithCatEars);
            root.addProperty("hideChestplateWithPaws",  hideChestplateWithPaws);
            Files.writeString(p, GSON.toJson(root));
        } catch (IOException e) {
            MelatowoinMod.LOGGER.warn("Failed to write melatowoin.json", e);
        }
    }

    /**
     * Pushes the three render preferences to the server so it can re-broadcast
     * them to every other client. The server stores the values keyed by the
     * sender's UUID. Safe to call whether or not we're connected — if the
     * channel isn't open the send just no-ops.
     */
    public static void sendToServer() {
        if (!loaded) load();
        try {
            net.melatowoin.network.WearerConfigC2SPacket.sendToServer(
                    earsLiftUnderHelmet, hideHelmetWithCatEars, hideChestplateWithPaws);
        } catch (Throwable t) {
            // Not connected to a server, or networking not initialised yet — ignore.
        }
    }
}
