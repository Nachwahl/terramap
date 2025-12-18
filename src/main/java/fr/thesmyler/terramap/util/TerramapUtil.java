package fr.thesmyler.terramap.util;

import fr.thesmyler.terramap.TerramapMod;
import net.buildtheearth.terraminusminus.projection.GeographicProjection;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import net.buildtheearth.terraminusminus.projection.ConformalEstimateProjection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/**
 * Misc stuff useful for Terramap
 * 
 * Migrated from Forge 1.12.2 to Fabric 1.21.4
 * Uses terraminusminus instead of Terra++/CubicChunks for coordinate conversion
 * 
 * @author SmylerMC
 */
public final class TerramapUtil {
    
    private TerramapUtil() {}

    // Earth circumference in meters
    public static final long EARTH_CIRCUMFERENCE = 40075017;

    // TODO: Load projection from config or world data
    // For now, we'll use a default BTE-compatible projection
    private static GeographicProjection defaultProjection = null;

    /**
     * Get the projection for a world.
     * In the original version, this was extracted from EarthGeneratorSettings.
     * With terraminusminus, we need to implement our own config/world data storage.
     * 
     * @param world The world
     * @return The geographic projection, or null if not available
     */
    public static GeographicProjection getProjectionForWorld(World world) {
        // TODO: Implement proper projection loading from:
        // 1. World-specific NBT data
        // 2. Server config
        // 3. Default to BTE projection
        
        if (defaultProjection == null) {
            // Initialize a default BTE-compatible projection
            // This is a placeholder - actual BTE projection config should be loaded
            defaultProjection = createDefaultBTEProjection();
        }
        
        return defaultProjection;
    }

    /**
     * Create a default BTE-compatible projection.
     * This replicates the BTE_DEFAULT_SETTINGS projection from Terra++.
     * 
     * @return A default projection
     */
    private static GeographicProjection createDefaultBTEProjection() {
        // TODO: Load the actual BTE projection configuration
        // For now, returning a basic conformal projection as placeholder
        // The actual implementation should parse JSON config similar to Terra++
        
        try {
            // Basic conformal projection (placeholder)
            // Real BTE uses: Conformal estimate with scale and offsets
            return new ConformalEstimateProjection();
        } catch (Exception e) {
            TerramapMod.logger.error("Failed to create default projection", e);
            return null;
        }
    }

    /**
     * Check if a world uses Earth-based coordinates.
     * In the original version, this checked for EarthWorldType and CubicChunks.
     * With datapacks and newer Minecraft, we need a different approach.
     * 
     * @param world The world to check
     * @return true if this is an Earth world
     */
    public static boolean isServerEarthWorld(World world) {
        // TODO: Implement proper Earth world detection
        // Options:
        // 1. Check world NBT data for Earth marker
        // 2. Check server config
        // 3. Check dimension type
        // 4. Use world seed or other identifiers
        
        // For now, assume overworld could be Earth world if projection is configured
        if (!(world instanceof ServerWorld)) {
            return false;
        }
        
        // Check if we're in the overworld dimension
        if (world.getRegistryKey() != World.OVERWORLD) {
            return false;
        }
        
        // Check if we have a projection configured for this world
        return getProjectionForWorld(world) != null;
    }

    /**
     * Check if a player is on an Earth world.
     * Simplified from original which checked EarthWorldType and dimension.
     * 
     * @param player The player
     * @return true if the player is on an Earth world
     */
    public static boolean isOnEarthWorld(PlayerEntity player) {
        World world = player.getWorld();
        // Only overworld can be Earth world
        if (world.getRegistryKey() != World.OVERWORLD) {
            return false;
        }
        
        return isServerEarthWorld(world);
    }

    /**
     * Check if a projection is BTE-compatible.
     * 
     * @param projection The projection to check
     * @return true if compatible with BTE
     */
    public static boolean isBteCompatible(GeographicProjection projection) {
        if (projection == null) {
            return false;
        }
        
        // TODO: Implement proper BTE compatibility check
        // This should compare projection configuration with BTE default
        
        // For now, accept any conformal projection as potentially compatible
        return true;
    }

    /**
     * Convert geographic coordinates to Minecraft coordinates.
     * 
     * @param longitude Longitude in degrees
     * @param latitude Latitude in degrees
     * @param world The world to use for projection
     * @return [x, z] coordinates, or null if conversion failed
     */
    public static double[] geoToMinecraft(double longitude, double latitude, World world) {
        GeographicProjection projection = getProjectionForWorld(world);
        if (projection == null) {
            return null;
        }
        
        try {
            return projection.fromGeo(longitude, latitude);
        } catch (OutOfProjectionBoundsException e) {
            TerramapMod.logger.debug("Coordinates out of projection bounds: {}, {}", longitude, latitude);
            return null;
        }
    }

    /**
     * Convert Minecraft coordinates to geographic coordinates.
     * 
     * @param x X coordinate in Minecraft
     * @param z Z coordinate in Minecraft
     * @param world The world to use for projection
     * @return [longitude, latitude] in degrees, or null if conversion failed
     */
    public static double[] minecraftToGeo(double x, double z, World world) {
        GeographicProjection projection = getProjectionForWorld(world);
        if (projection == null) {
            return null;
        }
        
        try {
            return projection.toGeo(x, z);
        } catch (OutOfProjectionBoundsException e) {
            TerramapMod.logger.debug("Coordinates out of projection bounds: {}, {}", x, z);
            return null;
        }
    }
}
