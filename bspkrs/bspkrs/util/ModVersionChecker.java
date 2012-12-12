package bspkrs.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModVersionChecker
{
    private final Logger logger;
    private URL versionURL;
    private final String modName;
    private final String newVer;
    private final String oldVer;
    private String updateURL;
    private String[] loadMsg;
    private String[] inGameMsg;

    public ModVersionChecker(String var1, String var2, String var3, String var4, String[] var5, String[] var6, Logger var7)
    {
        this.modName = var1;
        this.oldVer = var2;
        this.updateURL = var4;
        this.logger = var7;
        this.loadMsg = var5;
        this.inGameMsg = var6;

        try
        {
            this.versionURL = new URL(var3);
        }
        catch (Throwable var9)
        {
            var7.log(Level.WARNING, "Error initializing ModVersionChecker: " + var9.getMessage());
        }

        String[] var8 = this.loadTextFromURL(this.versionURL);
        this.newVer = var8[0].trim();

        if (var8.length > 1 && !var8[1].trim().equals(""))
        {
            this.updateURL = var8[1];
        }

        this.setLoadMessage(var5);
        this.setInGameMessage(var6);
    }

    public ModVersionChecker(String var1, String var2, String var3, String var4, Logger var5)
    {
        this(var1, var2, var3, var4, new String[] {"{modName} {oldVer} is out of date! Visit {updateURL} to download the latest release ({newVer})."}, new String[] {"\u00a7c{modName} {newVer} \u00a7ris out! Download the latest from \u00a7a{updateURL}"}, var5);
    }

    public void checkVersionWithLogging()
    {
        if (!this.isCurrentVersion())
        {
            String[] var1 = this.loadMsg;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                String var4 = var1[var3];
                this.logger.log(Level.INFO, var4);
            }
        }
    }

    public void setLoadMessage(String[] var1)
    {
        this.loadMsg = var1;

        for (int var2 = 0; var2 < this.loadMsg.length; ++var2)
        {
            this.loadMsg[var2] = this.replaceAllTags(this.loadMsg[var2]);
        }
    }

    public void setInGameMessage(String[] var1)
    {
        this.inGameMsg = var1;

        for (int var2 = 0; var2 < this.inGameMsg.length; ++var2)
        {
            this.inGameMsg[var2] = this.replaceAllTags(this.inGameMsg[var2]);
        }
    }

    public String[] getLoadMessage()
    {
        return this.loadMsg;
    }

    public String[] getInGameMessage()
    {
        return this.inGameMsg;
    }

    public boolean isCurrentVersion()
    {
        return this.newVer.equalsIgnoreCase(this.oldVer);
    }

    private String replaceAllTags(String var1)
    {
        return var1.replace("{oldVer}", this.oldVer).replace("{newVer}", this.newVer).replace("{modName}", this.modName).replace("{updateURL}", this.updateURL);
    }

    private String[] loadTextFromURL(URL var1)
    {
        ArrayList var2 = new ArrayList();
        Scanner var3 = null;

        try
        {
            var3 = new Scanner(var1.openStream(), "UTF-8");
        }
        catch (Throwable var5)
        {
            this.logger.log(Level.WARNING, "Error getting current version info: " + var5.getMessage());
            return new String[] {this.oldVer};
        }

        while (var3.hasNextLine())
        {
            var2.add(var3.nextLine());
        }

        var3.close();
        return (String[])((String[])var2.toArray(new String[var2.size()]));
    }
}
