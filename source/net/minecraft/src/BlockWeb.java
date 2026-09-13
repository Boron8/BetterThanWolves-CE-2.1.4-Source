package net.minecraft.src;

import java.util.Random;

public class BlockWeb extends Block {
   public BlockWeb(int var1) {
      super(var1, Material.web);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
      var5.setInWeb();
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      return null;
   }

   @Override
   public int getRenderType() {
      return 1;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Item.silk.itemID;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }
}
