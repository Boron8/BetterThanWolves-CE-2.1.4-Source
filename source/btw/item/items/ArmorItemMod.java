package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public abstract class ArmorItemMod extends ArmorItem {
   public ArmorItemMod(int iItemID, EnumArmorMaterial armorMaterial, int iRenderIndex, int iArmorType, int iWeight) {
      super(iItemID, armorMaterial, iRenderIndex, iArmorType, iWeight);
   }

   @Override
   public boolean hasColor(ItemStack stack) {
      return this.hasCustomColors()
         && stack.hasTagCompound()
         && stack.getTagCompound().hasKey("display")
         && stack.getTagCompound().getCompoundTag("display").hasKey("color");
   }

   @Override
   public int getColor(ItemStack stack) {
      if (this.hasCustomColors()) {
         NBTTagCompound var2 = stack.getTagCompound();
         if (var2 != null) {
            NBTTagCompound var3 = var2.getCompoundTag("display");
            if (var3 != null && var3.hasKey("color")) {
               return var3.getInteger("color");
            }
         }

         return this.getDefaultColor();
      } else {
         return -1;
      }
   }

   @Override
   public void removeColor(ItemStack stack) {
      if (this.hasCustomColors()) {
         NBTTagCompound var2 = stack.getTagCompound();
         if (var2 != null) {
            NBTTagCompound var3 = var2.getCompoundTag("display");
            if (var3.hasKey("color")) {
               var3.removeTag("color");
            }
         }
      }
   }

   @Override
   public void func_82813_b(ItemStack par1ItemStack, int par2) {
      if (!this.hasCustomColors()) {
         throw new UnsupportedOperationException("Can't dye this shiznit fo'shnizzle!");
      } else {
         NBTTagCompound var3 = par1ItemStack.getTagCompound();
         if (var3 == null) {
            var3 = new NBTTagCompound();
            par1ItemStack.setTagCompound(var3);
         }

         NBTTagCompound var4 = var3.getCompoundTag("display");
         if (!var3.hasKey("display")) {
            var3.setCompoundTag("display", var4);
         }

         var4.setInteger("color", par2);
      }
   }

   public boolean hasCustomColors() {
      return false;
   }

   public boolean hasSecondRenderLayerWhenWorn() {
      return false;
   }

   public int getDefaultColor() {
      return 0;
   }

   public String getWornTextureDirectory() {
      return "/btwmodtex/";
   }

   public abstract String getWornTexturePrefix();

   @Environment(EnvType.CLIENT)
   @Override
   public boolean requiresMultipleRenderPasses() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamageForRenderPass(int iDamage, int iRenderPass) {
      return this.a_(iDamage);
   }
}
