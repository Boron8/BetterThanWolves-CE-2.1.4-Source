package net.minecraft.src;

import btw.block.blocks.PlantsBlock;
import java.util.Random;

public class BlockFlower extends PlantsBlock {
   protected BlockFlower(int par1, Material par2Material) {
      super(par1, par2Material);
      this.b(true);
      float var3 = 0.2F;
      this.initBlockBounds(0.5F - var3, 0.0, 0.5F - var3, 0.5F + var3, var3 * 3.0F, 0.5F + var3);
      this.a(CreativeTabs.tabDecorations);
   }

   protected BlockFlower(int par1) {
      this(par1, Material.plants);
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      super.a(par1World, par2, par3, par4, par5);
      this.checkFlowerChange(par1World, par2, par3, par4);
   }

   @Override
   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      this.checkFlowerChange(par1World, par2, par3, par4);
   }

   protected final void checkFlowerChange(World par1World, int par2, int par3, int par4) {
      if (!this.canBlockStay(par1World, par2, par3, par4)) {
         this.c(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlockToAir(par2, par3, par4);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
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
   public int getRenderType() {
      return 1;
   }

   @Override
   public boolean canBlockStay(World world, int i, int j, int k) {
      return (world.getFullBlockLightValue(i, j, k) >= 8 || world.canBlockSeeTheSky(i, j, k)) && super.canBlockStay(world, i, j, k);
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return animal.canGrazeOnRoughVegetation();
   }
}
