package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockEndPortal extends BlockContainer {
   public static boolean bossDefeated = false;

   protected BlockEndPortal(int var1, Material var2) {
      super(var1, var2);
      this.a(1.0F);
   }

   @Override
   public TileEntity createNewTileEntity(World var1) {
      return new TileEntityEndPortal();
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      float var5 = 0.0625F;
      this.a(0.0F, 0.0F, 0.0F, 1.0F, var5, 1.0F);
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return var5 != 0 ? false : super.a(var1, var2, var3, var4, var5);
   }

   @Override
   public void addCollisionBoxesToList(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List var6, Entity var7) {
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
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
      if (var5.ridingEntity == null && var5.riddenByEntity == null && !var1.isRemote) {
         var5.travelToDimension(1);
      }
   }

   @Override
   public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
      double var6 = var2 + var5.nextFloat();
      double var8 = var3 + 0.8F;
      double var10 = var4 + var5.nextFloat();
      double var12 = 0.0;
      double var14 = 0.0;
      double var16 = 0.0;
      var1.spawnParticle("smoke", var6, var8, var10, var12, var14, var16);
   }

   @Override
   public int getRenderType() {
      return -1;
   }

   @Override
   public void onBlockAdded(World var1, int var2, int var3, int var4) {
      if (!bossDefeated) {
         if (var1.provider.dimensionId != 0) {
            var1.setBlockToAir(var2, var3, var4);
         }
      }
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return 0;
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.blockIcon = var1.registerIcon("portal");
   }
}
