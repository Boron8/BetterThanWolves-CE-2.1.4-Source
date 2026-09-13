package net.minecraft.src;

import java.util.List;

public class BlockWall extends Block {
   public static final String[] types = new String[]{"normal", "mossy"};

   public BlockWall(int var1, Block var2) {
      super(var1, var2.blockMaterial);
      this.c(var2.blockHardness);
      this.b(var2.blockResistance / 3.0F);
      this.a(var2.stepSound);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return var2 == 1 ? Block.cobblestoneMossy.getBlockTextureFromSide(var1) : Block.cobblestone.getBlockTextureFromSide(var1);
   }

   @Override
   public int getRenderType() {
      return 32;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean getBlocksMovement(IBlockAccess var1, int var2, int var3, int var4) {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      boolean var5 = this.canConnectWallTo(var1, var2, var3, var4 - 1);
      boolean var6 = this.canConnectWallTo(var1, var2, var3, var4 + 1);
      boolean var7 = this.canConnectWallTo(var1, var2 - 1, var3, var4);
      boolean var8 = this.canConnectWallTo(var1, var2 + 1, var3, var4);
      float var9 = 0.25F;
      float var10 = 0.75F;
      float var11 = 0.25F;
      float var12 = 0.75F;
      float var13 = 1.0F;
      if (var5) {
         var11 = 0.0F;
      }

      if (var6) {
         var12 = 1.0F;
      }

      if (var7) {
         var9 = 0.0F;
      }

      if (var8) {
         var10 = 1.0F;
      }

      if (var5 && var6 && !var7 && !var8) {
         var13 = 0.8125F;
         var9 = 0.3125F;
         var10 = 0.6875F;
      } else if (!var5 && !var6 && var7 && var8) {
         var13 = 0.8125F;
         var11 = 0.3125F;
         var12 = 0.6875F;
      }

      this.a(var9, 0.0F, var11, var10, var13, var12);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      this.maxY = 1.5;
      return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
   }

   public boolean canConnectWallTo(IBlockAccess var1, int var2, int var3, int var4) {
      int var5 = var1.getBlockId(var2, var3, var4);
      if (var5 != this.blockID && var5 != Block.fenceGate.blockID) {
         Block var6 = Block.blocksList[var5];
         return var6 != null && var6.blockMaterial.isOpaque() && var6.renderAsNormalBlock() ? var6.blockMaterial != Material.pumpkin : false;
      } else {
         return true;
      }
   }

   @Override
   public void getSubBlocks(int var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, 0));
      var3.add(new ItemStack(var1, 1, 1));
   }

   @Override
   public int damageDropped(int var1) {
      return var1;
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return var5 == 0 ? super.shouldSideBeRendered(var1, var2, var3, var4, var5) : true;
   }

   @Override
   public void registerIcons(IconRegister var1) {
   }
}
