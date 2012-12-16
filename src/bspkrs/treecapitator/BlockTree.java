package bspkrs.treecapitator;

import net.minecraft.server.Block;
import net.minecraft.server.BlockLog;
import net.minecraft.server.BlockTransparant;
import net.minecraft.server.BlockVine;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.World;

public class BlockTree extends BlockLog
{
    private TreeBlockBreaker breaker;

    public BlockTree(int var1)
    {
        super(var1);
        this.c(TreeCapitator.logHardnessNormal);
        this.a(Block.e);
        this.b("log");
        this.r();
    }

    public void attack(World var1, int var2, int var3, int var4, EntityHuman var5)
    {
        this.breaker = new TreeBlockBreaker(var5, this.id, this.getClass(), BlockTransparant.class, BlockVine.class);
        this.breaker.onBlockClicked(var1, var2, var3, var4, var5);
    }

    public void a(World var1, int var2, int var3, int var4, int var5, EntityHuman var6)
    {
        if (this.breaker == null || !this.breaker.player.equals(var6))
        {
            this.breaker = new TreeBlockBreaker(var6, this.id, this.getClass(), BlockTransparant.class, BlockVine.class);
        }

        this.breaker.onBlockHarvested(var1, var2, var3, var4, var5, var6);
    }

    public float m(World var1, int var2, int var3, int var4)
    {
        return this.breaker != null ? this.breaker.getBlockHardness() : TreeCapitator.logHardnessNormal;
    }
}
