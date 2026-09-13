package net.minecraft.src;

import btw.block.BTWBlocks;
import com.prupe.mcpatcher.cc.ColorizeBlock;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class BlockFluid extends Block {
   @Environment(EnvType.CLIENT)
   private Icon[] theIcon;
   @Environment(EnvType.CLIENT)
   public static boolean isAnySideBeingRendered;

   protected BlockFluid(int par1, Material par2Material) {
      super(par1, par2Material);
      float var3 = 0.0F;
      float var4 = 0.0F;
      this.initBlockBounds(0.0F + var4, 0.0F + var3, 0.0F + var4, 1.0F + var4, 1.0F + var3, 1.0F + var4);
      this.b(true);
   }

   @Override
   public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return this.blockMaterial != Material.lava;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBlockColor() {
      return ColorizeBlock.colorizeBlock(this) ? ColorizeBlock.blockColor : 16777215;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      if (ColorizeBlock.colorizeBlock(this, par1IBlockAccess, par2, par3, par4)) {
         return ColorizeBlock.blockColor;
      } else if (this.blockMaterial != Material.water) {
         return 16777215;
      } else {
         int var5 = 0;
         int var6 = 0;
         int var7 = 0;

         for (int var8 = -1; var8 <= 1; var8++) {
            for (int var9 = -1; var9 <= 1; var9++) {
               int var10 = par1IBlockAccess.getBiomeGenForCoords(par2 + var9, par4 + var8).waterColorMultiplier;
               var5 += (var10 & 0xFF0000) >> 16;
               var6 += (var10 & 0xFF00) >> 8;
               var7 += var10 & 0xFF;
            }
         }

         return (var5 / 9 & 0xFF) << 16 | (var6 / 9 & 0xFF) << 8 | var7 / 9 & 0xFF;
      }
   }

   public static float getFluidHeightPercent(int par0) {
      if (par0 >= 8) {
         par0 = 0;
      }

      return (par0 + 1) / 9.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return par1 != 0 && par1 != 1 ? this.theIcon[1] : this.theIcon[0];
   }

   protected int getFlowDecay(World par1World, int par2, int par3, int par4) {
      return par1World.getBlockMaterial(par2, par3, par4) == this.blockMaterial ? par1World.getBlockMetadata(par2, par3, par4) : -1;
   }

   protected int getEffectiveFlowDecay(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      if (par1IBlockAccess.getBlockMaterial(par2, par3, par4) != this.blockMaterial) {
         return -1;
      } else {
         int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
         if (var5 >= 8) {
            var5 = 0;
         }

         return var5;
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
   public boolean canCollideCheck(int par1, boolean par2) {
      return par2 && par1 == 0;
   }

   @Override
   public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      Material var6 = par1IBlockAccess.getBlockMaterial(par2, par3, par4);
      return var6 == this.blockMaterial
         ? false
         : (par5 == 1 ? true : (var6 == Material.ice ? false : super.isBlockSolid(par1IBlockAccess, par2, par3, par4, par5)));
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   @Override
   public int getRenderType() {
      return 4;
   }

   @Override
   public int idDropped(int par1, Random par2Random, int par3) {
      return 0;
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return 0;
   }

   @Override
   public void velocityToAddToEntity(World par1World, int par2, int par3, int par4, Entity par5Entity, Vec3 par6Vec3) {
      Vec3 var7 = this.getFlowVector(par1World, par2, par3, par4);
      par6Vec3.xCoord = par6Vec3.xCoord + var7.xCoord;
      par6Vec3.yCoord = par6Vec3.yCoord + var7.yCoord;
      par6Vec3.zCoord = par6Vec3.zCoord + var7.zCoord;
   }

   @Override
   public int tickRate(World par1World) {
      return this.blockMaterial == Material.water ? 5 : (this.blockMaterial == Material.lava ? (par1World.provider.hasNoSky ? 10 : 30) : 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int var5 = par1IBlockAccess.getLightBrightnessForSkyBlocks(par2, par3, par4, 0);
      int var6 = par1IBlockAccess.getLightBrightnessForSkyBlocks(par2, par3 + 1, par4, 0);
      int var7 = var5 & 0xFF;
      int var8 = var6 & 0xFF;
      int var9 = var5 >> 16 & 0xFF;
      int var10 = var6 >> 16 & 0xFF;
      return (var7 > var8 ? var7 : var8) | (var9 > var10 ? var9 : var10) << 16;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getBlockBrightness(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      float var5 = par1IBlockAccess.getLightBrightness(par2, par3, par4);
      float var6 = par1IBlockAccess.getLightBrightness(par2, par3 + 1, par4);
      return var5 > var6 ? var5 : var6;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderBlockPass() {
      return this.blockMaterial == Material.water ? 1 : 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (this.blockMaterial == Material.water) {
         if (par5Random.nextInt(10) == 0) {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);
            if (var6 <= 0 || var6 >= 8) {
               par1World.spawnParticle("suspended", par2 + par5Random.nextFloat(), par3 + par5Random.nextFloat(), par4 + par5Random.nextFloat(), 0.0, 0.0, 0.0);
            }
         }

         for (int var6 = 0; var6 < 0; var6++) {
            int var7 = par5Random.nextInt(4);
            int var8 = par2;
            int var9 = par4;
            if (var7 == 0) {
               var8 = par2 - 1;
            }

            if (var7 == 1) {
               var8++;
            }

            if (var7 == 2) {
               var9 = par4 - 1;
            }

            if (var7 == 3) {
               var9++;
            }

            if (par1World.getBlockMaterial(var8, par3, var9) == Material.air
               && (par1World.getBlockMaterial(var8, par3 - 1, var9).blocksMovement() || par1World.getBlockMaterial(var8, par3 - 1, var9).isLiquid())) {
               float var10 = 0.0625F;
               double var11 = par2 + par5Random.nextFloat();
               double var13 = par3 + par5Random.nextFloat();
               double var15 = par4 + par5Random.nextFloat();
               if (var7 == 0) {
                  var11 = par2 - var10;
               }

               if (var7 == 1) {
                  var11 = par2 + 1 + var10;
               }

               if (var7 == 2) {
                  var15 = par4 - var10;
               }

               if (var7 == 3) {
                  var15 = par4 + 1 + var10;
               }

               double var17 = 0.0;
               double var19 = 0.0;
               if (var7 == 0) {
                  var17 = -var10;
               }

               if (var7 == 1) {
                  var17 = var10;
               }

               if (var7 == 2) {
                  var19 = -var10;
               }

               if (var7 == 3) {
                  var19 = var10;
               }

               par1World.spawnParticle("splash", var11, var13, var15, var17, 0.0, var19);
            }
         }
      }

      if (this.blockMaterial == Material.water && par5Random.nextInt(64) == 0) {
         int var6 = par1World.getBlockMetadata(par2, par3, par4);
         if (var6 > 0 && var6 < 8) {
            par1World.playSound(
               par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "liquid.water", par5Random.nextFloat() * 0.25F + 0.75F, par5Random.nextFloat() * 1.0F + 0.5F, false
            );
         }
      }

      if (this.blockMaterial == Material.lava
         && par1World.getBlockMaterial(par2, par3 + 1, par4) == Material.air
         && !par1World.isBlockOpaqueCube(par2, par3 + 1, par4)) {
         if (par5Random.nextInt(100) == 0) {
            double var21 = par2 + par5Random.nextFloat();
            double var22 = par3 + this.maxY;
            double var23 = par4 + par5Random.nextFloat();
            par1World.spawnParticle("lava", var21, var22, var23, 0.0, 0.0, 0.0);
            par1World.playSound(var21, var22, var23, "liquid.lavapop", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
         }

         if (par5Random.nextInt(200) == 0) {
            par1World.playSound(par2, par3, par4, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
         }
      }

      if (par5Random.nextInt(10) == 0
         && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)
         && !par1World.getBlockMaterial(par2, par3 - 2, par4).blocksMovement()) {
         double var21 = par2 + par5Random.nextFloat();
         double var22 = par3 - 1.05;
         double var23 = par4 + par5Random.nextFloat();
         if (this.blockMaterial == Material.water) {
            par1World.spawnParticle("dripWater", var21, var22, var23, 0.0, 0.0, 0.0);
         } else {
            par1World.spawnParticle("dripLava", var21, var22, var23, 0.0, 0.0, 0.0);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public static double getFlowDirection(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, Material par4Material) {
      Vec3 var5 = null;
      if (par4Material == Material.water) {
         var5 = Block.waterMoving.getFlowVector(par0IBlockAccess, par1, par2, par3);
      }

      if (par4Material == Material.lava) {
         var5 = Block.lavaMoving.getFlowVector(par0IBlockAccess, par1, par2, par3);
      }

      return var5.xCoord == 0.0 && var5.zCoord == 0.0 ? -1000.0 : Math.atan2(var5.zCoord, var5.xCoord) - (Math.PI / 2);
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      this.checkForHarden(par1World, par2, par3, par4);
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      this.checkForHarden(par1World, par2, par3, par4);
   }

   private void checkForHarden(World par1World, int par2, int par3, int par4) {
      if (par1World.getBlockId(par2, par3, par4) == this.blockID && this.blockMaterial == Material.lava) {
         boolean var5 = false;
         if (var5 || par1World.getBlockMaterial(par2, par3, par4 - 1) == Material.water) {
            var5 = true;
         }

         if (var5 || par1World.getBlockMaterial(par2, par3, par4 + 1) == Material.water) {
            var5 = true;
         }

         if (var5 || par1World.getBlockMaterial(par2 - 1, par3, par4) == Material.water) {
            var5 = true;
         }

         if (var5 || par1World.getBlockMaterial(par2 + 1, par3, par4) == Material.water) {
            var5 = true;
         }

         if (var5 || par1World.getBlockMaterial(par2, par3 + 1, par4) == Material.water) {
            var5 = true;
         }

         if (var5) {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);
            if (var6 == 0) {
               par1World.setBlock(par2, par3, par4, Block.obsidian.blockID);
            } else if (var6 <= 4) {
               par1World.setBlock(par2, par3, par4, BTWBlocks.lavaPillow.blockID);
            }

            this.triggerLavaMixEffects(par1World, par2, par3, par4);
         }
      }
   }

   protected void triggerLavaMixEffects(World par1World, int par2, int par3, int par4) {
      par1World.playSoundEffect(
         par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F
      );

      for (int var5 = 0; var5 < 8; var5++) {
         par1World.spawnParticle("largesmoke", par2 + Math.random(), par3 + 1.2, par4 + Math.random(), 0.0, 0.0, 0.0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      if (this.blockMaterial == Material.lava) {
         this.theIcon = new Icon[]{par1IconRegister.registerIcon("lava"), par1IconRegister.registerIcon("lava_flow")};
      } else {
         this.theIcon = new Icon[]{par1IconRegister.registerIcon("water"), par1IconRegister.registerIcon("water_flow")};
      }
   }

   @Environment(EnvType.CLIENT)
   public static Icon func_94424_b(String par0Str) {
      return par0Str == "water"
         ? Block.waterMoving.theIcon[0]
         : (
            par0Str == "water_flow"
               ? Block.waterMoving.theIcon[1]
               : (par0Str == "lava" ? Block.lavaMoving.theIcon[0] : (par0Str == "lava_flow" ? Block.lavaMoving.theIcon[1] : null))
         );
   }

   @Override
   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   private Vec3 addFlowVectorAtTarget(IBlockAccess iBlockAccess, int i, int j, int k, int iFlowDecay, int iTempI, int iTempK, Vec3 flowVec) {
      int iTempDecay = this.getEffectiveFlowDecay(iBlockAccess, iTempI, j, iTempK);
      if (iTempDecay < 0) {
         if (!iBlockAccess.getBlockMaterial(iTempI, j, iTempK).blocksMovement()) {
            iTempDecay = this.getEffectiveFlowDecay(iBlockAccess, iTempI, j - 1, iTempK);
            if (iTempDecay >= 0) {
               int iDeltaDecay = iTempDecay - (iFlowDecay - 8);
               return flowVec.addVector((iTempI - i) * iDeltaDecay, 0.0, (iTempK - k) * iDeltaDecay);
            }
         }
      } else if (iTempDecay >= 0) {
         int iDeltaDecay = iTempDecay - iFlowDecay;
         return flowVec.addVector((iTempI - i) * iDeltaDecay, 0.0, (iTempK - k) * iDeltaDecay);
      }

      return flowVec;
   }

   private Vec3 getFlowVector(IBlockAccess iBlockAccess, int i, int j, int k) {
      Vec3 flowVec = iBlockAccess.getWorldVec3Pool().getVecFromPool(0.0, 0.0, 0.0);
      int iFlowDecay = this.getEffectiveFlowDecay(iBlockAccess, i, j, k);
      flowVec = this.addFlowVectorAtTarget(iBlockAccess, i, j, k, iFlowDecay, i - 1, k, flowVec);
      flowVec = this.addFlowVectorAtTarget(iBlockAccess, i, j, k, iFlowDecay, i, k - 1, flowVec);
      flowVec = this.addFlowVectorAtTarget(iBlockAccess, i, j, k, iFlowDecay, i + 1, k, flowVec);
      flowVec = this.addFlowVectorAtTarget(iBlockAccess, i, j, k, iFlowDecay, i, k + 1, flowVec);
      if (iBlockAccess.getBlockMetadata(i, j, k) >= 8
         && (
            this.isBlockSolid(iBlockAccess, i, j, k - 1, 2)
               || this.isBlockSolid(iBlockAccess, i, j, k + 1, 3)
               || this.isBlockSolid(iBlockAccess, i - 1, j, k, 4)
               || this.isBlockSolid(iBlockAccess, i + 1, j, k, 5)
               || this.isBlockSolid(iBlockAccess, i, j + 1, k - 1, 2)
               || this.isBlockSolid(iBlockAccess, i, j + 1, k + 1, 3)
               || this.isBlockSolid(iBlockAccess, i - 1, j + 1, k, 4)
               || this.isBlockSolid(iBlockAccess, i + 1, j + 1, k, 5)
         )) {
         flowVec = flowVec.normalize().addVector(0.0, -6.0, 0.0);
      }

      return flowVec.normalize();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      Material material = blockAccess.getBlockMaterial(i, j, k);
      if (material == this.blockMaterial || iSide != 1 && (material == Material.ice || !super.shouldSideBeRendered(blockAccess, i, j, k, iSide))) {
         return false;
      } else {
         isAnySideBeingRendered = true;
         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockFluids(this, i, j, k);
   }
}
