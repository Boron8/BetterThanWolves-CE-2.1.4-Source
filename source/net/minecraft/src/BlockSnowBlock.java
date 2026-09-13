package net.minecraft.src;

import java.util.Random;

public class BlockSnowBlock extends Block {
   protected BlockSnowBlock(int var1) {
      super(var1, Material.craftedSnow);
      this.b(true);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Item.snowball.itemID;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 4;
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (var1.getSavedLightValue(EnumSkyBlock.Block, var2, var3, var4) > 11) {
         this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
         var1.setBlockToAir(var2, var3, var4);
      }
   }
}
