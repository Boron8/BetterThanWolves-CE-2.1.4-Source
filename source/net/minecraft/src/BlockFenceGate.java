package net.minecraft.src;

public class BlockFenceGate extends BlockDirectional {
   public BlockFenceGate(int var1) {
      super(var1, Material.wood);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return Block.planks.getBlockTextureFromSide(var1);
   }

   @Override
   public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
      return !var1.getBlockMaterial(var2, var3 - 1, var4).isSolid() ? false : super.c(var1, var2, var3, var4);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      int var5 = var1.getBlockMetadata(var2, var3, var4);
      if (isFenceGateOpen(var5)) {
         return null;
      } else {
         return var5 != 2 && var5 != 0
            ? AxisAlignedBB.getAABBPool().getAABB(var2 + 0.375F, var3, var4, var2 + 0.625F, var3 + 1.5F, var4 + 1)
            : AxisAlignedBB.getAABBPool().getAABB(var2, var3, var4 + 0.375F, var2 + 1, var3 + 1.5F, var4 + 0.625F);
      }
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      int var5 = j(var1.getBlockMetadata(var2, var3, var4));
      if (var5 != 2 && var5 != 0) {
         this.a(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
      } else {
         this.a(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
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

   @Override
   public boolean getBlocksMovement(IBlockAccess var1, int var2, int var3, int var4) {
      return isFenceGateOpen(var1.getBlockMetadata(var2, var3, var4));
   }

   @Override
   public int getRenderType() {
      return 21;
   }

   @Override
   public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6) {
      int var7 = (MathHelper.floor_double(var5.rotationYaw * 4.0F / 360.0F + 0.5) & 3) % 4;
      var1.setBlockMetadataWithNotify(var2, var3, var4, var7, 2);
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      int var10 = var1.getBlockMetadata(var2, var3, var4);
      if (isFenceGateOpen(var10)) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, var10 & -5, 2);
      } else {
         int var11 = (MathHelper.floor_double(var5.rotationYaw * 4.0F / 360.0F + 0.5) & 3) % 4;
         int var12 = j(var10);
         if (var12 == (var11 + 2) % 4) {
            var10 = var11;
         }

         var1.setBlockMetadataWithNotify(var2, var3, var4, var10 | 4, 2);
      }

      var1.playAuxSFXAtEntity(var5, 1003, var2, var3, var4, 0);
      return true;
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      if (!var1.isRemote) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         boolean var7 = var1.isBlockIndirectlyGettingPowered(var2, var3, var4);
         if (var7 || var5 > 0 && Block.blocksList[var5].canProvidePower()) {
            if (var7 && !isFenceGateOpen(var6)) {
               var1.setBlockMetadataWithNotify(var2, var3, var4, var6 | 4, 2);
               var1.playAuxSFXAtEntity(null, 1003, var2, var3, var4, 0);
            } else if (!var7 && isFenceGateOpen(var6)) {
               var1.setBlockMetadataWithNotify(var2, var3, var4, var6 & -5, 2);
               var1.playAuxSFXAtEntity(null, 1003, var2, var3, var4, 0);
            }
         }
      }
   }

   public static boolean isFenceGateOpen(int var0) {
      return (var0 & 4) != 0;
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return true;
   }

   @Override
   public void registerIcons(IconRegister var1) {
   }
}
