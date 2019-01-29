/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icbm.classic;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.world.World;
import rpcore.api.CoreAPI;
import rpcore.api.DimensionAPI;
import rpcore.constants.ConsoleMessageType;
import rpcore.constants.ListenerBus;
import rpcore.module.dimension.Dimension;
import rpcore.module.dimension.ForgeDimension;
import rpcore.module.listener.Listener;
import cpw.mods.fml.common.eventhandler.EventPriority;
import icbm.classic.content.entity.EntityExplosion;
import icbm.classic.content.explosive.Explosives;
import icbm.classic.content.explosive.blast.BlastRedmatter;
import icbm.classic.content.explosive.tile.BlockExplosive;
import rpcore.RPCore;
import rpcore.constants.CelestialClass;
import rpcore.constants.CelestialType;
import rpcore.module.player.Player;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
/**
 *
 * @author draks
 */
public class ICBMWorldListener extends Listener {

    public ICBMWorldListener() {
        super("ICBMWorldListener", ListenerBus.Both);
    }
    
    //public static HashMap<Integer,Long> tickTimer = new HashMap<Integer,Long>();
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerLoggedInEvent e) {
        EntityPlayer player = (EntityPlayer)e.player;        
        Player p = RPCore.getPlayerRegistry().getRegistered(player.getUniqueID());
        if (p == null) {
            p = new Player(((EntityPlayer)e.player).getUniqueID());
            RPCore.getPlayerRegistry().register(p);
        }
        p = RPCore.getPlayerRegistry().getRegistered(player.getUniqueID());
        if (p.getPos().getDimension().getType().equals(CelestialType.System)) {
            for (Dimension child : p.getPos().getDimension().getChildren()) {
                if (child.getCelestialClass().equals(CelestialClass.CLASS_BLACKHOLE_STAR)) {
                    CoreAPI.sendConsoleEntry(("Creating BH at: " + child.getPosX() + "," + child.getPosZ() + " for : " + child.getIdentifier()), ConsoleMessageType.FINE);
                    BlockExplosive.triggerExplosive(((ForgeDimension)p.getPos().getDimension()).getWrappedObject(), (int)child.getPosX(), (int)1000, (int)child.getPosZ(), Explosives.REDMATTER, 0);
                }
            }
        }
    }    
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldLoad(WorldEvent.Load e) {
        World w = (World)e.world;
        ForgeDimension dimension = DimensionAPI.getForgeDimension(w.provider.dimensionId);
        if (dimension != null) {
            if (dimension.getType().equals(CelestialType.System)) {
                for (Dimension child : dimension.getChildren()) {
                    if (child.getCelestialClass().equals(CelestialClass.CLASS_BLACKHOLE_STAR)) {
                        CoreAPI.sendConsoleEntry(("Creating BH at: " + child.getPosX() + "," + child.getPosZ() + " for : " + child.getIdentifier() + " in : " + dimension.getIdentifier()), ConsoleMessageType.FINE);
                        BlockExplosive.triggerExplosive(w, (int)child.getPosX(), (int)1, (int)child.getPosZ(), Explosives.REDMATTER, 0);
                    }
                }
            }
        }
    }
}
