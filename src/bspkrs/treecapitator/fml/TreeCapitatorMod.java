package bspkrs.treecapitator.fml;

import bspkrs.fml.util.Config;
import bspkrs.treecapitator.TreeBlockBreaker;
import bspkrs.treecapitator.TreeCapitator;
import bspkrs.util.CommonUtils;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import java.util.HashMap;
import java.util.logging.Level;
import net.minecraft.server.Block;
import net.minecraft.server.BlockTransparant;
import net.minecraft.server.BlockVine;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

@Mod(
        name = "TreeCapitator",
        modid = "TreeCapitator",
        version = "Forge 1.4.5.r03",
        useMetadata = true
)
@NetworkMod(
        clientSideRequired = false,
        serverSideRequired = false
)
public class TreeCapitatorMod
{
    public static ModVersionChecker versionChecker;
    private final String versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/treeCapitatorForge.version";
    private final String mcfTopic = "http://www.minecraftforum.net/topic/1009577-";
    private HashMap leafClasses;
    private String idList = "17;";
    private static final String idListDesc = "Add the ID of log blocks (and optionally leaf blocks) that you want to be able to TreeCapitate. Format is \"<logID>[|<leafID>];\" ([] indicates optional elements). Example: 17|18; 209; 210; 211; 212; 213; 243|242;";
    public ModMetadata metadata;
    @SidedProxy(
            clientSide = "bspkrs.treecapitator.fml.ClientProxy",
            serverSide = "bspkrs.treecapitator.fml.CommonProxy"
    )
    public static CommonProxy proxy;
    @Instance("TreeCapitator")
    public static TreeCapitatorMod instance;

    @PreInit
    public void preInit(FMLPreInitializationEvent var1)
    {
        this.metadata = var1.getModMetadata();
        this.metadata.version = "Forge " + TreeCapitator.versionNumber;
        versionChecker = new ModVersionChecker(this.metadata.name, this.metadata.version, "https://dl.dropbox.com/u/20748481/Minecraft/1.4.5/treeCapitatorForge.version", "http://www.minecraftforum.net/topic/1009577-", FMLLog.getLogger());
        versionChecker.checkVersionWithLogging();
        Configuration var2 = new Configuration(var1.getSuggestedConfigurationFile());
        String var3 = "general";
        var2.load();
        TreeCapitator.allowUpdateCheck = Config.getBoolean(var2, "allowUpdateCheck", var3, TreeCapitator.allowUpdateCheck, "Set to true to allow checking for mod updates, false to disable");
        TreeCapitator.axeIDList = Config.getString(var2, "axeIDList", var3, TreeCapitator.axeIDList, "IDs of items that can chop down trees. Use \',\' to split item id from metadata and \';\' to split items.");
        TreeCapitator.needItem = Config.getBoolean(var2, "needItem", var3, TreeCapitator.needItem, "Whether you need an item from the IDList to chop down a tree. Disabling will let you chop trees with any item.");
        TreeCapitator.onlyDestroyUpwards = Config.getBoolean(var2, "onlyDestroyUpwards", var3, TreeCapitator.onlyDestroyUpwards, "Setting this to false will allow the chopping to move downward as well as upward (and blocks below the one you break will be chopped)");
        TreeCapitator.destroyLeaves = Config.getBoolean(var2, "destroyLeaves", var3, TreeCapitator.destroyLeaves, "Enabling this will make leaves be destroyed when trees are chopped.");
        TreeCapitator.shearLeaves = Config.getBoolean(var2, "shearLeaves", var3, TreeCapitator.shearLeaves, "Enabling this will cause destroyed leaves to be sheared when a shearing item is in the hotbar (ignored if destroyLeaves is false).");
        TreeCapitator.shearVines = Config.getBoolean(var2, "shearVines", var3, TreeCapitator.shearVines, "Enabling this will shear /some/ of the vines on a tree when a shearing item is in the hotbar (ignored if destroyLeaves is false).");
        TreeCapitator.shearIDList = Config.getString(var2, "shearIDList", var3, TreeCapitator.shearIDList, "IDs of items that when placed in the hotbar will allow leaves to be sheared when shearLeaves is true. Use \',\' to split item id from metadata and \';\' to split items.");
        TreeCapitator.logHardnessNormal = Config.getFloat(var2, "logHardnessNormal", var3, TreeCapitator.logHardnessNormal, 0.0F, 100.0F, "The hardness of logs for when you are using items that won\'t chop down the trees.");
        TreeCapitator.logHardnessModified = Config.getFloat(var2, "logHardnessModified", var3, TreeCapitator.logHardnessModified, 0.0F, 100.0F, "The hardness of logs for when you are using items that can chop down trees.");
        TreeCapitator.disableInCreative = Config.getBoolean(var2, "disableInCreative", var3, TreeCapitator.disableInCreative, "Flag to disable tree chopping in Creative mode");
        TreeCapitator.disableCreativeDrops = Config.getBoolean(var2, "disableCreativeDrops", var3, TreeCapitator.disableCreativeDrops, "Flag to disable drops in Creative mode");
        TreeCapitator.allowItemDamage = Config.getBoolean(var2, "allowItemDamage", var3, TreeCapitator.allowItemDamage, "Enable to cause item damage based on number of blocks destroyed");
        TreeCapitator.allowMoreBlocksThanDamage = Config.getBoolean(var2, "allowMoreBlocksThanDamage", var3, TreeCapitator.allowMoreBlocksThanDamage, "Enable to allow chopping down the entire tree even if your item does not have enough damage remaining to cover the number of blocks.");
        TreeCapitator.sneakAction = Config.getString(var2, "sneakAction", var3, TreeCapitator.sneakAction, "Set sneakAction = \"disable\" to disable tree chopping while sneaking, set sneakAction = \"enable\" to only enable tree chopping while sneaking.");
        TreeCapitator.maxBreakDistance = Config.getInt(var2, "maxBreakDistance", var3, TreeCapitator.maxBreakDistance, -1, 100, "The maximum horizontal distance that the log breaking algorithm will travel (use -1 for no limit).");
        this.idList = Config.getString(var2, "logIDList", var3, this.idList, "Add the ID of log blocks (and optionally leaf blocks) that you want to be able to TreeCapitate. Format is \"<logID>[|<leafID>];\" ([] indicates optional elements). Example: 17|18; 209; 210; 211; 212; 213; 243|242;");
        var2.save();
    }

    @Init
    public void init(FMLInitializationEvent var1)
    {
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        TreeCapitator.init(true);
        proxy.registerTickHandler();
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent var1)
    {
        this.parseBlockIDList(this.idList);
    }

    private void parseBlockIDList(String var1)
    {
        this.leafClasses = new HashMap();
        FMLLog.log(Level.INFO, "Parsing log ID list: %s", new Object[] {var1});

        if (var1.trim().length() > 0)
        {
            String[] var2 = var1.trim().split(";");
            String[] var3 = var2;
            int var4 = var2.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                String var6 = var3[var5];

                if (var6.trim().length() > 0)
                {
                    String[] var7 = var6.trim().split("\\|");
                    FMLLog.log(Level.INFO, "Found Log Block ID: %s", new Object[] {var7[0].trim()});
                    int var8 = CommonUtils.parseInt(var7[0].trim());
                    FMLLog.log(Level.INFO, "Interpretted: %s", new Object[] {Integer.valueOf(var8)});
                    int var9 = 18;

                    if (var7.length > 1)
                    {
                        FMLLog.log(Level.INFO, "Found Leaf Block ID: %s", new Object[] {var1});
                        var9 = CommonUtils.parseInt(var7[1].trim());
                        FMLLog.log(Level.INFO, "Interpretted: %s", new Object[] {Integer.valueOf(var9)});
                    }
                    else
                    {
                        FMLLog.log(Level.INFO, "Leaf Block ID not provided; using %s", new Object[] {Integer.valueOf(var9)});
                    }

                    if (var8 > 0)
                    {
                        Block var10 = Block.byId[var8];

                        if (var10 != null)
                        {
                            if (!TreeCapitator.logClasses.contains(var10.getClass()))
                            {
                                TreeCapitator.logClasses.add(var10.getClass());
                                FMLLog.log(Level.INFO, "Configured Log Block class: %s", new Object[] {var10.getClass().getName()});
                                Block var11 = Block.byId[var9];

                                if (var11 != null)
                                {
                                    if (var11 instanceof BlockTransparant)
                                    {
                                        this.leafClasses.put(var10.getClass(), BlockTransparant.class);
                                    }
                                    else
                                    {
                                        this.leafClasses.put(var10.getClass(), var11.getClass());
                                    }
                                }
                                else
                                {
                                    this.leafClasses.put(var10.getClass(), BlockTransparant.class);
                                }

                                FMLLog.log(Level.INFO, "Pairing Leaf Block class: %s", new Object[] {this.leafClasses.get(var10.getClass())});
                            }
                            else
                            {
                                FMLLog.log(Level.INFO, "Block for ID %s is already configured", new Object[] {Integer.valueOf(var8)});
                            }
                        }
                        else
                        {
                            FMLLog.log(Level.WARNING, "Block ID %s not found", new Object[] {Integer.valueOf(var8)});
                        }
                    }
                }
            }
        }
    }

    public void onBlockHarvested(World var1, int var2, int var3, int var4, Block var5, int var6, EntityHuman var7)
    {
        if (TreeCapitator.logClasses.contains(var5.getClass()))
        {
            TreeBlockBreaker var8 = new TreeBlockBreaker(var7, var5.id, var5.getClass(), (Class)this.leafClasses.get(var5.getClass()), BlockVine.class);
            var8.onBlockHarvested(var1, var2, var3, var4, var6, var7);
        }
    }

    public static boolean isItemInWorldManagerReplaced(EntityPlayer var0)
    {
        return !var0.itemInWorldManager.getClass().getSimpleName().equals(ItemInWorldManager.class.getSimpleName());
    }
}
