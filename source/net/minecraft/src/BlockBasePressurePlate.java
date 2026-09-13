package net.minecraft.src;

import java.util.Random;

public abstract class BlockBasePressurePlate extends Block {
   private String pressurePlateIconName;

   protected BlockBasePressurePlate(int var1, String var2, Material var3) {
      super(var1, var3);
      this.pressurePlateIconName = var2;
      this.a(CreativeTabs.tabRedstone);
      this.b(true);
      this.func_94353_c_(this.getMetaFromWeight(15));
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      this.func_94353_c_(var1.getBlockMetadata(var2, var3, var4));
   }

   protected void func_94353_c_(int var1) {
      boolean var2 = this.getPowerSupply(var1) > 0;
      float var3 = 0.0625F;
      if (var2) {
         this.a(var3, 0.0F, var3, 1.0F - var3, 0.03125F, 1.0F - var3);
      } else {
         this.a(var3, 0.0F, var3, 1.0F - var3, 0.0625F, 1.0F - var3);
      }
   }

   @Override
   public int tickRate(World var1) {
      return 20;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      return null;
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
   public boolean getBlocksMovement(IBlockAccess var1, int var2, int var3, int var4) {
      return true;
   }

   @Override
   public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
      return var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) || BlockFence.isIdAFence(var1.getBlockId(var2, var3 - 1, var4));
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      boolean var6 = false;
      if (!var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) && !BlockFence.isIdAFence(var1.getBlockId(var2, var3 - 1, var4))) {
         var6 = true;
      }

      if (var6) {
         this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
         var1.setBlockToAir(var2, var3, var4);
      }
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (!var1.isRemote) {
         int var6 = this.getPowerSupply(var1.getBlockMetadata(var2, var3, var4));
         if (var6 > 0) {
            this.setStateIfMobInteractsWithPlate(var1, var2, var3, var4, var6);
         }
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
      if (!var1.isRemote) {
         int var6 = this.getPowerSupply(var1.getBlockMetadata(var2, var3, var4));
         if (var6 == 0) {
            this.setStateIfMobInteractsWithPlate(var1, var2, var3, var4, var6);
         }
      }
   }

   protected void setStateIfMobInteractsWithPlate(World var1, int var2, int var3, int var4, int var5) {
      int var6 = this.getPlateState(var1, var2, var3, var4);
      boolean var7 = var5 > 0;
      boolean var8 = var6 > 0;
      if (var5 != var6) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, this.getMetaFromWeight(var6), 2);
         this.func_94354_b_(var1, var2, var3, var4);
         var1.markBlockRangeForRenderUpdate(var2, var3, var4, var2, var3, var4);
      }

      if (!var8 && var7) {
         var1.playSoundEffect(var2 + 0.5, var3 + 0.1, var4 + 0.5, "random.click", 0.3F, 0.5F);
      } else if (var8 && !var7) {
         var1.playSoundEffect(var2 + 0.5, var3 + 0.1, var4 + 0.5, "random.click", 0.3F, 0.6F);
      }

      if (var8) {
         var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1));
      }
   }

   protected AxisAlignedBB getSensitiveAABB(int var1, int var2, int var3) {
      float var4 = 0.125F;
      return AxisAlignedBB.getAABBPool().getAABB(var1 + var4, var2, var3 + var4, var1 + 1 - var4, var2 + 0.25, var3 + 1 - var4);
   }

   @Override
   public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
      if (this.getPowerSupply(var6) > 0) {
         this.func_94354_b_(var1, var2, var3, var4);
      }

      super.breakBlock(var1, var2, var3, var4, var5, var6);
   }

   protected void func_94354_b_(World var1, int var2, int var3, int var4) {
      var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
      var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return this.getPowerSupply(var1.getBlockMetadata(var2, var3, var4));
   }

   @Override
   public int isProvidingStrongPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return var5 == 1 ? this.getPowerSupply(var1.getBlockMetadata(var2, var3, var4)) : 0;
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }

   @Override
   public void setBlockBoundsForItemRender() {
      float var1 = 0.5F;
      float var2 = 0.125F;
      float var3 = 0.5F;
      this.a(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
   }

   @Override
   public int getMobilityFlag() {
      return 1;
   }

   protected abstract int getPlateState(World var1, int var2, int var3, int var4);

   protected abstract int getPowerSupply(int var1);

   protected abstract int getMetaFromWeight(int var1);

   @Override
   public void registerIcons(IconRegister var1) {
      this.blockIcon = var1.registerIcon(this.pressurePlateIconName);
   }
}
