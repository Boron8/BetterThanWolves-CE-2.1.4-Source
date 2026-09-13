package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StepSound;
import net.minecraft.src.World;

public class DirtSlabBlock extends AttachedSlabBlock {
   public static final int SUBTYPE_DIRT = 0;
   public static final int SUBTYPE_GRASS = 1;
   public static final int SUBTYPE_MYCELIUM = 2;
   public static final int SUBTYPE_PACKED_EARTH = 3;
   public static final int NUM_SUBTYPES = 4;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassSide;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassSideOverlay;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassTop;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassTopItem;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassSideHalf;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassSideOverlayHalf;
   @Environment(EnvType.CLIENT)
   private Icon iconPackedEarth;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassWithSnowSide;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassWithSnowSideHalf;

   public DirtSlabBlock(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.5F);
      this.setShovelsEffectiveOn(true);
      this.a(Block.soundGrassFootstep);
      this.c("fcBlockSlabDirt");
      this.b(true);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      int iSubType = this.getSubtype(world, i, j, k);
      if (iSubType == 1) {
         world.setBlock(i, j, k, BTWBlocks.grassSlab.blockID);
         BTWBlocks.grassSlab.updateTick(world, i, j, k, rand);
      }
   }

   @Override
   public int damageDropped(int iMetadata) {
      int iSubtype = this.getSubtype(iMetadata);
      return iSubtype == 3 ? iSubtype : 0;
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      int iSubtype = this.getSubtype(iMetadata);
      return iSubtype == 3 ? super.a(iMetadata, random, iFortuneModifier) : BTWBlocks.looseDirtSlab.blockID;
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      float fModifier = 1.0F;
      int iSubtype = this.getSubtype(world, i, j, k);
      if (iSubtype == 3) {
         fModifier = 1.2F;
      }

      return fModifier;
   }

   @Override
   public StepSound getStepSound(World world, int i, int j, int k) {
      int iSubtype = this.getSubtype(world, i, j, k);
      return iSubtype != 0 && iSubtype != 3 ? this.stepSound : Block.soundGravelFootstep;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      int iNumDropped = 3;
      if (this.getSubtype(iMetadata) == 3) {
         iNumDropped = 6;
      }

      this.dropItemsIndividually(world, i, j, k, BTWItems.dirtPile.itemID, iNumDropped, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean getCanGrassSpreadToBlock(World world, int i, int j, int k) {
      int iSubType = this.getSubtype(world, i, j, k);
      if (iSubType == 0) {
         Block blockAbove = Block.blocksList[world.getBlockId(i, j + 1, k)];
         boolean bIsUpsideDown = this.getIsUpsideDown(world, i, j, k);
         if (blockAbove == null || blockAbove.getCanGrassGrowUnderBlock(world, i, j + 1, k, !bIsUpsideDown)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean spreadGrassToBlock(World world, int x, int y, int z) {
      boolean isUpsideDown = this.getIsUpsideDown(world, x, y, z);
      world.setBlockWithNotify(x, y, z, BTWBlocks.grassSlab.blockID);
      BTWBlocks.grassSlab.setSparse(world, x, y, z);
      BTWBlocks.grassSlab.setIsUpsideDown(world, x, y, z, isUpsideDown);
      return true;
   }

   @Override
   public boolean getCanMyceliumSpreadToBlock(World world, int i, int j, int k) {
      int iSubType = this.getSubtype(world, i, j, k);
      return iSubType != 0 ? false : !this.getIsUpsideDown(world, i, j, k) || !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j + 1, k, 0);
   }

   @Override
   public boolean spreadMyceliumToBlock(World world, int x, int y, int z) {
      boolean isUpsideDown = this.getIsUpsideDown(world, x, y, z);
      world.setBlockWithNotify(x, y, z, BTWBlocks.myceliumSlab.blockID);
      BTWBlocks.myceliumSlab.setSparse(world, x, y, z);
      BTWBlocks.myceliumSlab.setIsUpsideDown(world, x, y, z, isUpsideDown);
      return true;
   }

   @Override
   public boolean attemptToCombineWithFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (entity.blockID == BTWBlocks.looseDirtSlab.blockID) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         if (this.getSubtype(iMetadata) != 3 && !this.getIsUpsideDown(iMetadata)) {
            world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
            return true;
         }
      }

      return super.attemptToCombineWithFallingEntity(world, i, j, k, entity);
   }

   @Override
   protected void onAnchorBlockLost(World world, int i, int j, int k) {
      if (this.getSubtype(world.getBlockMetadata(i, j, k)) != 3) {
         world.setBlock(i, j, k, BTWBlocks.looseDirtSlab.blockID, world.getBlockMetadata(i, j, k) & 3, 2);
      } else {
         this.dropComponentItemsOnBadBreak(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      int iSubtype = this.getSubtype(iMetadata);
      return iSubtype == 3 ? BTWBlocks.aestheticEarth.blockID : Block.dirt.blockID;
   }

   @Override
   public int getCombinedMetadata(int iMetadata) {
      int iSubtype = this.getSubtype(iMetadata);
      return iSubtype == 3 ? 6 : 0;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   protected ItemStack createStackedBlock(int metadata) {
      int subtype = this.getSubtype(metadata);
      return subtype == 1 ? new ItemStack(BTWBlocks.grassSlab) : new ItemStack(this.blockID, 1, subtype);
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal byAnimal) {
      return this.getSubtype(blockAccess, i, j, k) == 1;
   }

   @Override
   public void onGrazed(World world, int i, int j, int k, EntityAnimal animal) {
      if (!animal.getDisruptsEarthOnGraze()) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.grassSlab.blockID);
         BTWBlocks.grassSlab.setSparse(world, i, j, k);
      } else {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirtSlab.blockID);
         this.notifyNeighborsBlockDisrupted(world, i, j, k);
      }
   }

   @Override
   public void onVegetationAboveGrazed(World world, int i, int j, int k, EntityAnimal animal) {
      if (animal.getDisruptsEarthOnGraze()) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirtSlab.blockID);
         this.notifyNeighborsBlockDisrupted(world, i, j, k);
      }
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      super.onBlockDestroyedWithImproperTool(world, player, i, j, k, iMetadata);
      if (this.getSubtype(iMetadata) != 3) {
         this.onDirtSlabDugWithImproperTool(world, i, j, k, this.getIsUpsideDown(iMetadata));
      }
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      super.a(world, i, j, k, explosion);
      if (this.getSubtype(world, i, j, k) != 3) {
         this.onDirtSlabDugWithImproperTool(world, i, j, k, this.getIsUpsideDown(world, i, j, k));
      }
   }

   @Override
   protected void onNeighborDirtDugWithImproperTool(World world, int i, int j, int k, int iToFacing) {
      int iSubtype = this.getSubtype(world, i, j, k);
      if (iSubtype != 3 && (iSubtype != 1 || iToFacing == 0)) {
         boolean bIsUpsideDown = this.getIsUpsideDown(world, i, j, k);
         if ((!bIsUpsideDown || iToFacing != 0) && (bIsUpsideDown || iToFacing != 1)) {
            world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirtSlab.blockID);
         }
      }
   }

   public int getSubtype(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getSubtype(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getSubtype(int iMetadata) {
      return (iMetadata & -2) >> 1;
   }

   public void setSubtype(World world, int i, int j, int k, int iSubtype) {
      int iMetadata = world.getBlockMetadata(i, j, k) & 1;
      iMetadata |= iSubtype << 1;
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("dirt");
      this.iconGrassSide = register.registerIcon("grass_side");
      this.iconGrassSideOverlay = register.registerIcon("grass_side_overlay");
      this.iconGrassTop = register.registerIcon("grass_top");
      this.iconGrassTopItem = register.registerIcon("fcBlockSlabDirt_grass_top_item");
      this.iconGrassSideHalf = register.registerIcon("FCBlockSlabDirt_grass_side");
      this.iconGrassSideOverlayHalf = register.registerIcon("FCBlockSlabDirt_grass_side_overlay");
      this.iconPackedEarth = register.registerIcon("FCBlockPackedEarth");
      this.iconGrassWithSnowSide = register.registerIcon("snow_side");
      this.iconGrassWithSnowSideHalf = register.registerIcon("FCBlockSlabDirt_grass_snow_side");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide == 1 && this.getSubtype(iMetadata) == 1 ? this.iconGrassTopItem : this.getIconFromMetadata(iSide, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   private Icon getIconFromMetadata(int iSide, int iMetadata) {
      int iSubtype = this.getSubtype(iMetadata);
      if (iSubtype == 1 && iSide != 0) {
         if (iSide != 1) {
            boolean bIsUpsideDown = (iMetadata & 1) > 0;
            return bIsUpsideDown ? this.iconGrassSide : this.iconGrassSideHalf;
         } else {
            return this.iconGrassTop;
         }
      } else {
         return iSubtype == 3 ? this.iconPackedEarth : this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      int iSubtype = this.getSubtype(iMetadata);
      if (iSubtype == 1 && iSide > 1 && this.isSnowCoveringTopSurface(blockAccess, i, j, k)) {
         Icon betterGrassIcon = RenderBlocksUtils.getGrassTexture(Block.grass, blockAccess, i, j, k, iSide, this.iconGrassTop);
         if (betterGrassIcon != null) {
            return betterGrassIcon;
         } else {
            return this.getIsUpsideDown(iMetadata) ? this.iconGrassWithSnowSide : this.iconGrassWithSnowSideHalf;
         }
      } else {
         Icon icon = RenderBlocksUtils.getGrassTexture(Block.grass, blockAccess, i, j, k, iSide, this.iconGrassTop);
         return icon != null && iSubtype == 1 && iSide >= 1 ? icon : this.getIconFromMetadata(iSide, iMetadata);
      }
   }

   @Environment(EnvType.CLIENT)
   public Icon getSideOverlayTexture(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsUpsideDown(blockAccess, i, j, k) ? this.iconGrassSideOverlayHalf : this.iconGrassSideOverlay;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 3));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      int iSubtype = BTWBlocks.dirtSlab.getSubtype(blockAccess, i, j, k);
      if (iSubtype == 1 && !this.isSnowCoveringTopSurface(blockAccess, i, j, k)) {
         int iColorMultiplier = this.colorMultiplier(blockAccess, i, j, k);
         float fRed = (iColorMultiplier >> 16 & 0xFF) / 255.0F;
         float fGreen = (iColorMultiplier >> 8 & 0xFF) / 255.0F;
         float fBlue = (iColorMultiplier & 0xFF) / 255.0F;
         return Minecraft.isAmbientOcclusionEnabled()
            ? renderer.renderGrassBlockWithAmbientOcclusion(this, i, j, k, fRed, fGreen, fBlue, this.getSideOverlayTexture(blockAccess, i, j, k))
            : renderer.renderGrassBlockWithColorMultiplier(this, i, j, k, fRed, fGreen, fBlue, this.getSideOverlayTexture(blockAccess, i, j, k));
      } else {
         return renderer.renderStandardBlock(this, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, iItemDamage << 1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = this.getSubtype(blockAccess, i, j, k);
      if (iSubtype == 1 && !this.isSnowCoveringTopSurface(blockAccess, i, j, k)) {
         if (ColorizeBlock.colorizeBlock(this, blockAccess, i, j, k)) {
            return ColorizeBlock.blockColor;
         } else {
            int iRed = 0;
            int iGreen = 0;
            int iBlue = 0;

            for (int iKOffset = -1; iKOffset <= 1; iKOffset++) {
               for (int iIOffset = -1; iIOffset <= 1; iIOffset++) {
                  int iBiomeGrassColor = blockAccess.getBiomeGenForCoords(i + iIOffset, k + iKOffset).getBiomeGrassColor();
                  iRed += (iBiomeGrassColor & 0xFF0000) >> 16;
                  iGreen += (iBiomeGrassColor & 0xFF00) >> 8;
                  iBlue += iBiomeGrassColor & 0xFF;
               }
            }

            return (iRed / 9 & 0xFF) << 16 | (iGreen / 9 & 0xFF) << 8 | iBlue / 9 & 0xFF;
         }
      } else {
         return super.c(blockAccess, i, j, k);
      }
   }
}
