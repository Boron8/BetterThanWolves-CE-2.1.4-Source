package net.minecraft.src;

import java.util.Random;

public class BlockDaylightDetector extends BlockContainer {
   private Icon[] iconArray = new Icon[2];

   public BlockDaylightDetector(int var1) {
      super(var1, Material.wood);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return var1.getBlockMetadata(var2, var3, var4);
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
   }

   @Override
   public void onBlockAdded(World var1, int var2, int var3, int var4) {
   }

   public void updateLightLevel(World var1, int var2, int var3, int var4) {
      if (!var1.provider.hasNoSky) {
         int var5 = var1.getBlockMetadata(var2, var3, var4);
         int var6 = var1.getSavedLightValue(EnumSkyBlock.Sky, var2, var3, var4) - var1.skylightSubtracted;
         float var7 = var1.getCelestialAngleRadians(1.0F);
         if (var7 < (float) Math.PI) {
            var7 += (0.0F - var7) * 0.2F;
         } else {
            var7 += ((float) (Math.PI * 2) - var7) * 0.2F;
         }

         var6 = Math.round(var6 * MathHelper.cos(var7));
         if (var6 < 0) {
            var6 = 0;
         }

         if (var6 > 15) {
            var6 = 15;
         }

         if (var5 != var6) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, var6, 3);
         }
      }
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }

   @Override
   public TileEntity createNewTileEntity(World var1) {
      return new TileEntityDaylightDetector();
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return var1 == 1 ? this.iconArray[0] : this.iconArray[1];
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.iconArray[0] = var1.registerIcon("daylightDetector_top");
      this.iconArray[1] = var1.registerIcon("daylightDetector_side");
   }
}
