package bspkrs.treecapitator.fml.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import java.util.Map;

@TransformerExclusions( {"bspkrs.treecapitator.fml.asm", "bspkrs.util", "bspkrs.treecapitator", "bspkrs.treecapitator.fml", "bspkrs.fml.util"})
public class TreeCapitatorCorePlugin implements IFMLLoadingPlugin
{
    public String[] getLibraryRequestClass()
    {
        return null;
    }

    public String[] getASMTransformerClass()
    {
        return new String[] {"bspkrs.treecapitator.fml.asm.ItemInWorldManagerTransformer"};
    }

    public String getModContainerClass()
    {
        return null;
    }

    public String getSetupClass()
    {
        return null;
    }

    public void injectData(Map var1) {}
}
