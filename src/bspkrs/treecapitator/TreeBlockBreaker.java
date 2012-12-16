package bspkrs.treecapitator;

import bspkrs.util.CommonUtils;
import bspkrs.util.Coord;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class TreeBlockBreaker
{
    public EntityHuman player;
    public boolean shouldFell;
    private Coord startPos;
    private ItemStack axe;
    private ItemStack shears;
    private final Class leafClass;
    private final Class vineClass;
    private final int logClassIndex;
    private final int logID;

    public TreeBlockBreaker(EntityHuman var1, int var2, Class var3, Class var4, Class var5)
    {
        this.player = var1;
        this.shouldFell = false;
        this.logID = var2;
        this.leafClass = var4;
        this.vineClass = var5;

        if (!TreeCapitator.logClasses.contains(var3))
        {
            TreeCapitator.logClasses.add(var3);
        }

        this.logClassIndex = TreeCapitator.logClasses.indexOf(var3);
    }

    public void onBlockClicked(World var1, int var2, int var3, int var4, EntityHuman var5)
    {
        this.shouldFell = false;
        this.player = var5;

        if ((this.isAxeItemEquipped() || !TreeCapitator.needItem) && !var1.isStatic)
        {
            this.shouldFell = true;

            if (!this.player.abilities.canInstantlyBuild && TreeCapitator.allowItemDamage && this.axe != null && this.axe.getData() == this.axe.k() && !TreeCapitator.allowMoreBlocksThanDamage)
            {
                this.shouldFell = false;
            }
        }
    }

    public static boolean isBreakingPossible(World var0, EntityHuman var1)
    {
        ItemStack var2 = var1.bT();
        return (isAxeItemEquipped(var1) || !TreeCapitator.needItem) && !var0.isStatic ? var1.abilities.canInstantlyBuild || !TreeCapitator.allowItemDamage || var2 == null || var2.getData() != var2.k() || TreeCapitator.allowMoreBlocksThanDamage : false;
    }

    public void onBlockHarvested(World var1, int var2, int var3, int var4, int var5, EntityHuman var6)
    {
        this.player = var6;
        this.startPos = new Coord(var2, var3, var4);

        if ((TreeCapitator.sneakAction.equalsIgnoreCase("enable") && this.player.isSneaking() || !TreeCapitator.sneakAction.toLowerCase().contains("enable") && !this.player.isSneaking()) && !var1.isStatic && (!this.player.abilities.canInstantlyBuild || !TreeCapitator.disableInCreative))
        {
            Coord var7 = this.getTopLog(var1, new Coord(var2, var3, var4));

            if (this.leavesAround(var1, var7) > 2 && (this.isAxeItemEquipped() || !TreeCapitator.needItem) && !var1.isStatic)
            {
                ArrayList var8 = new ArrayList();
                List var9 = this.addLogs(var1, new Coord(var2, var3, var4));
                this.addLogsAbove(var1, new Coord(var2, var3, var4), var8);
                this.destroyBlocks(var1, var9);

                if (TreeCapitator.destroyLeaves)
                {
                    Iterator var10 = var8.iterator();

                    while (var10.hasNext())
                    {
                        Coord var11 = (Coord)var10.next();
                        List var12 = this.addLeaves(var1, var11);
                        this.removeLeavesWithLogsAround(var1, var12);
                        this.destroyBlocksWithChance(var1, var12, 0.5F, this.hasShearsInHotbar(this.player));
                    }
                }

                this.shouldFell = false;
            }
        }
    }

    public float getBlockHardness()
    {
        return this.isAxeItemEquipped() ? TreeCapitator.logHardnessModified : TreeCapitator.logHardnessNormal;
    }

    public static float getBlockHardness(EntityHuman var0)
    {
        return isAxeItemEquipped(var0) ? TreeCapitator.logHardnessModified : TreeCapitator.logHardnessNormal;
    }

    public Coord getTopLog(World var1, Coord var2)
    {
        while (((Class)TreeCapitator.logClasses.get(this.logClassIndex)).isInstance(Block.byId[var1.getTypeId(var2.x, var2.y + 1, var2.z)]))
        {
            ++var2.y;
        }

        return var2;
    }

    public int leavesAround(World var1, Coord var2)
    {
        int var3 = 0;

        for (int var4 = -1; var4 <= 1; ++var4)
        {
            for (int var5 = -1; var5 <= 1; ++var5)
            {
                for (int var6 = -1; var6 <= 1; ++var6)
                {
                    if ((var4 != 0 || var5 != 0 || var6 != 0) && this.leafClass.isInstance(Block.byId[var1.getTypeId(var2.x + var4, var2.y + var5, var2.z + var6)]))
                    {
                        ++var3;
                    }
                }
            }
        }

        return var3;
    }

    private boolean isAxeItemEquipped()
    {
        ItemStack var1 = this.player.bT();

        if (var1 != null && var1.count > 0 && CommonUtils.isItemInList(var1.id, var1.getData(), TreeCapitator.axeIDList))
        {
            this.axe = var1;
            return true;
        }
        else
        {
            this.axe = null;
            return false;
        }
    }

    public static boolean isAxeItemEquipped(EntityHuman var0)
    {
        ItemStack var1 = var0.bT();
        return var1 != null && var1.count > 0 && CommonUtils.isItemInList(var1.id, var1.getData(), TreeCapitator.axeIDList);
    }

    private boolean hasShearsInHotbar(EntityHuman var1)
    {
        return this.shearsHotbarIndex(var1) != -1;
    }

    private int shearsHotbarIndex(EntityHuman var1)
    {
        for (int var2 = 0; var2 < 9; ++var2)
        {
            ItemStack var3 = var1.inventory.items[var2];

            if (var3 != null && var3.count > 0 && CommonUtils.isItemInList(var3.id, var3.getData(), TreeCapitator.shearIDList))
            {
                this.shears = var3;
                return var2;
            }
        }

        this.shears = null;
        return -1;
    }

    private void destroyBlocks(World var1, List var2)
    {
        this.destroyBlocksWithChance(var1, var2, 1.0F, false);
    }

    private void destroyBlocksWithChance(World var1, List var2, float var3, boolean var4)
    {
        while (var2.size() > 0)
        {
            Coord var5 = (Coord)var2.remove(0);
            int var6 = var1.getTypeId(var5.x, var5.y, var5.z);

            if (var6 != 0)
            {
                Block var7 = Block.byId[var6];
                int var8 = var1.getData(var5.x, var5.y, var5.z);

                if ((this.vineClass.isInstance(var7) && TreeCapitator.shearVines || this.leafClass.isInstance(var7) && TreeCapitator.shearLeaves) && var4 && (!this.player.abilities.canInstantlyBuild || !TreeCapitator.disableCreativeDrops))
                {
                    var1.addEntity(new EntityItem(var1, (double)var5.x, (double)var5.y, (double)var5.z, new ItemStack(var6, 1, var8 & 3)));

                    if (TreeCapitator.allowItemDamage && !this.player.abilities.canInstantlyBuild && this.shears != null && this.shears.count > 0)
                    {
                        var4 = this.damageShearsAndContinue(var1, var6, var5.x, var5.y, var5.z);
                    }
                }
                else if (!this.player.abilities.canInstantlyBuild || !TreeCapitator.disableCreativeDrops)
                {
                    var7.c(var1, var5.x, var5.y, var5.z, var8, 0);

                    if (TreeCapitator.allowItemDamage && !this.player.abilities.canInstantlyBuild && this.axe != null && this.axe.count > 0 && !this.vineClass.isInstance(var7) && !this.leafClass.isInstance(var7) && !var5.equals(this.startPos) && !this.damageAxeAndContinue(var1, var6, var5.x, var5.y, var5.z))
                    {
                        var2.clear();
                    }
                }

                var1.setTypeId(var5.x, var5.y, var5.z, 0);
            }
        }
    }

    private boolean damageAxeAndContinue(World var1, int var2, int var3, int var4, int var5)
    {
        if (this.axe != null)
        {
            this.axe.getItem().a(this.axe, var1, var2, var3, var4, var5, this.player);

            if (this.axe != null && this.axe.count < 1)
            {
                this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
            }
        }

        return !TreeCapitator.needItem || TreeCapitator.allowMoreBlocksThanDamage || this.isAxeItemEquipped();
    }

    private boolean damageShearsAndContinue(World var1, int var2, int var3, int var4, int var5)
    {
        if (this.shears != null)
        {
            int var6 = this.shearsHotbarIndex(this.player);

            if (TreeCapitator.isForge && this.shears.id == Item.SHEARS.id)
            {
                this.shears.damage(1, this.player);
            }
            else
            {
                this.shears.getItem().a(this.shears, var1, var2, var3, var4, var5, this.player);
            }

            if (this.shears != null && this.shears.count < 1 && var6 != -1)
            {
                this.player.inventory.items[var6] = null;
            }
        }

        return TreeCapitator.allowMoreBlocksThanDamage || this.hasShearsInHotbar(this.player);
    }

    public List addLogs(World var1, Coord var2)
    {
        int var3 = 0;
        int var4 = var2.y;
        ArrayList var8 = new ArrayList();
        var8.add(var2);

        do
        {
            Coord var10 = (Coord)var8.get(var3);

            for (int var5 = -1; var5 <= 1; ++var5)
            {
                for (int var6 = -1; var6 <= 1; ++var6)
                {
                    for (int var7 = -1; var7 <= 1; ++var7)
                    {
                        if (((Class)TreeCapitator.logClasses.get(this.logClassIndex)).isInstance(Block.byId[var1.getTypeId(var10.x + var5, var10.y + var6, var10.z + var7)]))
                        {
                            Coord var9 = new Coord(var10.x + var5, var10.y + var6, var10.z + var7);

                            if ((TreeCapitator.maxBreakDistance == -1 || Math.abs(var9.x - this.startPos.x) <= TreeCapitator.maxBreakDistance && Math.abs(var9.z - this.startPos.z) <= TreeCapitator.maxBreakDistance) && !var8.contains(var9) && (var9.y >= var4 || !TreeCapitator.onlyDestroyUpwards))
                            {
                                var8.add(var9);
                            }
                        }
                    }
                }
            }

            ++var3;
        }
        while (var3 < var8.size());

        return var8;
    }

    private void addLogsAbove(World var1, Coord var2, List var3)
    {
        ArrayList var4 = new ArrayList();
        var4.add(var2);

        while (true)
        {
            ArrayList var5 = var4;
            var4 = new ArrayList();
            Iterator var11 = var5.iterator();
            Coord var6;
            int var9;
            int var10;

            while (var11.hasNext())
            {
                Coord var12 = (Coord)var11.next();
                int var7 = 0;

                for (var9 = -1; var9 <= 1; ++var9)
                {
                    for (var10 = -1; var10 <= 1; ++var10)
                    {
                        if (((Class)TreeCapitator.logClasses.get(this.logClassIndex)).isInstance(Block.byId[var1.getTypeId(var12.x + var9, var12.y + 1, var12.z + var10)]))
                        {
                            if (!var4.contains(var6 = new Coord(var12.x + var9, var12.y + 1, var12.z + var10)))
                            {
                                var4.add(var6);
                            }

                            ++var7;
                        }
                    }
                }

                if (var7 == 0)
                {
                    var3.add(var12.clone());
                }
            }

            int var8 = -1;

            while (true)
            {
                ++var8;

                if (var8 >= var4.size())
                {
                    if (var4.size() <= 0)
                    {
                        return;
                    }

                    break;
                }

                Coord var13 = (Coord)var4.get(var8);

                for (var9 = -1; var9 <= 1; ++var9)
                {
                    for (var10 = -1; var10 <= 1; ++var10)
                    {
                        if (((Class)TreeCapitator.logClasses.get(this.logClassIndex)).isInstance(Block.byId[var1.getTypeId(var13.x + var9, var13.y, var13.z + var10)]) && !var4.contains(var6 = new Coord(var13.x + var9, var13.y, var13.z + var10)))
                        {
                            var4.add(var6);
                        }
                    }
                }
            }
        }
    }

    public List addLeaves(World var1, Coord var2)
    {
        int var3 = -1;
        ArrayList var4 = new ArrayList();
        this.addLeavesInDistance(var1, var2, 4, var4);

        while (true)
        {
            ++var3;

            if (var3 >= var4.size())
            {
                return var4;
            }

            Coord var5 = (Coord)var4.get(var3);
            this.addLeavesInDistance(var1, var5, 1, var4);
        }
    }

    public void addLeavesInDistance(World var1, Coord var2, int var3, List var4)
    {
        for (int var5 = -var3; var5 <= var3; ++var5)
        {
            for (int var6 = -var3; var6 <= var3; ++var6)
            {
                for (int var7 = -var3; var7 <= var3; ++var7)
                {
                    Block var8 = Block.byId[var1.getTypeId(var5 + var2.x, var6 + var2.y, var7 + var2.z)];

                    if (this.leafClass.isInstance(var8) || this.vineClass.isInstance(var8))
                    {
                        int var9 = var1.getData(var5 + var2.x, var6 + var2.y, var7 + var2.z);

                        if ((var9 & 8) != 0 && (var9 & 4) == 0)
                        {
                            Coord var10 = new Coord(var5 + var2.x, var6 + var2.y, var7 + var2.z);

                            if (!var4.contains(var10))
                            {
                                var4.add(var10);
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeLeavesWithLogsAround(World var1, List var2)
    {
        int var3 = 0;

        while (var3 < var2.size())
        {
            if (this.hasLogClose(var1, (Coord)var2.get(var3), 1))
            {
                var2.remove(var3);
            }
            else
            {
                ++var3;
            }
        }
    }

    public boolean hasLogClose(World var1, Coord var2, int var3)
    {
        for (int var4 = -var3; var4 <= var3; ++var4)
        {
            for (int var5 = -var3; var5 <= var3; ++var5)
            {
                for (int var6 = -var3; var6 <= var3; ++var6)
                {
                    Coord var7 = new Coord(var4 + var2.x, var5 + var2.y, var6 + var2.z);
                    int var8 = var1.getTypeId(var7.x, var7.y, var7.z);

                    if ((var4 != 0 || var5 != 0 || var6 != 0) && var8 != 0 && TreeCapitator.logClasses.contains(Block.byId[var8].getClass()) && !var7.equals(this.startPos))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
