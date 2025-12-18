package fr.thesmyler.terramap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.thesmyler.terramap.TerramapVersion.InvalidVersionString;
import fr.thesmyler.terramap.TerramapVersion.ReleaseType;
import fr.thesmyler.terramap.maps.raster.MapStylesLibrary;
import fr.thesmyler.terramap.permissions.PermissionManager;

import java.io.File;

/**
 * Main Terramap mod initializer for Fabric
 * This class handles common (both client and server) initialization
 */
public class TerramapMod implements ModInitializer {

    public static final String MODID = "terramap";
    public static final String AUTHOR_EMAIL = "smyler at mail dot com";
    public static final String STYLE_UPDATE_HOSTNAME = "styles.terramap.thesmyler.fr";
    private static TerramapVersion version;

    // These are notable versions
    public static final TerramapVersion OLDEST_COMPATIBLE_CLIENT = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 0);
    public static final TerramapVersion OLDEST_COMPATIBLE_SERVER = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 0);
    public static final TerramapVersion OLDEST_TERRA121_TERRAMAP_VERSION = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 7);

    public static final Logger logger = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        logger.info("Terramap common initialization starting");

        // Initialize version
        initializeVersion();

        // Initialize configuration
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File mapStyleFile = new File(configDir, "terramap_user_styles.json");
        MapStylesLibrary.setConfigMapFile(mapStyleFile);

        // Register permissions
        PermissionManager.registerNodes();

        // Load map styles
        MapStylesLibrary.loadFromConfigFile();

        // Register server lifecycle events
        registerServerEvents();

        // TODO: Register common event handlers (need to convert from Forge event system)
        // TODO: Initialize networking (need to convert from Forge SimpleNetworkWrapper)

        logger.info("Terramap common initialization complete - version: {}", getVersion());
    }

    private void initializeVersion() {
        // Try to read version from mod metadata
        String versionStr = FabricLoader.getInstance()
                .getModContainer(MODID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("${version}");

        if (System.getProperties().containsKey("terramap.debug")) {
            logger.info("Debug flag is set, forcing a development version string.");
            versionStr = "${version}";
        }

        try {
            TerramapMod.version = new TerramapVersion(versionStr);
        } catch (InvalidVersionString e) {
            logger.error("Failed to parse Terramap version number from string {}, will be assuming a 1.0.0 release.", versionStr);
            TerramapMod.version = new TerramapVersion(1, 0, 0);
        }
    }

    private void registerServerEvents() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            logger.debug("Server starting event");
            // TODO: Initialize server-side functionality
            // TODO: Register commands
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            logger.debug("Server started event");
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            logger.debug("Server stopping event");
        });
    }

    public static TerramapVersion getVersion() {
        try {
            return TerramapMod.version != null ? TerramapMod.version : new TerramapVersion("0.0.0");
        } catch (InvalidVersionString e) {
            throw new IllegalStateException("Version 0.0.0 should not be invalid");
        }
    }

    /**
     * Check if we're running on the client
     */
    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT;
    }

    /**
     * Check if we're running on a dedicated server
     */
    public static boolean isDedicatedServer() {
        return FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.SERVER;
    }
}
