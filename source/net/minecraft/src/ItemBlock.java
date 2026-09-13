package net.minecraft.src;

import btw.item.items.PlaceAsBlockItem;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemBlock extends PlaceAsBlockItem {
   @Environment(EnvType.CLIENT)
   private Icon field_94588_b;
   private boolean hasOldNamePrefix = false;

   public ItemBlock(int par1) {
      super(par1);
      this.blockID = par1 + 256;
   }

   public int getBlockID() {
      return this.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getSpriteNumber() {
      return Block.blocksList[this.blockID].getItemIconName() != null ? 1 : 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int par1) {
      return this.field_94588_b != null ? this.field_94588_b : Block.blocksList[this.blockID].getBlockTextureFromSide(1);
   }

   @Override
   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return Block.blocksList[this.blockID].getUnlocalizedName();
   }

   @Override
   public String getUnlocalizedName() {
      return Block.blocksList[this.blockID].getUnlocalizedName();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public CreativeTabs getCreativeTab() {
      return Block.blocksList[this.blockID].getCreativeTabToDisplayOn();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      Block.blocksList[this.blockID].getSubBlocks(par1, par2CreativeTabs, par3List);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      String var2 = Block.blocksList[this.blockID].getItemIconName();
      if (var2 != null) {
         this.field_94588_b = par1IconRegister.registerIcon(var2);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getColorFromItemStack(ItemStack var1, int var2) {
      Block var3 = Block.blocksList[this.blockID];
      return var3 != null ? var3.getRenderColor(var2) : super.a(var1, var2);
   }

   @Override
   public float getBuoyancy(int iItemDamage) {
      return Block.blocksList[this.blockID].getBuoyancy(iItemDamage);
   }

   @Override
   public Item setBuoyancy(float fBuoyancy) {
      Block.blocksList[this.blockID].setBuoyancy(fBuoyancy);
      return super.setBuoyancy(fBuoyancy);
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return Block.blocksList[this.blockID].getFurnaceBurnTime(iItemDamage);
   }

   @Override
   public Item setfurnaceburntime(int iBurnTime) {
      Block.blocksList[this.blockID].setFurnaceBurnTime(iBurnTime);
      return super.setfurnaceburntime(iBurnTime);
   }

   @Override
   public int getHerbivoreFoodValue(int iItemDamage) {
      return Block.blocksList[this.blockID].getHerbivoreItemFoodValue(iItemDamage);
   }

   @Override
   public Item setHerbivoreFoodValue(int iFoodValue) {
      Block.blocksList[this.blockID].setHerbivoreItemFoodValue(iFoodValue);
      return super.setHerbivoreFoodValue(iFoodValue);
   }

   @Override
   public int getChickenFoodValue(int iItemDamage) {
      return Block.blocksList[this.blockID].getChickenItemFoodValue(iItemDamage);
   }

   @Override
   public Item setChickenFoodValue(int iFoodValue) {
      Block.blocksList[this.blockID].setChickenItemFoodValue(iFoodValue);
      return super.setChickenFoodValue(iFoodValue);
   }

   @Override
   public int getPigFoodValue(int iItemDamage) {
      return Block.blocksList[this.blockID].getPigItemFoodValue(iItemDamage);
   }

   @Override
   public Item setPigFoodValue(int iFoodValue) {
      Block.blocksList[this.blockID].setPigItemFoodValue(iFoodValue);
      return super.setPigFoodValue(iFoodValue);
   }

   @Override
   public boolean isIncineratedInCrucible() {
      return Block.blocksList[this.blockID].isIncineratedInCrucible();
   }

   @Override
   public PlaceAsBlockItem setAssociatedBlockID(int iBlockID) {
      this.blockID = iBlockID;
      return super.setAssociatedBlockID(iBlockID);
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      return Block.blocksList[this.blockID].canItemPassIfFilter(filteredItem);
   }

   @Override
   public int getFilterableProperties(ItemStack stack) {
      return Block.blocksList[this.blockID].getFilterableProperties(stack);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return Block.blocksList[this.blockID].getHopperFilterIcon();
   }
}
