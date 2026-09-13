package net.minecraft.src;

import java.util.Random;

public class BlockEnderChest extends BlockContainer {
   protected BlockEnderChest(int var1) {
      super(var1, Material.rock);
      this.a(CreativeTabs.tabDecorations);
      this.a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
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
   public int getRenderType() {
      return 22;
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Block.obsidian.blockID;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 8;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6) {
      byte var7 = 0;
      int var8 = MathHelper.floor_double(var5.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
      if (var8 == 0) {
         var7 = 2;
      }

      if (var8 == 1) {
         var7 = 5;
      }

      if (var8 == 2) {
         var7 = 3;
      }

      if (var8 == 3) {
         var7 = 4;
      }

      var1.setBlockMetadataWithNotify(var2, var3, var4, var7, 2);
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      InventoryEnderChest var10 = var5.getInventoryEnderChest();
      TileEntityEnderChest var11 = (TileEntityEnderChest)var1.getBlockTileEntity(var2, var3, var4);
      if (var10 != null && var11 != null) {
         if (var1.isBlockNormalCube(var2, var3 + 1, var4)) {
            return true;
         } else if (var1.isRemote) {
            return true;
         } else {
            var10.setAssociatedChest(var11);
            var5.displayGUIChest(var10);
            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public TileEntity createNewTileEntity(World var1) {
      return new TileEntityEnderChest();
   }

   @Override
   public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
      for (int var6 = 0; var6 < 3; var6++) {
         double var7 = var2 + var5.nextFloat();
         double var9 = var3 + var5.nextFloat();
         double var11 = var4 + var5.nextFloat();
         double var13 = 0.0;
         double var15 = 0.0;
         double var17 = 0.0;
         int var19 = var5.nextInt(2) * 2 - 1;
         int var20 = var5.nextInt(2) * 2 - 1;
         var13 = (var5.nextFloat() - 0.5) * 0.125;
         var15 = (var5.nextFloat() - 0.5) * 0.125;
         var17 = (var5.nextFloat() - 0.5) * 0.125;
         var11 = var4 + 0.5 + 0.25 * var20;
         var17 = var5.nextFloat() * 1.0F * var20;
         var7 = var2 + 0.5 + 0.25 * var19;
         var13 = var5.nextFloat() * 1.0F * var19;
         var1.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
      }
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.blockIcon = var1.registerIcon("obsidian");
   }
}
