package bspkrs.util;

import net.minecraft.server.Block;
import net.minecraft.server.World;

public class BlockID
{
    public int id;
    public int metadata;

    public BlockID(int var1, int var2)
    {
        this.id = var1;
        this.metadata = var2;
    }

    public BlockID(Block var1, int var2)
    {
        this(var1.id, var2);
    }

    public BlockID(Block var1)
    {
        this(var1.id, -1);
    }

    public BlockID(int var1)
    {
        this(var1, -1);
    }

    public BlockID(World var1, int var2, int var3, int var4)
    {
        this(var1, var2, var3, var4, var1.getData(var2, var3, var4));
    }

    public BlockID(World var1, int var2, int var3, int var4, int var5)
    {
        this(var1.getTypeId(var2, var3, var4), var5);
    }

    public BlockID clone()
    {
        return new BlockID(this.id, this.metadata);
    }

    public boolean equals(Object var1)
    {
        if (this == var1)
        {
            return true;
        }
        else if (!(var1 instanceof BlockID))
        {
            return false;
        }
        else
        {
            BlockID var2 = (BlockID)var1;
            return this.id == var2.id && this.metadata == var2.metadata;
        }
    }

    public int hashCode()
    {
        byte var1 = 23;
        int var2 = HashCodeUtil.hash(var1, this.id);
        var2 = HashCodeUtil.hash(var2, this.metadata);
        return var2;
    }
}
