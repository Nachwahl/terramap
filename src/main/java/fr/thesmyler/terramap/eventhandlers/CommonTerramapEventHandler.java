package fr.thesmyler.terramap.eventhandlers;

import fr.thesmyler.terramap.TerramapConfig;
import fr.thesmyler.terramap.saving.server.TerramapServerPreferences;
import fr.thesmyler.terramap.network.RemoteSynchronizer;
import fr.thesmyler.terramap.util.TerramapUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

// TODO: Port to Fabric event system
// Forge events need to be replaced with Fabric equivalents:
// - PlayerLoggedInEvent -> ServerPlayConnectionEvents.JOIN
// - PlayerChangedDimensionEvent -> ServerLivingEntityEvents or custom tracking
// - PlayerLoggedOutEvent -> ServerPlayConnectionEvents.DISCONNECT
// - WorldTickEvent -> ServerTickEvents.END_WORLD_TICK
// - WorldEvent.Load -> ServerWorldEvents.LOAD

public class CommonTerramapEventHandler {

    private long tickCounter = 0;

    public static void register() {
        // TODO: Register Fabric events here
        // For now, we'll create a placeholder to avoid compilation errors
    }

    // The following methods need to be adapted to Fabric's event system
    
    public void onPlayerLoggedIn(Object event){
        // TODO: Port to Fabric ServerPlayConnectionEvents.JOIN
        /*
        if(!event.player.world.isRemote) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            RemoteSynchronizer.sendHelloToClient(player);
            RemoteSynchronizer.sendTpCommandToClient(player);
        }
        */
    }

    public void onChangeDimension(Object event) {
        // TODO: Port to Fabric dimension change tracking
        /*
        if(!event.player.world.isRemote)
            RemoteSynchronizer.sendHelloToClient((EntityPlayerMP) event.player);
        */
    }

    public void onPlayerLoggedOut(Object event) {
        // TODO: Port to Fabric ServerPlayConnectionEvents.DISCONNECT
        /*
        RemoteSynchronizer.playersToUpdate.remove(event.player.getPersistentID());
        */
    }


    public void onWorldTick(Object event) {
        // TODO: Port to Fabric ServerTickEvents.END_WORLD_TICK
        /*
        if(event.phase.equals(TickEvent.Phase.END) || event.world.isRemote) return;
        WorldServer world = event.world.getMinecraftServer().worlds[0]; //event.world has no entity or players
        if(TerramapConfig.SERVER.synchronizePlayers && TerramapUtil.isServerEarthWorld(world) && this.tickCounter == 0) {
            RemoteSynchronizer.syncPlayers(world);
        }
        this.tickCounter = (this.tickCounter+1) % TerramapConfig.SERVER.syncInterval;
        */
    }

    public void onWorldLoads(Object event) {
        // TODO: Port to Fabric ServerWorldEvents.LOAD
        /*
        if(!event.getWorld().isRemote) {
            WorldServer world = ((WorldServer)event.getWorld());
            TerramapServerPreferences.loadWorldPreferences(world);
        }
        */
    }

}
