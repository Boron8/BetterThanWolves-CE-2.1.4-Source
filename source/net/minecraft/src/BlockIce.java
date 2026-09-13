package net.minecraft.src;

import java.util.Random;

public class BlockIce extends BlockBreakable {
   public BlockIce(int var1) {
      super(var1, "ice", Material.ice, false);
      this.slipperiness = 0.98F;
      this.b(true);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int getRenderBlockPass() {
      return 1;
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return super.shouldSideBeRendered(var1, var2, var3, var4, 1 - var5);
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6) {
      var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
      var2.addExhaustion(0.025F);
      if (this.r_() && EnchantmentHelper.getSilkTouchModifier(var2)) {
         ItemStack var9 = this.c_(var6);
         if (var9 != null) {
            this.b(var1, var3, var4, var5, var9);
         }
      } else {
         if (var1.provider.isHellWorld) {
            var1.setBlockToAir(var3, var4, var5);
            return;
         }

         int var7 = EnchantmentHelper.getFortuneModifier(var2);
         this.c(var1, var3, var4, var5, var6, var7);
         Material var8 = var1.getBlockMaterial(var3, var4 - 1, var5);
         if (var8.blocksMovement() || var8.isLiquid()) {
            var1.setBlock(var3, var4, var5, Block.waterMoving.blockID);
         }
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (var1.getSavedLightValue(EnumSkyBlock.Block, var2, var3, var4) > 11 - Block.lightOpacity[this.blockID]) {
         if (var1.provider.isHellWorld) {
            var1.setBlockToAir(var2, var3, var4);
            return;
         }

         this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
         var1.setBlock(var2, var3, var4, Block.waterStill.blockID);
      }
   }

   @Override
   public int getMobilityFlag() {
      return 0;
   }
}
