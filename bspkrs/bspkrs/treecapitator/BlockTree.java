package bspkrs.treecapitator;

import net.minecraft.server.Block;
import net.minecraft.src.BlockLeavesBase;
import net.minecraft.server.BlockLog;
import net.minecraft.server.BlockVine;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.World;

public class BlockTree extends BlockLog
{
    private TreeBlockBreaker breaker;

    public BlockTree(int var1)
    {
        super(var1);
        this.setHardness(TreeCapitator.logHardnessNormal);
        this.setStepSound(Block.soundWoodFootstep);
        this.setBlockName("log");
        this.setRequiresSelfNotify();
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World var1, int var2, int var3, int var4, EntityPlayer var5)
    {
        this.breaker = new TreeBlockBreaker(var5, this.blockID, this.getClass(), BlockLeavesBase.class, BlockVine.class);
        this.breaker.onBlockClicked(var1, var2, var3, var4, var5);
    }

    /**
     * Called when the block is attempted to be harvested
     */
    public void onBlockHarvested(World var1, int var2, int var3, int var4, int var5, EntityPlayer var6)
    {
        if (this.breaker == null || !this.breaker.player.equals(var6))
        {
            this.breaker = new TreeBlockBreaker(var6, this.blockID, this.getClass(), BlockLeavesBase.class, BlockVine.class);
        }

        this.breaker.onBlockHarvested(var1, var2, var3, var4, var5, var6);
    }

    /**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    public float getBlockHardness(World var1, int var2, int var3, int var4)
    {
        return this.breaker != null ? this.breaker.getBlockHardness() : TreeCapitator.logHardnessNormal;
    }
}
