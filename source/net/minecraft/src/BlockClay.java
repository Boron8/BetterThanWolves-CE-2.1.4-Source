package net.minecraft.src;

import java.util.Random;

public class BlockClay extends Block {
   public BlockClay(int var1) {
      super(var1, Material.clay);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Item.clay.itemID;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 4;
   }
}
