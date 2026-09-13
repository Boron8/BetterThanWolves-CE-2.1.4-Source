package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemArmor extends Item {
   private static final int[] maxDamageArray = new int[]{16, 16, 16, 16};
   private static final String[] field_94606_cu = new String[]{"helmetCloth_overlay", "chestplateCloth_overlay", "leggingsCloth_overlay", "bootsCloth_overlay"};
   public static final String[] field_94603_a = new String[]{"slot_empty_helmet", "slot_empty_chestplate", "slot_empty_leggings", "slot_empty_boots"};
   private static final IBehaviorDispenseItem field_96605_cw = new BehaviorDispenseArmor();
   public final int armorType;
   public int damageReduceAmount;
   public final int renderIndex;
   private final EnumArmorMaterial material;
   @Environment(EnvType.CLIENT)
   private Icon field_94605_cw;
   @Environment(EnvType.CLIENT)
   private Icon field_94604_cx;

   public ItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
      super(par1);
      this.material = par2EnumArmorMaterial;
      this.armorType = par4;
      this.renderIndex = par3;
      this.damageReduceAmount = par2EnumArmorMaterial.getDamageReductionAmount(par4);
      this.e(par2EnumArmorMaterial.getDurability(par4));
      this.maxStackSize = 1;
      this.a(CreativeTabs.tabCombat);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      if (par2 > 0) {
         return 16777215;
      } else {
         int var3 = this.getColor(par1ItemStack);
         if (var3 < 0) {
            var3 = 16777215;
         }

         return var3;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean requiresMultipleRenderPasses() {
      return this.material == EnumArmorMaterial.CLOTH;
   }

   @Override
   public int getItemEnchantability() {
      return this.material.getEnchantability();
   }

   public EnumArmorMaterial getArmorMaterial() {
      return this.material;
   }

   public boolean hasColor(ItemStack par1ItemStack) {
      return this.material != EnumArmorMaterial.CLOTH
         ? false
         : (
            !par1ItemStack.hasTagCompound()
               ? false
               : (!par1ItemStack.getTagCompound().hasKey("display") ? false : par1ItemStack.getTagCompound().getCompoundTag("display").hasKey("color"))
         );
   }

   public int getColor(ItemStack par1ItemStack) {
      if (this.material != EnumArmorMaterial.CLOTH) {
         return -1;
      } else {
         NBTTagCompound var2 = par1ItemStack.getTagCompound();
         if (var2 == null) {
            return ColorizeEntity.undyedLeatherColor;
         } else {
            NBTTagCompound var3 = var2.getCompoundTag("display");
            return var3 == null ? ColorizeEntity.undyedLeatherColor : (var3.hasKey("color") ? var3.getInteger("color") : ColorizeEntity.undyedLeatherColor);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamageForRenderPass(int par1, int par2) {
      return par2 == 1 ? this.field_94605_cw : super.getIconFromDamageForRenderPass(par1, par2);
   }

   public void removeColor(ItemStack par1ItemStack) {
      if (this.material == EnumArmorMaterial.CLOTH) {
         NBTTagCompound var2 = par1ItemStack.getTagCompound();
         if (var2 != null) {
            NBTTagCompound var3 = var2.getCompoundTag("display");
            if (var3.hasKey("color")) {
               var3.removeTag("color");
            }
         }
      }
   }

   public void func_82813_b(ItemStack par1ItemStack, int par2) {
      if (this.material != EnumArmorMaterial.CLOTH) {
         throw new UnsupportedOperationException("Can't dye non-leather!");
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

   @Override
   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return this.material.getArmorCraftingMaterial() == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      super.registerIcons(par1IconRegister);
      if (this.material == EnumArmorMaterial.CLOTH) {
         this.field_94605_cw = par1IconRegister.registerIcon(field_94606_cu[this.armorType]);
      }

      this.field_94604_cx = par1IconRegister.registerIcon(field_94603_a[this.armorType]);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      int var4 = EntityLiving.getArmorPosition(par1ItemStack) - 1;
      ItemStack var5 = par3EntityPlayer.getCurrentArmor(var4);
      if (var5 == null) {
         par3EntityPlayer.setCurrentItemOrArmor(var4, par1ItemStack.copy());
         par1ItemStack.stackSize = 0;
      }

      return par1ItemStack;
   }

   @Environment(EnvType.CLIENT)
   public static Icon func_94602_b(int par0) {
      switch (par0) {
         case 0:
            return Item.helmetDiamond.field_94604_cx;
         case 1:
            return Item.plateDiamond.field_94604_cx;
         case 2:
            return Item.legsDiamond.field_94604_cx;
         case 3:
            return Item.bootsDiamond.field_94604_cx;
         default:
            return null;
      }
   }

   static int[] getMaxDamageArray() {
      return maxDamageArray;
   }

   @Override
   public boolean isEnchantmentApplicable(Enchantment enchantment) {
      if (enchantment.type == EnumEnchantmentType.armor) {
         return true;
      } else if (enchantment.type == EnumEnchantmentType.armor_head) {
         return this.armorType == 0;
      } else if (enchantment.type == EnumEnchantmentType.armor_torso) {
         return this.armorType == 1;
      } else if (enchantment.type == EnumEnchantmentType.armor_legs) {
         return this.armorType == 2;
      } else {
         return enchantment.type == EnumEnchantmentType.armor_feet ? this.armorType == 3 : super.isEnchantmentApplicable(enchantment);
      }
   }
}
