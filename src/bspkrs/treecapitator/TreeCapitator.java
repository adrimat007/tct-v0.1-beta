package bspkrs.treecapitator;

import java.util.ArrayList;
import net.minecraft.server.Block;
import net.minecraft.server.BlockLog;
import net.minecraft.server.Item;

public final class TreeCapitator
{
    public static String versionNumber = "1.4.5.r03";
    public static final String allowUpdateCheckDesc = "Set to true to allow checking for mod updates, false to disable";
    public static boolean allowUpdateCheck = true;
    public static final String axeIDListDesc = "IDs of items that can chop down trees. Use \',\' to split item id from metadata and \';\' to split items.";
    public static String axeIDList = Item.WOOD_AXE.id + "; " + Item.STONE_AXE.id + "; " + Item.IRON_AXE.id + "; " + Item.GOLD_AXE.id + "; " + Item.DIAMOND_AXE.id;
    public static final String needItemDesc = "Whether you need an item from the IDList to chop down a tree. Disabling will let you chop trees with any item.";
    public static boolean needItem = true;
    public static final String onlyDestroyUpwardsDesc = "Setting this to false will allow the chopping to move downward as well as upward (and blocks below the one you break will be chopped)";
    public static boolean onlyDestroyUpwards = true;
    public static final String destroyLeavesDesc = "Enabling this will make leaves be destroyed when trees are chopped.";
    public static boolean destroyLeaves = true;
    public static final String shearLeavesDesc = "Enabling this will cause destroyed leaves to be sheared when a shearing item is in the hotbar (ignored if destroyLeaves is false).";
    public static boolean shearLeaves = false;
    public static final String shearVinesDesc = "Enabling this will shear /some/ of the vines on a tree when a shearing item is in the hotbar (ignored if destroyLeaves is false).";
    public static boolean shearVines = false;
    public static final String shearIDListDesc = "IDs of items that when placed in the hotbar will allow leaves to be sheared when shearLeaves is true. Use \',\' to split item id from metadata and \';\' to split items.";
    public static String shearIDList = Item.SHEARS.id + "";
    public static final String logHardnessNormalDesc = "The hardness of logs for when you are using items that won\'t chop down the trees.";
    public static float logHardnessNormal = 2.0F;
    public static final String logHardnessModifiedDesc = "The hardness of logs for when you are using items that can chop down trees.";
    public static float logHardnessModified = 4.0F;
    public static final String disableInCreativeDesc = "Flag to disable tree chopping in Creative mode";
    public static boolean disableInCreative = false;
    public static final String disableCreativeDropsDesc = "Flag to disable drops in Creative mode";
    public static boolean disableCreativeDrops = false;
    public static final String allowItemDamageDesc = "Enable to cause item damage based on number of blocks destroyed";
    public static boolean allowItemDamage = true;
    public static final String allowMoreBlocksThanDamageDesc = "Enable to allow chopping down the entire tree even if your item does not have enough damage remaining to cover the number of blocks.";
    public static boolean allowMoreBlocksThanDamage = false;
    public static final String sneakActionDesc = "Set sneakAction = \"disable\" to disable tree chopping while sneaking, set sneakAction = \"enable\" to only enable tree chopping while sneaking.";
    public static String sneakAction = "disable";
    public static final String maxBreakDistanceDesc = "The maximum horizontal distance that the log breaking algorithm will travel (use -1 for no limit).";
    public static int maxBreakDistance = 10;
    public static boolean isForge = false;
    public static ArrayList logClasses = new ArrayList();

    public static void init()
    {
        init(false);
    }

    public static void init(boolean var0)
    {
        isForge = var0;

        if (!isForge)
        {
            Block.byId[Block.LOG.id] = null;
            Block.byId[Block.LOG.id] = new BlockTree(Block.LOG.id);
            logClasses.add(BlockLog.class);
        }
    }
}
