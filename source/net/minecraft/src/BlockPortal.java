package net.minecraft.src;

import java.util.Random;

public class BlockPortal extends BlockBreakable {
   public BlockPortal(int var1) {
      super(var1, "portal", Material.portal, false);
      this.b(true);
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      super.a(var1, var2, var3, var4, var5);
      if (var1.provider.isSurfaceWorld() && var5.nextInt(2000) < var1.difficultySetting) {
         int var6 = var3;

         while (!var1.doesBlockHaveSolidTopSurface(var2, var6, var4) && var6 > 0) {
            var6--;
         }

         if (var6 > 0 && !var1.isBlockNormalCube(var2, var6 + 1, var4)) {
            Entity var7 = ItemMonsterPlacer.spawnCreature(var1, 57, var2 + 0.5, var6 + 1.1, var4 + 0.5);
            if (var7 != null) {
               var7.timeUntilPortal = var7.getPortalCooldown();
            }
         }
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      return null;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      if (var1.getBlockId(var2 - 1, var3, var4) != this.blockID && var1.getBlockId(var2 + 1, var3, var4) != this.blockID) {
         float var7 = 0.125F;
         float var8 = 0.5F;
         this.a(0.5F - var7, 0.0F, 0.5F - var8, 0.5F + var7, 1.0F, 0.5F + var8);
      } else {
         float var5 = 0.5F;
         float var6 = 0.125F;
         this.a(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
      }
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean tryToCreatePortal(World var1, int var2, int var3, int var4) {
      byte var5 = 0;
      byte var6 = 0;
      if (var1.getBlockId(var2 - 1, var3, var4) == Block.obsidian.blockID || var1.getBlockId(var2 + 1, var3, var4) == Block.obsidian.blockID) {
         var5 = 1;
      }

      if (var1.getBlockId(var2, var3, var4 - 1) == Block.obsidian.blockID || var1.getBlockId(var2, var3, var4 + 1) == Block.obsidian.blockID) {
         var6 = 1;
      }

      if (var5 == var6) {
         return false;
      } else {
         if (var1.getBlockId(var2 - var5, var3, var4 - var6) == 0) {
            var2 -= var5;
            var4 -= var6;
         }

         for (int var7 = -1; var7 <= 2; var7++) {
            for (int var8 = -1; var8 <= 3; var8++) {
               boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 3;
               if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 3) {
                  int var10 = var1.getBlockId(var2 + var5 * var7, var3 + var8, var4 + var6 * var7);
                  if (var9) {
                     if (var10 != Block.obsidian.blockID) {
                        return false;
                     }
                  } else if (var10 != 0 && var10 != Block.fire.blockID) {
                     return false;
                  }
               }
            }
         }

         for (int var11 = 0; var11 < 2; var11++) {
            for (int var12 = 0; var12 < 3; var12++) {
               var1.setBlock(var2 + var5 * var11, var3 + var12, var4 + var6 * var11, Block.portal.blockID, 0, 2);
            }
         }

         return true;
      }
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      byte var6 = 0;
      byte var7 = 1;
      if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
         var6 = 1;
         var7 = 0;
      }

      int var8 = var3;

      while (var1.getBlockId(var2, var8 - 1, var4) == this.blockID) {
         var8--;
      }

      if (var1.getBlockId(var2, var8 - 1, var4) != Block.obsidian.blockID) {
         var1.setBlockToAir(var2, var3, var4);
      } else {
         int var9 = 1;

         while (var9 < 4 && var1.getBlockId(var2, var8 + var9, var4) == this.blockID) {
            var9++;
         }

         if (var9 == 3 && var1.getBlockId(var2, var8 + var9, var4) == Block.obsidian.blockID) {
            boolean var10 = var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID;
            boolean var11 = var1.getBlockId(var2, var3, var4 - 1) == this.blockID || var1.getBlockId(var2, var3, var4 + 1) == this.blockID;
            if (var10 && var11) {
               var1.setBlockToAir(var2, var3, var4);
            } else {
               if ((
                     var1.getBlockId(var2 + var6, var3, var4 + var7) != Block.obsidian.blockID
                        || var1.getBlockId(var2 - var6, var3, var4 - var7) != this.blockID
                  )
                  && (
                     var1.getBlockId(var2 - var6, var3, var4 - var7) != Block.obsidian.blockID
                        || var1.getBlockId(var2 + var6, var3, var4 + var7) != this.blockID
                  )) {
                  var1.setBlockToAir(var2, var3, var4);
               }
            }
         } else {
            var1.setBlockToAir(var2, var3, var4);
         }
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      if (var1.getBlockId(var2, var3, var4) == this.blockID) {
         return false;
      } else {
         boolean var6 = var1.getBlockId(var2 - 1, var3, var4) == this.blockID && var1.getBlockId(var2 - 2, var3, var4) != this.blockID;
         boolean var7 = var1.getBlockId(var2 + 1, var3, var4) == this.blockID && var1.getBlockId(var2 + 2, var3, var4) != this.blockID;
         boolean var8 = var1.getBlockId(var2, var3, var4 - 1) == this.blockID && var1.getBlockId(var2, var3, var4 - 2) != this.blockID;
         boolean var9 = var1.getBlockId(var2, var3, var4 + 1) == this.blockID && var1.getBlockId(var2, var3, var4 + 2) != this.blockID;
         boolean var10 = var6 || var7;
         boolean var11 = var8 || var9;
         if (var10 && var5 == 4) {
            return true;
         } else if (var10 && var5 == 5) {
            return true;
         } else {
            return var11 && var5 == 2 ? true : var11 && var5 == 3;
         }
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public int getRenderBlockPass() {
      return 1;
   }

   @Override
   public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5) {
      if (var5.ridingEntity == null && var5.riddenByEntity == null) {
         var5.setInPortal();
      }
   }

   @Override
   public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
      if (var5.nextInt(100) == 0) {
         var1.playSound(var2 + 0.5, var3 + 0.5, var4 + 0.5, "portal.portal", 0.5F, var5.nextFloat() * 0.4F + 0.8F, false);
      }

      for (int var6 = 0; var6 < 4; var6++) {
         double var7 = var2 + var5.nextFloat();
         double var9 = var3 + var5.nextFloat();
         double var11 = var4 + var5.nextFloat();
         double var13 = 0.0;
         double var15 = 0.0;
         double var17 = 0.0;
         int var19 = var5.nextInt(2) * 2 - 1;
         var13 = (var5.nextFloat() - 0.5) * 0.5;
         var15 = (var5.nextFloat() - 0.5) * 0.5;
         var17 = (var5.nextFloat() - 0.5) * 0.5;
         if (var1.getBlockId(var2 - 1, var3, var4) != this.blockID && var1.getBlockId(var2 + 1, var3, var4) != this.blockID) {
            var7 = var2 + 0.5 + 0.25 * var19;
            var13 = var5.nextFloat() * 2.0F * var19;
         } else {
            var11 = var4 + 0.5 + 0.25 * var19;
            var17 = var5.nextFloat() * 2.0F * var19;
         }

         var1.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
      }
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return 0;
   }
}
