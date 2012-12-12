package net.minecraft.server;

import bspkrs.treecapitator.TreeCapitator;
import bspkrs.util.ModVersionChecker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.BaseMod;
import net.minecraft.server.MLProp;
import net.minecraft.server.ModLoader;

public abstract class mod_treecapitator extends BaseMod
{
    @MLProp(
            info = "Set to true to allow checking for mod updates, false to disable"
    )
    public static boolean allowUpdateCheck = true;
    @MLProp(
            info = "IDs of items that can chop down trees. Use \',\' to split item id from metadata and \';\' to split items."
    )
    public static String axeIDList = TreeCapitator.axeIDList;
    @MLProp(
            info = "Whether you need an item from the IDList to chop down a tree. Disabling will let you chop trees with any item."
    )
    public static boolean needItem = TreeCapitator.needItem;
    @MLProp(
            info = "Setting this to false will allow the chopping to move downward as well as upward (and blocks below the one you break will be chopped)"
    )
    public static boolean onlyDestroyUpwards = TreeCapitator.onlyDestroyUpwards;
    @MLProp(
            info = "Enabling this will make leaves be destroyed when trees are chopped."
    )
    public static boolean destroyLeaves = TreeCapitator.destroyLeaves;
    @MLProp(
            info = "Enabling this will cause destroyed leaves to be sheared when a shearing item is in the hotbar (ignored if destroyLeaves is false)."
    )
    public static boolean shearLeaves = TreeCapitator.shearLeaves;
    @MLProp(
            info = "Enabling this will shear /some/ of the vines on a tree when a shearing item is in the hotbar (ignored if destroyLeaves is false)."
    )
    public static boolean shearVines = TreeCapitator.shearVines;
    @MLProp(
            info = "IDs of items that when placed in the hotbar will allow leaves to be sheared when shearLeaves is true. Use \',\' to split item id from metadata and \';\' to split items."
    )
    public static String shearIDList = TreeCapitator.shearIDList;
    @MLProp(
            info = "The hardness of logs for when you are using items that won\'t chop down the trees."
    )
    public static float logHardnessNormal = TreeCapitator.logHardnessNormal;
    @MLProp(
            info = "The hardness of logs for when you are using items that can chop down trees."
    )
    public static float logHardnessModified = TreeCapitator.logHardnessModified;
    @MLProp(
            info = "Flag to disable tree chopping in Creative mode"
    )
    public static boolean disableInCreative = TreeCapitator.disableInCreative;
    @MLProp(
            info = "Flag to disable drops in Creative mode"
    )
    public static boolean disableCreativeDrops = TreeCapitator.disableCreativeDrops;
    @MLProp(
            info = "Enable to cause item damage based on number of blocks destroyed"
    )
    public static boolean allowItemDamage = TreeCapitator.allowItemDamage;
    @MLProp(
            info = "Enable to allow chopping down the entire tree even if your item does not have enough damage remaining to cover the number of blocks."
    )
    public static boolean allowMoreBlocksThanDamage = TreeCapitator.allowMoreBlocksThanDamage;
    @MLProp(
            info = "Set sneakAction = \"disable\" to disable tree chopping while sneaking, set sneakAction = \"enable\" to only enable tree chopping while sneaking."
    )
    public static String sneakAction = TreeCapitator.sneakAction;
    @MLProp(
            info = "The maximum horizontal distance that the log breaking algorithm will travel (use -1 for no limit).\n\n**ONLY EDIT WHAT IS BELOW THIS**"
    )
    public static int maxBreakDistance = TreeCapitator.maxBreakDistance;
    private final ModVersionChecker versionChecker = new ModVersionChecker(this.getName(), this.getVersion(), "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/treeCapitator.version", "http://www.minecraftforum.net/topic/1009577-", ModLoader.getLogger());
    private final String versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/treeCapitator.version";
    private final String mcfTopic = "http://www.minecraftforum.net/topic/1009577-";

    public mod_treecapitator()
    {
        TreeCapitator.init();
        TreeCapitator.allowUpdateCheck = allowUpdateCheck;
        TreeCapitator.axeIDList = axeIDList;
        TreeCapitator.needItem = needItem;
        TreeCapitator.onlyDestroyUpwards = onlyDestroyUpwards;
        TreeCapitator.destroyLeaves = destroyLeaves;
        TreeCapitator.shearLeaves = shearLeaves;
        TreeCapitator.shearVines = shearVines;
        TreeCapitator.shearIDList = shearIDList;
        TreeCapitator.logHardnessNormal = logHardnessNormal;
        TreeCapitator.logHardnessModified = logHardnessModified;
        TreeCapitator.disableInCreative = disableInCreative;
        TreeCapitator.disableCreativeDrops = disableCreativeDrops;
        TreeCapitator.allowItemDamage = allowItemDamage;
        TreeCapitator.allowMoreBlocksThanDamage = allowMoreBlocksThanDamage;
        TreeCapitator.sneakAction = sneakAction;
        TreeCapitator.maxBreakDistance = maxBreakDistance;
    }

    public String getName()
    {
        return "TreeCapitator";
    }

    public String getVersion()
    {
        return "ML " + TreeCapitator.versionNumber;
    }

    public void load()
    {
        this.versionChecker.checkVersionWithLogging();
        ModLoader.setInGameHook(this, true, true);
    }

    public boolean onTickInGame(float var1, Minecraft var2)
    {
        if (TreeCapitator.allowUpdateCheck && !this.versionChecker.isCurrentVersion())
        {
            String[] var3 = this.versionChecker.getInGameMessage();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                String var6 = var3[var5];
                var2.thePlayer.addChatMessage(var6);
            }
        }

        return false;
    }
}
