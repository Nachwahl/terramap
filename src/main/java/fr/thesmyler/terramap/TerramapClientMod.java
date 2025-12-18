package fr.thesmyler.terramap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.imageio.ImageIO;

@Environment(EnvType.CLIENT)
public class TerramapClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TerramapMod.logger.info("Terramap client initialization");
        
        // Check for WebP support
        if (!ImageIO.getImageReadersBySuffix("webp").hasNext()) {
            TerramapMod.logger.warn("ImageIO does not have WebP support, triggering a plugin scan!");
            ImageIO.scanForPlugins();
            if (ImageIO.getImageReadersBySuffix("webp").hasNext()) {
                TerramapMod.logger.info("Found a WebP ImageIO reader.");
            } else {
                TerramapMod.logger.error("Could not find a WebP ImageIO reader! WebP will not be supported.");
            }
        }
        
        // TODO: Initialize client-specific components
        // - Register key bindings
        // - Register client event handlers
        // - Initialize GUI components
        // - Register marker controllers
        // - Reload map styles
        
        TerramapMod.logger.info("Terramap client initialization complete");
    }
}
