package net.minecraft.src;

import java.util.Random;

public class BlockGlass extends BlockBreakable {
   public BlockGlass(int var1, Material var2, boolean var3) {
      super(var1, "glass", var2, var3);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public int getRenderBlockPass() {
      return 0;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }
}
