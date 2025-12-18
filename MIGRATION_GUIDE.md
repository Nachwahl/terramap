# Terramap Port from Forge 1.12.2 to Fabric 1.21.4 - Migration Guide

This document outlines the comprehensive migration required to port Terramap from Forge 1.12.2 to Fabric 1.21.4.

## Completed Infrastructure Changes

### Build System ✅
- Replaced ForgeGradle with Fabric Loom 1.9
- Updated to Minecraft 1.21.4, Yarn mappings, Fabric Loader 0.16.9
- Added Fabric API 0.111.0+1.21.4
- Integrated terraminusminus for coordinate projection (replacing Terra++/CubicChunks)
- Updated Java version requirement to 21
- Modernized dependency versions (TwelveMonkeys 3.11.0)

### Metadata ✅
- Created fabric.mod.json replacing mcmod.info
- Updated pack.mcmeta to format 34 (1.21.4)
- Configured mod entry points for common and client

## Major Code Migration Required

### 1. Main Mod Entry Point
**Files affected:** `TerramapMod.java`, `TerramapClientMod.java` (new)

**Changes needed:**
```java
// OLD (Forge):
@Mod(modid="terramap", ...)
public class TerramapMod {
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) { ... }
}

// NEW (Fabric):
public class TerramapMod implements ModInitializer {
    @Override
    public void onInitialize() { ... }
}

public class TerramapClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() { ... }
}
```

### 2. Remove Proxy System
**Files to delete:** `TerramapProxy.java`, `TerramapClientProxy.java`, `TerramapServerProxy.java`

**Replacement:** Use Fabric's entrypoints directly:
- Common code: `ModInitializer`
- Client code: `ClientModInitializer`
- Server code: `DedicatedServerModInitializer`

### 3. Event System Migration
**Files affected:** All event handlers in `eventhandlers/` package

**Changes needed:**
```java
// OLD (Forge):
@SubscribeEvent
public void onPlayerTick(TickEvent.PlayerTickEvent event) { ... }

// NEW (Fabric):
ServerTickEvents.START_SERVER_TICK.register(server -> { ... });
ServerTickEvents.END_SERVER_TICK.register(server -> { ... });
PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> { ... });
```

Key Fabric event APIs:
- `net.fabricmc.fabric.api.event.lifecycle.*`
- `net.fabricmc.fabric.api.event.player.*`  
- `net.fabricmc.fabric.api.client.event.*`

### 4. Networking System Rewrite
**Files affected:** All files in `network/` package (~20 files)

**Changes needed:**
```java
// OLD (Forge):
SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("terramap");
network.registerMessage(PacketHandler.class, MyPacket.class, 0, Side.CLIENT);

// NEW (Fabric):
ServerPlayNetworking.registerGlobalReceiver(MY_PACKET_ID, (server, player, handler, buf, responseSender) -> {
    // Handle packet
});

ClientPlayNetworking.registerGlobalReceiver(MY_PACKET_ID, (client, handler, buf, responseSender) -> {
    // Handle packet
});
```

**Key changes:**
- Replace `IMessage` and `IMessageHandler` with Fabric's packet system
- Use `PacketByteBuf` instead of Forge's `ByteBuf` wrapper
- Channel identifiers are now `Identifier` objects
- Packet handling is callback-based, not class-based

### 5. Configuration System
**Files affected:** `TerramapConfig.java`

**Changes needed:**
```java
// OLD (Forge):
@Config(modid = "terramap")
public class TerramapConfig {
    @Config.Comment("...")
    public static boolean someSetting = true;
}

// NEW (Fabric):
// Use a config library like:
// - Cloth Config API
// - Auto Config
// - Or implement custom JSON/TOML config
```

### 6. GUI/Rendering System Modernization
**Files affected:** All files in `gui/` package (~50+ files)

**Critical API changes:**
```java
// OLD (1.12.2):
public class MyScreen extends GuiScreen {
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        // OpenGL 1.x calls
    }
}

// NEW (1.21.4):
public class MyScreen extends Screen {
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Modern rendering with DrawContext
        context.drawTexture(...);
    }
}
```

**Major changes:**
- `GuiScreen` → `Screen`
- `drawScreen()` → `render(DrawContext, ...)`
- `GlStateManager` → `RenderSystem` (many methods changed)
- Font rendering completely different
- `TextComponent` system replaced with modern `Text` API
- Button/Widget system completely redesigned

### 7. Command System Migration
**Files affected:** All files in `command/` package

**Changes needed:**
```java
// OLD (Forge 1.12.2):
public class MyCommand extends CommandBase {
    public String getName() { return "mycommand"; }
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) { ... }
}

// NEW (1.21.4 Brigadier):
CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
    dispatcher.register(literal("mycommand")
        .executes(context -> {
            // Command logic
            return 1;
        }));
});
```

### 8. World/Coordinate System Changes
**Files affected:** `util/TerramapUtil.java`, player tracking, map layers

**Integration with terraminusminus:**
```java
// OLD (Terra++/CubicChunks):
import net.buildtheearth.terraplusplus.generator.EarthGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.api.world.ICubeProvider;

EarthGeneratorSettings settings = getEarthGeneratorSettingsFromWorld(world);
GeographicProjection projection = settings.projection();

// NEW (terraminusminus):
import net.buildtheearth.terraminusminus.projection.GeographicProjection;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;

// Need to implement own world settings storage/config
// terraminusminus provides projection API but not world integration
GeographicProjection projection = loadProjectionFromConfig();
double[] coords = projection.fromGeo(longitude, latitude);
```

**Cubic Chunks removal:**
- Remove all `ICubeProvider` checks
- Use standard Minecraft world height (now supports -64 to 319+ via datapacks)
- Update dimension checking

### 9. Entity/Player Tracking
**Files affected:** `network/playersync/` package

**Changes needed:**
- Update to 1.21.4 entity APIs
- `EntityPlayer` → `PlayerEntity`
- `EntityPlayerMP` → `ServerPlayerEntity`
- UUID handling modernized
- Gamemode API changed

### 10. Resource/Asset Migration
**Files affected:** Language files, textures

**Changes:**
- `.lang` files → `.json` format
- Old format: `key.translation=Value`
- New format: `{"key.translation": "Value"}`
- Texture paths remain compatible

## Critical API Mapping Reference

### Package Renames
- `net.minecraft.entity.player.EntityPlayer` → `net.minecraft.entity.player.PlayerEntity`
- `net.minecraft.entity.player.EntityPlayerMP` → `net.minecraft.server.network.ServerPlayerEntity`
- `net.minecraft.world.World` → `net.minecraft.world.World` (API changed significantly)
- `net.minecraft.util.text.*` → `net.minecraft.text.*`

### Common Method Renames
- `world.provider.getDimension()` → `world.getRegistryKey()`
- `player.getGameProfile()` → `player.getGameProfile()` (same but context different)
- `world.isRemote` → `world.isClient()`

### Removed Concepts
- Sided proxy system (use entrypoints)
- `@Mod` annotation (use `ModInitializer`)
- Forge event bus (use Fabric events)
- Config annotations (use config library)

## Testing Strategy

1. **Phase 1:** Get mod loading without errors
2. **Phase 2:** Basic map display functionality
3. **Phase 3:** Player synchronization
4. **Phase 4:** Commands and config
5. **Phase 5:** All features working

## Estimated Effort

- **~200+ files** need modification
- **~10,000+ lines** of code changes
- **Complexity:** High - major version jump (1.12.2 → 1.21.4)
- **Time estimate:** 40-80 hours for experienced developer

## Key Challenges

1. **GUI System:** Complete rewrite needed for modern rendering
2. **Networking:** Different packet paradigm
3. **terraminusminus Integration:** No world integration, need custom solution
4. **API Changes:** 9 major Minecraft versions of changes
5. **Testing:** Need to verify all features still work

## Recommendations

1. Start with minimal functionality (just load mod, show basic GUI)
2. Incrementally add features
3. Write integration tests for coordinate conversion
4. Consider gradual migration if maintaining both versions
5. Document all breaking changes for users

## Next Steps

To continue this port, the developer should:

1. Convert `TerramapMod.java` to implement `ModInitializer`
2. Create `TerramapClientMod.java` implementing `ClientModInitializer`
3. Port one GUI screen as a template for others
4. Implement basic terraminusminus integration
5. Port networking for one packet type as template
6. Continue systematically through each package
