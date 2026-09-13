package net.minecraft.src;

import java.util.Random;

public class BlockFarmland extends Block {
   private Icon field_94441_a;
   private Icon field_94440_b;

   protected BlockFarmland(int var1) {
      super(var1, Material.ground);
      this.b(true);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
      this.k(255);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      return AxisAlignedBB.getAABBPool().getAABB(var2 + 0, var3 + 0, var4 + 0, var2 + 1, var3 + 1, var4 + 1);
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
   public Icon getIcon(int var1, int var2) {
      if (var1 == 1) {
         return var2 > 0 ? this.field_94441_a : this.field_94440_b;
      } else {
         return Block.dirt.getBlockTextureFromSide(var1);
      }
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (!this.isWaterNearby(var1, var2, var3, var4) && !var1.canLightningStrikeAt(var2, var3 + 1, var4)) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         if (var6 > 0) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, var6 - 1, 2);
         } else if (!this.isCropsNearby(var1, var2, var3, var4)) {
            var1.setBlock(var2, var3, var4, Block.dirt.blockID);
         }
      } else {
         var1.setBlockMetadataWithNotify(var2, var3, var4, 7, 2);
      }
   }

   @Override
   public void onFallenUpon(World var1, int var2, int var3, int var4, Entity var5, float var6) {
      if (!var1.isRemote && var1.rand.nextFloat() < var6 - 0.5F) {
         if (!(var5 instanceof EntityPlayer) && !var1.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
            return;
         }

         var1.setBlock(var2, var3, var4, Block.dirt.blockID);
      }
   }

   private boolean isCropsNearby(World var1, int var2, int var3, int var4) {
      byte var5 = 0;

      for (int var6 = var2 - var5; var6 <= var2 + var5; var6++) {
         for (int var7 = var4 - var5; var7 <= var4 + var5; var7++) {
            int var8 = var1.getBlockId(var6, var3 + 1, var7);
            if (var8 == Block.crops.blockID
               || var8 == Block.melonStem.blockID
               || var8 == Block.pumpkinStem.blockID
               || var8 == Block.potato.blockID
               || var8 == Block.carrot.blockID) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean isWaterNearby(World var1, int var2, int var3, int var4) {
      for (int var5 = var2 - 4; var5 <= var2 + 4; var5++) {
         for (int var6 = var3; var6 <= var3 + 1; var6++) {
            for (int var7 = var4 - 4; var7 <= var4 + 4; var7++) {
               if (var1.getBlockMaterial(var5, var6, var7) == Material.water) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      super.onNeighborBlockChange(var1, var2, var3, var4, var5);
      Material var6 = var1.getBlockMaterial(var2, var3 + 1, var4);
      if (var6.isSolid()) {
         var1.setBlock(var2, var3, var4, Block.dirt.blockID);
      }
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Block.dirt.idDropped(0, var2, var3);
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return Block.dirt.blockID;
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.field_94441_a = var1.registerIcon("farmland_wet");
      this.field_94440_b = var1.registerIcon("farmland_dry");
   }
}
