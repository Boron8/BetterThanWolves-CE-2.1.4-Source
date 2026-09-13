package net.minecraft.src;

public class BlockSoulSand extends Block {
   public BlockSoulSand(int var1) {
      super(var1, Material.sand);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      float var5 = 0.125F;
      return AxisAlignedBB.getAABBPool().getAABB(var2, var3, var4, var2 + 1, var3 + 1 - var5, var4 + 1);
   }

   @Override
   public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
      var5.motionX *= 0.4;
      var5.motionZ *= 0.4;
   }
}
