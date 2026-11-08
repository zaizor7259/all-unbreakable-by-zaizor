package net.zaizor.unbreakable;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public final class UnbreakableConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("all_unbreakable_by_zaizor.properties");
    private static final AtomicBoolean REPAIR_REQUESTED = new AtomicBoolean(true);
    private static volatile boolean enabled = true;
    private static volatile boolean unsupportedServer;

    private UnbreakableConfig() {
    }

    public static void load() {
        Properties properties = new Properties();
        if (Files.exists(FILE)) {
            try (InputStream input = Files.newInputStream(FILE)) {
                properties.load(input);
                enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));
            } catch (IOException exception) {
                AllUnbreakable.LOGGER.error("Could not read {}", FILE, exception);
            }
        } else {
            save();
        }
        REPAIR_REQUESTED.set(enabled);
    }

    public static boolean isEnabled() {
        return enabled && !unsupportedServer;
    }

    public static boolean isServerUnsupported() {
        return unsupportedServer;
    }

    public static void setServerUnsupported(boolean value) {
        unsupportedServer = value;
    }

    public static void setEnabled(boolean value) {
        if (value && unsupportedServer) {
            return;
        }
        if (enabled == value) {
            return;
        }
        enabled = value;
        save();
        if (value) {
            REPAIR_REQUESTED.set(true);
        }
    }

    public static boolean isItemProtectionEnabled() {
        return isEnabled();
    }

    public static boolean areDurabilityBarsHidden() {
        return isEnabled();
    }

    public static boolean isRepairEnabled() {
        return isEnabled();
    }

    public static boolean areAnvilsProtected() {
        return isEnabled();
    }

    public static boolean consumeRepairRequest() {
        return REPAIR_REQUESTED.getAndSet(false);
    }

    private static void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(enabled));
        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream output = Files.newOutputStream(FILE)) {
                properties.store(output, "All Unbreakable by Zaizor settings");
            }
        } catch (IOException exception) {
            AllUnbreakable.LOGGER.error("Could not write {}", FILE, exception);
        }
    }
}
