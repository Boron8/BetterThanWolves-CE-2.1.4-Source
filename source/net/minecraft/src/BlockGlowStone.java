package net.minecraft.src;

import java.util.Random;

public class BlockGlowStone extends Block {
   public BlockGlowStone(int var1, Material var2) {
      super(var1, var2);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      return MathHelper.clamp_int(this.quantityDropped(var2) + var2.nextInt(var1 + 1), 1, 4);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 2 + var1.nextInt(3);
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Item.lightStoneDust.itemID;
   }
}
