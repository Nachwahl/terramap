package fr.thesmyler.terramap.permissions;

/**
 * Terramap permissions
 * 
 * Note: Forge's DefaultPermissionLevel doesn't exist in Fabric.
 * For Fabric, we'll use a simple boolean or integrate with a permission mod like LuckPerms.
 * For now, we'll use a simple permission level enum.
 */
public enum Permission {

    UPDATE_PLAYER_VISIBILITY_SELF(
            "terramap.commands.terrashow.self",
            PermissionLevel.ALL,
            "Lets players hide or show themselves on the map with /terrashow"),
    UPDATE_PLAYER_VISIBILITY_OTHER(
            "terramap.commands.terrashow.others",
            PermissionLevel.OP,
            "Lets players hide or show others on the map with /terrashow"),
    RELOAD_MAP_STYLES(
            "terramap.commands.reloadmapstyles",
            PermissionLevel.OP,
            "Allows server admins to reload the map server map styles without restarting"),
    RADAR_PLAYERS(
            "terramap.radar.players",
            PermissionLevel.ALL,
            "Allows players to see other players on the map"),
    RADAR_ANIMALS(
            "terramap.radar.animals",
            PermissionLevel.ALL,
            "Allows players to see animals on the map"),
    RADAR_MOBS(
            "terramap.radar.mobs",
            PermissionLevel.ALL,
            "Allows players to see mobs on the map");

    private final String node;
    private final PermissionLevel lvl;
    private final String description;

    Permission(String node, PermissionLevel lvl, String description) {
        this.node = node;
        this.lvl = lvl;
        this.description = description;
    }

    public String getNodeName() {
        return this.node;
    }

    public PermissionLevel getDefaultPermissionLevel() {
        return lvl;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Simple permission levels for Fabric
     * Can be integrated with permission mods later
     */
    public enum PermissionLevel {
        ALL,      // Everyone can use
        OP,       // Only operators
        NONE      // Nobody (disabled)
    }
}

