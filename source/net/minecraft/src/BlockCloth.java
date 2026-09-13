package net.minecraft.src;

import java.util.List;

public class BlockCloth extends Block {
   private Icon[] iconArray;

   public BlockCloth() {
      super(35, Material.cloth);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return this.iconArray[var2 % this.iconArray.length];
   }

   @Override
   public int damageDropped(int var1) {
      return var1;
   }

   public static int getBlockFromDye(int var0) {
      return ~var0 & 15;
   }

   public static int getDyeFromBlock(int var0) {
      return ~var0 & 15;
   }

   @Override
   public void getSubBlocks(int var1, CreativeTabs var2, List var3) {
      for (int var4 = 0; var4 < 16; var4++) {
         var3.add(new ItemStack(var1, 1, var4));
      }
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.iconArray = new Icon[16];

      for (int var2 = 0; var2 < this.iconArray.length; var2++) {
         this.iconArray[var2] = var1.registerIcon("cloth_" + var2);
      }
   }
}
