package net.minecraft.src;

public class BlockTrapDoor extends Block {
   protected BlockTrapDoor(int var1, Material var2) {
      super(var1, var2);
      float var3 = 0.5F;
      float var4 = 1.0F;
      this.a(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
      this.a(CreativeTabs.tabRedstone);
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
      return !isTrapdoorOpen(var1.getBlockMetadata(var2, var3, var4));
   }

   @Override
   public int getRenderType() {
      return 0;
   }

   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      return super.getSelectedBoundingBoxFromPool(var1, var2, var3, var4);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      this.setBlockBoundsForBlockRender(var1.getBlockMetadata(var2, var3, var4));
   }

   @Override
   public void setBlockBoundsForItemRender() {
      float var1 = 0.1875F;
      this.a(0.0F, 0.5F - var1 / 2.0F, 0.0F, 1.0F, 0.5F + var1 / 2.0F, 1.0F);
   }

   public void setBlockBoundsForBlockRender(int var1) {
      float var2 = 0.1875F;
      if ((var1 & 8) != 0) {
         this.a(0.0F, 1.0F - var2, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, var2, 1.0F);
      }

      if (isTrapdoorOpen(var1)) {
         if ((var1 & 3) == 0) {
            this.a(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
         }

         if ((var1 & 3) == 1) {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
         }

         if ((var1 & 3) == 2) {
            this.a(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         }

         if ((var1 & 3) == 3) {
            this.a(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
         }
      }
   }

   @Override
   public void onBlockClicked(World var1, int var2, int var3, int var4, EntityPlayer var5) {
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      if (this.blockMaterial == Material.iron) {
         return true;
      } else {
         int var10 = var1.getBlockMetadata(var2, var3, var4);
         var1.setBlockMetadataWithNotify(var2, var3, var4, var10 ^ 4, 2);
         var1.playAuxSFXAtEntity(var5, 1003, var2, var3, var4, 0);
         return true;
      }
   }

   public void onPoweredBlockChange(World var1, int var2, int var3, int var4, boolean var5) {
      int var6 = var1.getBlockMetadata(var2, var3, var4);
      boolean var7 = (var6 & 4) > 0;
      if (var7 != var5) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, var6 ^ 4, 2);
         var1.playAuxSFXAtEntity(null, 1003, var2, var3, var4, 0);
      }
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      if (!var1.isRemote) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         int var7 = var2;
         int var8 = var4;
         if ((var6 & 3) == 0) {
            var8 = var4 + 1;
         }

         if ((var6 & 3) == 1) {
            var8--;
         }

         if ((var6 & 3) == 2) {
            var7 = var2 + 1;
         }

         if ((var6 & 3) == 3) {
            var7--;
         }

         if (!isValidSupportBlock(var1.getBlockId(var7, var3, var8))) {
            var1.setBlockToAir(var2, var3, var4);
            this.c(var1, var2, var3, var4, var6, 0);
         }

         boolean var9 = var1.isBlockIndirectlyGettingPowered(var2, var3, var4);
         if (var9 || var5 > 0 && Block.blocksList[var5].canProvidePower()) {
            this.onPoweredBlockChange(var1, var2, var3, var4, var9);
         }
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3 var5, Vec3 var6) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      return super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
   }

   @Override
   public int onBlockPlaced(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      byte var10 = 0;
      if (var5 == 2) {
         var10 = 0;
      }

      if (var5 == 3) {
         var10 = 1;
      }

      if (var5 == 4) {
         var10 = 2;
      }

      if (var5 == 5) {
         var10 = 3;
      }

      if (var5 != 1 && var5 != 0 && var7 > 0.5F) {
         var10 |= 8;
      }

      return var10;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, int var2, int var3, int var4, int var5) {
      if (var5 == 0) {
         return false;
      } else if (var5 == 1) {
         return false;
      } else {
         if (var5 == 2) {
            var4++;
         }

         if (var5 == 3) {
            var4--;
         }

         if (var5 == 4) {
            var2++;
         }

         if (var5 == 5) {
            var2--;
         }

         return isValidSupportBlock(var1.getBlockId(var2, var3, var4));
      }
   }

   public static boolean isTrapdoorOpen(int var0) {
      return (var0 & 4) != 0;
   }

   private static boolean isValidSupportBlock(int var0) {
      if (var0 <= 0) {
         return false;
      } else {
         Block var1 = Block.blocksList[var0];
         return var1 != null && var1.blockMaterial.isOpaque() && var1.renderAsNormalBlock()
            || var1 == Block.glowStone
            || var1 instanceof BlockHalfSlab
            || var1 instanceof BlockStairs;
      }
   }
}
