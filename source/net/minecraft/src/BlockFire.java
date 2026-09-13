package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockFire extends Block {
   public static int[] chanceToEncourageFire = new int[4096];
   public static int[] abilityToCatchFire = new int[4096];
   private Icon[] iconArray;

   protected BlockFire(int par1) {
      super(par1, Material.fire);
      this.b(true);
   }

   private void setBurnRate(int par1, int par2, int par3) {
      chanceToEncourageFire[par1] = par2;
      abilityToCatchFire[par1] = par3;
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
      return 3;
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return 0;
   }

   @Override
   public int tickRate(World par1World) {
      return 30;
   }

   @Override
   public boolean func_82506_l() {
      return false;
   }

   private void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7) {
      int var8 = abilityToCatchFire[par1World.getBlockId(par2, par3, par4)];
      if (par6Random.nextInt(par5) < var8) {
         boolean var9 = par1World.getBlockId(par2, par3, par4) == Block.tnt.blockID;
         if (par6Random.nextInt(par7 + 10) < 5 && !par1World.isRainingAtPos(par2, par3, par4)) {
            int var10 = par7 + par6Random.nextInt(5) / 4;
            if (var10 > 15) {
               var10 = 15;
            }

            par1World.setBlock(par2, par3, par4, this.blockID, var10, 3);
         } else {
            par1World.setBlockToAir(par2, par3, par4);
         }

         if (var9) {
            Block.tnt.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
         }
      }
   }

   protected boolean canNeighborBurn(World par1World, int par2, int par3, int par4) {
      return this.canBlockCatchFire(par1World, par2 + 1, par3, par4)
         ? true
         : (
            this.canBlockCatchFire(par1World, par2 - 1, par3, par4)
               ? true
               : (
                  this.canBlockCatchFire(par1World, par2, par3 - 1, par4)
                     ? true
                     : (
                        this.canBlockCatchFire(par1World, par2, par3 + 1, par4)
                           ? true
                           : (this.canBlockCatchFire(par1World, par2, par3, par4 - 1) ? true : this.canBlockCatchFire(par1World, par2, par3, par4 + 1))
                     )
               )
         );
   }

   @Override
   public boolean isCollidable() {
      return false;
   }

   public boolean canBlockCatchFire(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return chanceToEncourageFire[par1IBlockAccess.getBlockId(par2, par3, par4)] > 0;
   }

   @Override
   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || this.canNeighborBurn(par1World, par2, par3, par4);
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !this.canNeighborBurn(par1World, par2, par3, par4)) {
         par1World.setBlockToAir(par2, par3, par4);
      }
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      if (par1World.provider.dimensionId > 0
         || par1World.getBlockId(par2, par3 - 1, par4) != Block.obsidian.blockID
         || !Block.portal.tryToCreatePortal(par1World, par2, par3, par4)) {
         if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !this.canNeighborBurn(par1World, par2, par3, par4)) {
            par1World.setBlockToAir(par2, par3, par4);
         } else {
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World) + par1World.rand.nextInt(10));
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (par5Random.nextInt(24) == 0) {
         par1World.playSound(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "fire.fire", 1.0F + par5Random.nextFloat(), par5Random.nextFloat() * 0.7F + 0.3F, false);
      }

      if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !Block.fire.canBlockCatchFire(par1World, par2, par3 - 1, par4)) {
         if (Block.fire.canBlockCatchFire(par1World, par2 - 1, par3, par4)) {
            for (int var6 = 0; var6 < 2; var6++) {
               float var7 = par2 + par5Random.nextFloat() * 0.1F;
               float var8 = par3 + par5Random.nextFloat();
               float var9 = par4 + par5Random.nextFloat();
               par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0, 0.0, 0.0);
            }
         }

         if (Block.fire.canBlockCatchFire(par1World, par2 + 1, par3, par4)) {
            for (int var6 = 0; var6 < 2; var6++) {
               float var7 = par2 + 1 - par5Random.nextFloat() * 0.1F;
               float var8 = par3 + par5Random.nextFloat();
               float var9 = par4 + par5Random.nextFloat();
               par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0, 0.0, 0.0);
            }
         }

         if (Block.fire.canBlockCatchFire(par1World, par2, par3, par4 - 1)) {
            for (int var6 = 0; var6 < 2; var6++) {
               float var7 = par2 + par5Random.nextFloat();
               float var8 = par3 + par5Random.nextFloat();
               float var9 = par4 + par5Random.nextFloat() * 0.1F;
               par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0, 0.0, 0.0);
            }
         }

         if (Block.fire.canBlockCatchFire(par1World, par2, par3, par4 + 1)) {
            for (int var6 = 0; var6 < 2; var6++) {
               float var7 = par2 + par5Random.nextFloat();
               float var8 = par3 + par5Random.nextFloat();
               float var9 = par4 + 1 - par5Random.nextFloat() * 0.1F;
               par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0, 0.0, 0.0);
            }
         }

         if (Block.fire.canBlockCatchFire(par1World, par2, par3 + 1, par4)) {
            for (int var6 = 0; var6 < 2; var6++) {
               float var7 = par2 + par5Random.nextFloat();
               float var8 = par3 + 1 - par5Random.nextFloat() * 0.1F;
               float var9 = par4 + par5Random.nextFloat();
               par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0, 0.0, 0.0);
            }
         }
      } else {
         for (int var6 = 0; var6 < 3; var6++) {
            float var7 = par2 + par5Random.nextFloat();
            float var8 = par3 + par5Random.nextFloat() * 0.5F + 0.5F;
            float var9 = par4 + par5Random.nextFloat();
            par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0, 0.0, 0.0);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.iconArray = new Icon[]{par1IconRegister.registerIcon("fire_0"), par1IconRegister.registerIcon("fire_1")};
   }

   @Environment(EnvType.CLIENT)
   public Icon func_94438_c(int par1) {
      return this.iconArray[par1];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return this.iconArray[0];
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldFirePreferToDisplayUpwards(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.doesBlockHaveSolidTopSurface(i, j - 1, k) && Block.fire.canBlockCatchFire(blockAccess, i, j - 1, k)
         || this.isBlockInfiniteBurnToTopForRender(blockAccess, i, j - 1, k);
   }

   @Environment(EnvType.CLIENT)
   public boolean isBlockInfiniteBurnToTopForRender(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      return iBlockID == Block.netherrack.blockID || iBlockID == BTWBlocks.hibachi.blockID || this.doesInfiniteBurnToFacing(blockAccess, i, j, k, 1);
   }
}
