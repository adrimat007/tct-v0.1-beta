package bspkrs.util;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.World;

public final class CommonUtils
{
    public static boolean isItemInList(int var0, int var1, String var2)
    {
        if (var2.trim().length() != 0)
        {
            String[] var3 = var2.split(";");
            String[] var4 = var3;
            int var5 = var3.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                String var7 = var4[var6];
                String[] var8 = var7.split(",");

                if (parseInt(var8[0]) == var0)
                {
                    if (var8.length == 1)
                    {
                        return true;
                    }

                    if (var8.length == 2 && parseInt(var8[1]) == var1)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isIDInList(int var0, int var1, String var2)
    {
        String[] var3 = var2.split(";");

        for (int var4 = 0; var4 < var3.length; ++var4)
        {
            String[] var5 = var3[var4].split(",");
            int var6 = parseInt(var5[0]);

            if (var5.length > 1 && parseInt(var5[1]) == var1)
            {
                return true;
            }

            if (var6 == var0)
            {
                return true;
            }
        }

        return false;
    }

    public static int sqr(int var0)
    {
        return var0 * var0;
    }

    public static float sqr(float var0)
    {
        return var0 * var0;
    }

    public static int parseInt(String var0)
    {
        try
        {
            return Integer.parseInt(var0.trim());
        }
        catch (NumberFormatException var2)
        {
            return 0;
        }
    }

    public static int getHighestGroundBlock(World var0, int var1, int var2, int var3)
    {
        while (var2 > 0 && var0.isAirBlock(var1, var2, var3) || !var0.isBlockNormalCube(var1, var2, var3) || var0.getBlockId(var1, var2, var3) == Block.wood.blockID)
        {
            --var2;
        }

        return var2;
    }

    public static int getFirstNonAirBlockFromTop(World var0, int var1, int var2)
    {
        int var3;

        for (var3 = 255; var0.isAirBlock(var1, var3 - 1, var2) && var3 > 0; --var3)
        {
            ;
        }

        return var3;
    }

    public static double getDistanceRatioToCenter(int var0, int var1, int var2)
    {
        double var3 = (double)Math.abs(var1 - var0) / 2.0D;
        double var5 = Math.abs((double)Math.abs(var2 - var0) - var3);
        return var3 != 0.0D ? var5 / var3 : 0.0D;
    }

    public static void setHugeMushroom(World var0, Random var1, int var2, int var3, int var4, int var5)
    {
        byte var6 = 3;
        byte var7 = 4;
        byte var8 = 4;
        fillWithBlocks(var0, var2 - var6 + 1, var3, var4 - var6, var2 + var6 - 1, var3 + var8 - 1, var4 - var6, var5, 10);
        fillWithBlocks(var0, var2 - var6 + 1, var3, var4 + var6, var2 + var6 - 1, var3 + var8 - 1, var4 + var6, var5, 10);
        fillWithBlocks(var0, var2 - var6, var3, var4 - var6 + 1, var2 - var6, var3 + var8 - 1, var4 + var6 - 1, var5, 10);
        fillWithBlocks(var0, var2 + var6, var3, var4 - var6 + 1, var2 + var6, var3 + var8 - 1, var4 + var6 - 1, var5, 10);
        fillWithBlocksRounded(var0, var2 - var7, var3 + var8, var4 - var7, var2 + var7, var3 + var8, var4 + var7, var5, 14);
    }

    public static void fillWithBlocks(World var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8)
    {
        for (int var9 = var1; var9 <= var4; ++var9)
        {
            for (int var10 = var2; var10 <= var5; ++var10)
            {
                for (int var11 = var3; var11 <= var6; ++var11)
                {
                    var0.setBlockAndMetadata(var9, var10, var11, var7, var8);
                }
            }
        }
    }

    public static void fillWithBlocksRounded(World var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8)
    {
        for (int var9 = var1; var9 <= var4; ++var9)
        {
            for (int var10 = var2; var10 <= var5; ++var10)
            {
                for (int var11 = var3; var11 <= var6; ++var11)
                {
                    double var12 = getDistanceRatioToCenter(var1, var4, var9);
                    double var14 = getDistanceRatioToCenter(var2, var5, var10);
                    double var16 = getDistanceRatioToCenter(var3, var6, var11);

                    if (var12 * var12 + var14 * var14 + var16 * var16 <= 1.5D)
                    {
                        var0.setBlockAndMetadata(var9, var10, var11, var7, var8);
                    }
                }
            }
        }
    }

    public static String getMCTimeString(long var0, long var2)
    {
        long var4 = (long)((int)((var0 / 1000L + 6L) % 24L));
        int var6 = (int)((double)(var0 % 1000L) / 1000.0D * 60.0D);
        boolean var7 = var4 < 12L;
        var4 %= var2;
        String var8 = "";

        if (var2 == 24L)
        {
            var8 = (var4 < 10L ? "0" : "") + String.valueOf(var4);
        }
        else
        {
            var8 = String.valueOf(var4 == 0L ? 12L : var4);
        }

        String var9 = (var6 < 10 ? "0" : "") + String.valueOf(var6);
        return var8 + ":" + var9 + (var2 == 12L ? (var7 ? "AM" : "PM") : "");
    }

    public static String ticksToTimeString(long var0)
    {
        long var2 = var0 / 20L;
        long var4 = var2 / 60L;
        long var6 = var4 / 60L;
        long var8 = var6 / 24L;
        String var10 = "";

        if (var8 > 0L)
        {
            var10 = var8 + ":";
        }

        if (var6 > 0L)
        {
            var10 = var10 + (var6 % 24L < 10L && var8 > 0L ? "0" : "") + var6 % 24L + ":";
        }

        if (var4 > 0L)
        {
            var10 = var10 + (var4 % 60L < 10L && var6 > 0L ? "0" : "") + var4 % 60L + ":";
        }
        else
        {
            var10 = var10 + "0:";
        }

        var10 = var10 + (var2 % 60L < 10L ? "0" : "") + var2 % 60L;
        return var10;
    }
}
