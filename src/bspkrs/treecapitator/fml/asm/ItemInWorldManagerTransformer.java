package bspkrs.treecapitator.fml.asm;

import cpw.mods.fml.relauncher.IClassTransformer;
import java.util.HashMap;
import java.util.Iterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ItemInWorldManagerTransformer implements IClassTransformer
{
    private final String targetMethodDesc = "(III)Z";
    private final HashMap obfStrings = new HashMap();
    private final HashMap mcpStrings;

    public ItemInWorldManagerTransformer()
    {
        this.obfStrings.put("className", "ir");
        this.obfStrings.put("javaClassName", "ir");
        this.obfStrings.put("targetMethodName", "d");
        this.obfStrings.put("worldFieldName", "a");
        this.obfStrings.put("entityPlayerFieldName", "b");
        this.obfStrings.put("worldJavaClassName", "xv");
        this.obfStrings.put("getBlockMetadataMethodName", "h");
        this.obfStrings.put("blockJavaClassName", "amj");
        this.obfStrings.put("blocksListFieldName", "p");
        this.obfStrings.put("entityPlayerJavaClassName", "qx");
        this.obfStrings.put("entityPlayerMPJavaClassName", "iq");
        this.mcpStrings = new HashMap();
        this.mcpStrings.put("className", "net.minecraft.src.ItemInWorldManager");
        this.mcpStrings.put("javaClassName", "net/minecraft/src/ItemInWorldManager");
        this.mcpStrings.put("targetMethodName", "removeBlock");
        this.mcpStrings.put("worldFieldName", "theWorld");
        this.mcpStrings.put("entityPlayerFieldName", "thisPlayerMP");
        this.mcpStrings.put("worldJavaClassName", "net/minecraft/src/World");
        this.mcpStrings.put("getBlockMetadataMethodName", "getBlockMetadata");
        this.mcpStrings.put("blockJavaClassName", "net/minecraft/src/Block");
        this.mcpStrings.put("blocksListFieldName", "blocksList");
        this.mcpStrings.put("entityPlayerJavaClassName", "net/minecraft/src/EntityPlayer");
        this.mcpStrings.put("entityPlayerMPJavaClassName", "net/minecraft/src/EntityPlayerMP");
    }

    public byte[] transform(String var1, byte[] var2)
    {
        return var1.equals(this.obfStrings.get("className")) ? this.transformItemInWorldManager(var2, this.obfStrings) : (var1.equals(this.mcpStrings.get("className")) ? this.transformItemInWorldManager(var2, this.mcpStrings) : var2);
    }

    private byte[] transformItemInWorldManager(byte[] var1, HashMap var2)
    {
        System.out.println("TreeCapitator ASM Magic Time!");
        System.out.println("Class Transformation running on " + var2.get("javaClassName") + "...");
        ClassNode var3 = new ClassNode();
        ClassReader var4 = new ClassReader(var1);
        var4.accept(var3, 0);
        Iterator var5 = var3.methods.iterator();

        while (var5.hasNext())
        {
            MethodNode var6 = (MethodNode)var5.next();

            if (var6.name.equals(var2.get("targetMethodName")) && var6.desc.equals("(III)Z"))
            {
                System.out.println("Found target method " + var6.name + var6.desc + "! Searching for landmarks...");
                int var7 = 4;
                int var8 = 5;

                for (int var9 = 0; var9 < var6.instructions.size(); ++var9)
                {
                    int var11;
                    VarInsnNode var12;

                    if (var6.instructions.get(var9).getType() == 4)
                    {
                        FieldInsnNode var10 = (FieldInsnNode)var6.instructions.get(var9);

                        if (var10.owner.equals(var2.get("blockJavaClassName")) && var10.name.equals(var2.get("blocksListFieldName")))
                        {
                            for (var11 = 1; var6.instructions.get(var9 + var11).getOpcode() != 58; ++var11)
                            {
                                ;
                            }

                            System.out.println("Found Block object ASTORE Node at " + (var9 + var11));
                            var12 = (VarInsnNode)var6.instructions.get(var9 + var11);
                            var7 = var12.var;
                            System.out.println("Block object is in local object " + var7);
                        }
                    }

                    if (var6.instructions.get(var9).getType() == 5)
                    {
                        MethodInsnNode var14 = (MethodInsnNode)var6.instructions.get(var9);

                        if (var14.owner.equals(var2.get("worldJavaClassName")) && var14.name.equals(var2.get("getBlockMetadataMethodName")))
                        {
                            for (var11 = 1; var6.instructions.get(var9 + var11).getOpcode() != 54; ++var11)
                            {
                                ;
                            }

                            System.out.println("Found metadata local variable ISTORE Node at " + (var9 + var11));
                            var12 = (VarInsnNode)var6.instructions.get(var9 + var11);
                            var8 = var12.var;
                            System.out.println("Metadata is in local variable " + var8);
                        }
                    }

                    if (var6.instructions.get(var9).getOpcode() == 198)
                    {
                        System.out.println("Found IFNULL Node at " + var9);
                        int var15;

                        for (var15 = 1; var6.instructions.get(var9 + var15).getOpcode() != 25; ++var15)
                        {
                            ;
                        }

                        System.out.println("Found ALOAD Node at offset " + var15 + " from IFNULL Node");
                        System.out.println("Patching method " + (String)var2.get("javaClassName") + "/" + var6.name + var6.desc + "...");
                        LabelNode var16 = new LabelNode(new Label());
                        InsnList var17 = new InsnList();
                        var17.add(new FieldInsnNode(178, "bspkrs/treecapitator/fml/TreeCapitatorMod", "instance", "Lbspkrs/treecapitator/fml/TreeCapitatorMod;"));
                        var17.add(new VarInsnNode(25, 0));
                        var17.add(new FieldInsnNode(180, (String)var2.get("javaClassName"), (String)var2.get("worldFieldName"), "L" + (String)var2.get("worldJavaClassName") + ";"));
                        var17.add(new VarInsnNode(21, 1));
                        var17.add(new VarInsnNode(21, 2));
                        var17.add(new VarInsnNode(21, 3));
                        var17.add(new VarInsnNode(25, var7));
                        var17.add(new VarInsnNode(21, var8));
                        var17.add(new VarInsnNode(25, 0));
                        var17.add(new FieldInsnNode(180, (String)var2.get("javaClassName"), (String)var2.get("entityPlayerFieldName"), "L" + (String)var2.get("entityPlayerMPJavaClassName") + ";"));
                        var17.add(new MethodInsnNode(182, "bspkrs/treecapitator/fml/TreeCapitatorMod", "onBlockHarvested", "(L" + (String)var2.get("worldJavaClassName") + ";IIIL" + (String)var2.get("blockJavaClassName") + ";IL" + (String)var2.get("entityPlayerJavaClassName") + ";)V"));
                        var17.add(var16);
                        var6.instructions.insertBefore(var6.instructions.get(var9 + var15), var17);
                        System.out.println("Method " + (String)var2.get("javaClassName") + "/" + var6.name + var6.desc + " patched at index " + (var9 + var15 - 1));
                        System.out.println("TreeCapitator ASM Patching Complete!");
                        break;
                    }
                }
            }
        }

        ClassWriter var13 = new ClassWriter(1);
        var3.accept(var13);
        return var13.toByteArray();
    }
}
