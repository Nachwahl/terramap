package fr.thesmyler.terramap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import javax.imageio.ImageIO;

/**
 * Client-side initialization for Terramap
 * This class handles client-only functionality
 */
@Environment(EnvType.CLIENT)
public class TerramapClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TerramapMod.logger.info("Terramap client initialization starting");

        // Check WebP support
        checkWebPSupport();

        // TODO: Initialize SmyLibGui for modern Minecraft
        // SmyLibGui.init(TerramapMod.logger, SmyLibGuiContext.MODERN);

        // Register client event handlers
        registerClientEvents();

        // TODO: Register key bindings
        // KeyBindings.registerBindings();

        // TODO: Register marker controllers
        // MarkerControllerManager.registerBuiltInControllers();

        // TODO: Reload map styles
        // MapStylesLibrary.reload();

        // TODO: Register client commands
        // registerClientCommands();

        // TODO: Initialize networking client-side handlers
        // TerramapNetworkManager.registerClientHandlers();

        TerramapMod.logger.info("Terramap client initialization complete");
    }

    private void checkWebPSupport() {
        if (!ImageIO.getImageReadersBySuffix("webp").hasNext()) {
            TerramapMod.logger.warn("ImageIO does not have WebP support, triggering a plugin scan!");
            ImageIO.scanForPlugins();
            if (ImageIO.getImageReadersBySuffix("webp").hasNext()) {
                TerramapMod.logger.info("Found a WebP ImageIO reader.");
            } else {
                TerramapMod.logger.error("Could not find a WebP ImageIO reader! WebP will not be supported.");
            }
        }
    }

    private void registerClientEvents() {
        // Register client lifecycle events
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            TerramapMod.logger.debug("Client started event");
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            TerramapMod.logger.debug("Client stopping event");
        });

        // Register tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // TODO: Handle client tick events
            // This replaces ClientTerramapEventHandler tick handling
        });

        // TODO: Register rendering events
        // TODO: Register input events
        // TODO: Register world events
    }

    private void registerClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // TODO: Register client-side commands
            // Example: OpenMapCommand needs to be converted to Brigadier format
            TerramapMod.logger.debug("Registering client commands");
        });
    }
}
