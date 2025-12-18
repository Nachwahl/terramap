package fr.thesmyler.terramap;

import java.io.File;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.thesmyler.terramap.TerramapVersion.InvalidVersionString;
import fr.thesmyler.terramap.TerramapVersion.ReleaseType;
import fr.thesmyler.terramap.eventhandlers.CommonTerramapEventHandler;
import fr.thesmyler.terramap.maps.raster.MapStylesLibrary;
import fr.thesmyler.terramap.permissions.PermissionManager;

public class TerramapMod implements ModInitializer {

    public static final String MODID = "terramap";
    public static final String AUTHOR_EMAIL = "smyler at mail dot com";
    public static final String STYLE_UPDATE_HOSTNAME = "styles.terramap.thesmyler.fr";
    private static TerramapVersion version; // Read from the metadata

    // These are notable versions
    public static final TerramapVersion OLDEST_COMPATIBLE_CLIENT = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 0);
    public static final TerramapVersion OLDEST_COMPATIBLE_SERVER = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 0);
    public static final TerramapVersion OLDEST_TERRA121_TERRAMAP_VERSION = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 7);

    public static Logger logger = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
        logger.info("Terramap common initialization");
        
        // Initialize version
        String versionStr = FabricLoader.getInstance()
            .getModContainer(MODID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("0.0.0");
            
        if (System.getProperties().containsKey("terramap.debug")) {
            logger.info("Debug flag is set, forcing a development version string.");
            versionStr = "${version}";
        }
        
        try {
            TerramapMod.version = new TerramapVersion(versionStr);
        } catch(InvalidVersionString e) {
            logger.error("Failed to parse Terramap version number from string " + versionStr + ", will be assuming a 1.0.0 release.");
            TerramapMod.version = new TerramapVersion(1, 0, 0);
        }
        
        logger.info("Terramap version: " + getVersion());
        
        // Initialize map styles
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File mapStyleFile = new File(configDir, "terramap_user_styles.json");
        MapStylesLibrary.setConfigMapFile(mapStyleFile);
        
        // Register event handlers
        CommonTerramapEventHandler.register();
        
        // Register permissions
        PermissionManager.registerNodes();
        
        // Load map styles from config
        MapStylesLibrary.loadFromConfigFile();
    }

    public static TerramapVersion getVersion() {
        try {
            return TerramapMod.version != null ? TerramapMod.version: new TerramapVersion("0.0.0");
        } catch (InvalidVersionString e) {
            throw new IllegalStateException("Version 0.0.0 should not be invalid");
        }
    }
}
