package net.minecraft.src;

import java.util.List;

public class ItemFireworkCharge extends Item {
   private Icon theIcon;

   public ItemFireworkCharge(int var1) {
      super(var1);
   }

   @Override
   public Icon getIconFromDamageForRenderPass(int var1, int var2) {
      return var2 > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(var1, var2);
   }

   @Override
   public int getColorFromItemStack(ItemStack var1, int var2) {
      if (var2 != 1) {
         return super.getColorFromItemStack(var1, var2);
      } else {
         NBTBase var3 = func_92108_a(var1, "Colors");
         if (var3 == null) {
            return 9079434;
         } else {
            NBTTagIntArray var4 = (NBTTagIntArray)var3;
            if (var4.intArray.length == 1) {
               return var4.intArray[0];
            } else {
               int var5 = 0;
               int var6 = 0;
               int var7 = 0;

               for (int var11 : var4.intArray) {
                  var5 += (var11 & 0xFF0000) >> 16;
                  var6 += (var11 & 0xFF00) >> 8;
                  var7 += (var11 & 0xFF) >> 0;
               }

               var5 /= var4.intArray.length;
               var6 /= var4.intArray.length;
               var7 /= var4.intArray.length;
               return var5 << 16 | var6 << 8 | var7;
            }
         }
      }
   }

   @Override
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   public static NBTBase func_92108_a(ItemStack var0, String var1) {
      if (var0.hasTagCompound()) {
         NBTTagCompound var2 = var0.getTagCompound().getCompoundTag("Explosion");
         if (var2 != null) {
            return var2.getTag(var1);
         }
      }

      return null;
   }

   @Override
   public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4) {
      if (var1.hasTagCompound()) {
         NBTTagCompound var5 = var1.getTagCompound().getCompoundTag("Explosion");
         if (var5 != null) {
            func_92107_a(var5, var3);
         }
      }
   }

   public static void func_92107_a(NBTTagCompound var0, List var1) {
      byte var2 = var0.getByte("Type");
      if (var2 >= 0 && var2 <= 4) {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.type." + var2).trim());
      } else {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
      }

      int[] var3 = var0.getIntArray("Colors");
      if (var3.length > 0) {
         boolean var4 = true;
         String var5 = "";

         for (int var9 : var3) {
            if (!var4) {
               var5 = var5 + ", ";
            }

            var4 = false;
            boolean var10 = false;

            for (int var11 = 0; var11 < 16; var11++) {
               if (var9 == ItemDye.dyeColors[var11]) {
                  var10 = true;
                  var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.dyeColorNames[var11]);
                  break;
               }
            }

            if (!var10) {
               var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
            }
         }

         var1.add(var5);
      }

      int[] var13 = var0.getIntArray("FadeColors");
      if (var13.length > 0) {
         boolean var14 = true;
         String var16 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";

         for (int var21 : var13) {
            if (!var14) {
               var16 = var16 + ", ";
            }

            var14 = false;
            boolean var22 = false;

            for (int var12 = 0; var12 < 16; var12++) {
               if (var21 == ItemDye.dyeColors[var12]) {
                  var22 = true;
                  var16 = var16 + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.dyeColorNames[var12]);
                  break;
               }
            }

            if (!var22) {
               var16 = var16 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
            }
         }

         var1.add(var16);
      }

      boolean var15 = var0.getBoolean("Trail");
      if (var15) {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
      }

      boolean var17 = var0.getBoolean("Flicker");
      if (var17) {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
      }
   }

   @Override
   public void registerIcons(IconRegister var1) {
      super.registerIcons(var1);
      this.theIcon = var1.registerIcon("fireworksCharge_overlay");
   }
}
