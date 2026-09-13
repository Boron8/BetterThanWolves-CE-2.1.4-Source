package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class GrassSlabBlock extends AttachedSlabBlock {
   @Environment(EnvType.CLIENT)
   private boolean hasSnowOnTop;
   @Environment(EnvType.CLIENT)
   public static boolean secondPass;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassTop;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassTopSparse;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassTopSparseDirt;
   @Environment(EnvType.CLIENT)
   private Icon iconSnowSide;
   @Environment(EnvType.CLIENT)
   private Icon iconSnowSideHalf;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassSideOverlay;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassSideOverlayHalf;

   public GrassSlabBlock(int blockID) {
      super(blockID, Material.ground);
      this.c(0.5F);
      this.setShovelsEffectiveOn(true);
      this.a(Block.soundGrassFootstep);
      this.c("fcBlockSlabDirt");
      this.b(true);
      this.a(CreativeTabs.tabBlock);
      this.c("fcBlockGrassSlab");
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (!GrassBlock.canGrassSurviveAtLocation(world, x, y, z)) {
         this.revertToDirt(world, x, y, z);
      } else if (GrassBlock.canGrassSpreadFromLocation(world, x, y, z)) {
         if (rand.nextFloat() <= 0.8F) {
            GrassBlock.checkForGrassSpreadFromLocation(world, x, y, z);
         }

         if (this.isSparse(world, x, y, z) && rand.nextInt(12) == 0) {
            this.setFullyGrown(world, x, y, z);
         }
      }
   }

   @Override
   public int idDropped(int metadata, Random random, int fortuneModifier) {
      return BTWBlocks.looseDirtSlab.blockID;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int x, int y, int z, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, x, y, z, BTWItems.dirtPile.itemID, 3, 0, fChanceOfDrop);
      return true;
   }

   @Override
   protected void onAnchorBlockLost(World world, int i, int j, int k) {
      world.setBlock(i, j, k, BTWBlocks.looseDirtSlab.blockID, world.getBlockMetadata(i, j, k) & 3, 2);
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return Block.grass.blockID;
   }

   @Override
   public boolean attemptToCombineWithFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
      if (entity.blockID == BTWBlocks.looseDirtSlab.blockID && !this.getIsUpsideDown(world, x, y, z)) {
         world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
         return true;
      } else {
         return super.attemptToCombineWithFallingEntity(world, x, y, z, entity);
      }
   }

   @Override
   public boolean canBePistonShoveled(World world, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int x, int y, int z, EntityAnimal animal) {
      return !this.isSparse(blockAccess, x, y, z) || animal.isStarving() || animal.getDisruptsEarthOnGraze();
   }

   @Override
   public void onGrazed(World world, int x, int y, int z, EntityAnimal animal) {
      if (!animal.getDisruptsEarthOnGraze()) {
         if (this.isSparse(world, x, y, z)) {
            world.setBlockWithNotify(x, y, z, BTWBlocks.dirtSlab.blockID);
         } else {
            this.setSparse(world, x, y, z);
         }
      } else {
         world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirtSlab.blockID);
         this.notifyNeighborsBlockDisrupted(world, x, y, z);
      }
   }

   @Override
   public void onVegetationAboveGrazed(World world, int x, int y, int z, EntityAnimal animal) {
      if (animal.getDisruptsEarthOnGraze()) {
         world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
         this.notifyNeighborsBlockDisrupted(world, x, y, z);
      }
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      super.onBlockDestroyedWithImproperTool(world, player, i, j, k, iMetadata);
      this.onDirtSlabDugWithImproperTool(world, i, j, k, this.getIsUpsideDown(iMetadata));
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      super.a(world, i, j, k, explosion);
      this.onDirtSlabDugWithImproperTool(world, i, j, k, this.getIsUpsideDown(world, i, j, k));
   }

   public void revertToDirt(World world, int x, int y, int z) {
      boolean isUpsideDown = this.getIsUpsideDown(world, x, y, z);
      world.setBlockWithNotify(x, y, z, BTWBlocks.dirtSlab.blockID);
      BTWBlocks.dirtSlab.setSubtype(world, x, y, z, 0);
      BTWBlocks.dirtSlab.setIsUpsideDown(world, x, y, z, isUpsideDown);
   }

   public boolean isSparse(IBlockAccess blockAccess, int x, int y, int z) {
      return this.isSparse(blockAccess.getBlockMetadata(x, y, z));
   }

   public boolean isSparse(int metadata) {
      return (metadata & -1) == 2;
   }

   public void setSparse(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      world.setBlockMetadataWithNotify(x, y, z, metadata | 2);
   }

   public void setFullyGrown(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      world.setBlockMetadataWithNotify(x, y, z, metadata & 1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = Block.dirt.blockIcon;
      this.iconGrassTop = register.registerIcon("grass_top");
      this.iconGrassTopSparse = register.registerIcon("fcBlockGrassSparse");
      this.iconGrassTopSparseDirt = register.registerIcon("fcBlockGrassSparseDirt");
      this.iconSnowSide = register.registerIcon("snow_side");
      this.iconSnowSideHalf = register.registerIcon("FCBlockSlabDirt_grass_snow_side");
      this.iconGrassSideOverlay = register.registerIcon("grass_side_overlay");
      this.iconGrassSideOverlayHalf = register.registerIcon("FCBlockSlabDirt_grass_side_overlay");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
      if (!this.hasSnowOnTop && secondPass) {
         if (ColorizeBlock.colorizeBlock(this, blockAccess, x, y, z)) {
            return ColorizeBlock.blockColor;
         } else {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int i = -1; i <= 1; i++) {
               for (int k = -1; k <= 1; k++) {
                  int iBiomeGrassColor = blockAccess.getBiomeGenForCoords(x + i, z + k).getBiomeGrassColor();
                  red += (iBiomeGrassColor & 0xFF0000) >> 16;
                  green += (iBiomeGrassColor & 0xFF00) >> 8;
                  blue += iBiomeGrassColor & 0xFF;
               }
            }

            return (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
         }
      } else {
         return 16777215;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side) {
      BlockPos pos = new BlockPos(neighborX, neighborY, neighborZ, Facing.oppositeSide[side]);
      if (!secondPass) {
         if (side == 1 && !this.isSparse(blockAccess, pos.x, pos.y, pos.z) && !this.hasSnowOnTop) {
         }
      } else {
         if (side == 0) {
            return false;
         }

         if (side >= 2 && this.hasSnowOnTop) {
            return false;
         }
      }

      return super.a(blockAccess, neighborX, neighborY, neighborZ, side);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
      if (!secondPass) {
         if (side == 1 && this.isSparse(blockAccess, x, y, z)) {
            return this.iconGrassTopSparseDirt;
         } else if (side > 1 && this.hasSnowOnTop) {
            Icon betterGrassIcon = RenderBlocksUtils.getGrassTexture(this, blockAccess, x, y, z, side, this.iconGrassTop);
            if (betterGrassIcon != null && betterGrassIcon != this.iconGrassTop && betterGrassIcon != this.iconGrassTopSparse) {
               return betterGrassIcon;
            } else {
               return this.getIsUpsideDown(blockAccess, x, y, z) ? this.iconSnowSide : this.iconSnowSideHalf;
            }
         } else {
            return Block.dirt.getBlockTextureFromSide(side);
         }
      } else {
         return this.getBlockTextureSecondPass(blockAccess, x, y, z, side);
      }
   }

   @Environment(EnvType.CLIENT)
   public Icon getBlockTextureSecondPass(IBlockAccess blockAccess, int x, int y, int z, int side) {
      Icon topIcon;
      if (this.isSparse(blockAccess, x, y, z)) {
         topIcon = this.iconGrassTopSparse;
      } else {
         topIcon = this.iconGrassTop;
      }

      Icon betterGrassIcon = RenderBlocksUtils.getGrassTexture(this, blockAccess, x, y, z, side, topIcon);
      if (betterGrassIcon != null) {
         return betterGrassIcon;
      } else if (side == 1) {
         return topIcon;
      } else if (side > 1) {
         return this.getIsUpsideDown(blockAccess, x, y, z) ? this.iconGrassSideOverlay : this.iconGrassSideOverlayHalf;
      } else {
         return null;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks render, int x, int y, int z) {
      this.hasSnowOnTop = this.isSnowCoveringTopSurface(render.blockAccess, x, y, z);
      AxisAlignedBB bounds = this.getBlockBoundsFromPoolBasedOnState(render.blockAccess, x, y, z);
      render.setRenderBounds(bounds);
      return render.renderStandardBlock(this, x, y, z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks render, int x, int y, int z, boolean firstPassResult) {
      secondPass = true;
      AxisAlignedBB bounds = this.getBlockBoundsFromPoolBasedOnState(render.blockAccess, x, y, z);
      render.setRenderBounds(bounds);
      render.renderStandardBlock(this, x, y, z);
      secondPass = false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int itemDamage, float brightness) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      int var4 = 1;
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, BTWBlocks.dirtSlab, -0.5F, -0.5F, -0.5F, var4 << 1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int blockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(blockID, 1, 0));
   }
}
