package bspkrs.treecapitator.fml;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.TickRegistry;
import java.util.EnumSet;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    public void registerTickHandler()
    {
    }
}
