package bspkrs.treecapitator.fml;

import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import net.minecraft.server.Block;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PlayerHandler
{
    @ForgeSubscribe
    public void onBlockClicked(PlayerInteractEvent var1)
    {
        if (var1.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
        {
            int var2 = var1.entityPlayer.world.getTypeId(var1.x, var1.y, var1.z);

            if (var2 > 0)
            {
                Block var3 = Block.byId[var2];

                if (var3 != null && TreeCapitator.logClasses.contains(var3.getClass()))
                {
                    var3.c(TreeBlockBreaker.getBlockHardness(var1.entityPlayer));
                }
            }
        }
    }
}
