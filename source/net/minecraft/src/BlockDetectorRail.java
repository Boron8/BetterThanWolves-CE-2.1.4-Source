package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockDetectorRail extends BlockRailBase {
   private Icon[] iconArray;

   public BlockDetectorRail(int var1) {
      super(var1, true);
      this.b(true);
   }

   @Override
   public int tickRate(World var1) {
      return 20;
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }

   @Override
   public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
      if (!var1.isRemote) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         if ((var6 & 8) == 0) {
            this.setStateIfMinecartInteractsWithRail(var1, var2, var3, var4, var6);
         }
      }
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (!var1.isRemote) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         if ((var6 & 8) != 0) {
            this.setStateIfMinecartInteractsWithRail(var1, var2, var3, var4, var6);
         }
      }
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return (var1.getBlockMetadata(var2, var3, var4) & 8) != 0 ? 15 : 0;
   }

   @Override
   public int isProvidingStrongPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      if ((var1.getBlockMetadata(var2, var3, var4) & 8) == 0) {
         return 0;
      } else {
         return var5 == 1 ? 15 : 0;
      }
   }

   private void setStateIfMinecartInteractsWithRail(World var1, int var2, int var3, int var4, int var5) {
      boolean var6 = (var5 & 8) != 0;
      boolean var7 = false;
      float var8 = 0.125F;
      List var9 = var1.getEntitiesWithinAABB(
         EntityMinecart.class, AxisAlignedBB.getAABBPool().getAABB(var2 + var8, var3, var4 + var8, var2 + 1 - var8, var3 + 1 - var8, var4 + 1 - var8)
      );
      if (!var9.isEmpty()) {
         var7 = true;
      }

      if (var7 && !var6) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, var5 | 8, 3);
         var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
         var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
         var1.markBlockRangeForRenderUpdate(var2, var3, var4, var2, var3, var4);
      }

      if (!var7 && var6) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, var5 & 7, 3);
         var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
         var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
         var1.markBlockRangeForRenderUpdate(var2, var3, var4, var2, var3, var4);
      }

      if (var7) {
         var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1));
      }

      var1.func_96440_m(var2, var3, var4, this.blockID);
   }

   @Override
   public void onBlockAdded(World var1, int var2, int var3, int var4) {
      super.onBlockAdded(var1, var2, var3, var4);
      this.setStateIfMinecartInteractsWithRail(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World var1, int var2, int var3, int var4, int var5) {
      if ((var1.getBlockMetadata(var2, var3, var4) & 8) > 0) {
         float var6 = 0.125F;
         List var7 = var1.selectEntitiesWithinAABB(
            EntityMinecart.class,
            AxisAlignedBB.getAABBPool().getAABB(var2 + var6, var3, var4 + var6, var2 + 1 - var6, var3 + 1 - var6, var4 + 1 - var6),
            IEntitySelector.selectInventories
         );
         if (var7.size() > 0) {
            return Container.calcRedstoneFromInventory((IInventory)var7.get(0));
         }
      }

      return 0;
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.iconArray = new Icon[2];
      this.iconArray[0] = var1.registerIcon("detectorRail");
      this.iconArray[1] = var1.registerIcon("detectorRail_on");
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return (var2 & 8) != 0 ? this.iconArray[1] : this.iconArray[0];
   }
}
