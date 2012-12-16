package bspkrs.fml.util;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class Config
{
    public static String getString(Configuration var0, String var1, String var2, String var3, String var4)
    {
        Property var5 = var0.get(var2, var1, var3);
        var5.comment = var4 + " [default: " + var3 + "]";
        return var5.value;
    }

    public static boolean getBoolean(Configuration var0, String var1, String var2, boolean var3, String var4)
    {
        Property var5 = var0.get(var2, var1, var3);
        var5.comment = var4 + " [default: " + var3 + "]";
        return var5.getBoolean(var3);
    }

    public static int getInt(Configuration var0, String var1, String var2, int var3, int var4, int var5, String var6)
    {
        Property var7 = var0.get(var2, var1, var3);
        var7.comment = var6 + " [range: " + var4 + " ~ " + var5 + ", default: " + var3 + "]";
        return var7.getInt(var3) < var4 ? var4 : (var7.getInt(var3) > var5 ? var5 : var7.getInt(var3));
    }

    public static float getFloat(Configuration var0, String var1, String var2, float var3, float var4, float var5, String var6)
    {
        Property var7 = var0.get(var2, var1, Float.toString(var3));
        var7.comment = var6 + " [range: " + var4 + " ~ " + var5 + ", default: " + var3 + "]";

        try
        {
            return Float.parseFloat(var7.value) < var4 ? var4 : (Float.parseFloat(var7.value) > var5 ? var5 : Float.parseFloat(var7.value));
        }
        catch (Exception var9)
        {
            var9.printStackTrace();
            return var3;
        }
    }

    public static int getBlockId(Configuration var0, String var1, int var2, String var3)
    {
        Property var4 = var0.getBlock(var1, var2);
        var4.comment = var3 + " [default: " + var2 + "]";
        return var4.getInt(var2);
    }
}
