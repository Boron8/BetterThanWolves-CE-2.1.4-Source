package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class MyceliumSlabBlock extends AttachedSlabBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconTopSparse;
   @Environment(EnvType.CLIENT)
   private Icon iconBottom;
   @Environment(EnvType.CLIENT)
   private Icon iconSide;
   @Environment(EnvType.CLIENT)
   private Icon iconSideHalf;
   @Environment(EnvType.CLIENT)
   private Icon iconSideSnow;
   @Environment(EnvType.CLIENT)
   private Icon iconSideHalfSnow;

   public MyceliumSlabBlock(int iBlockID) {
      super(iBlockID, Material.grass);
      this.c(0.6F);
      this.setShovelsEffectiveOn();
      this.a(Block.soundGrassFootstep);
      this.c("fcBlockMyceliumSlab");
      this.b(true);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (!MyceliumBlock.canMyceliumSurviveAtLocation(world, x, y, z)) {
         this.revertToDirt(world, x, y, z);
      } else {
         MyceliumBlock.checkForMyceliumSpreadFromLocation(world, x, y, z);
         if (this.isSparse(world, x, y, z) && rand.nextInt(4) == 0) {
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
   public boolean attemptToCombineWithFallingEntity(World world, int x, int y, int z, EntityFallingSand entity) {
      if (entity.blockID == BTWBlocks.looseDirtSlab.blockID && !this.getIsUpsideDown(world, x, y, z)) {
         world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
         return true;
      } else {
         return super.attemptToCombineWithFallingEntity(world, x, y, z, entity);
      }
   }

   @Override
   protected void onAnchorBlockLost(World world, int x, int y, int z) {
      world.setBlock(x, y, z, BTWBlocks.looseDirtSlab.blockID, world.getBlockMetadata(x, y, z) & 3, 2);
   }

   @Override
   public int getCombinedBlockID(int metadata) {
      return Block.mycelium.blockID;
   }

   @Override
   public boolean canBePistonShoveled(World world, int x, int y, int z) {
      return true;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   public boolean canMobsSpawnOn(World world, int x, int y, int z) {
      return false;
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int x, int y, int z, EntityAnimal animal) {
      return this.isSparse(blockAccess, x, y, z) && !animal.isStarving() && !animal.getDisruptsEarthOnGraze() ? false : animal.canGrazeMycelium();
   }

   @Override
   public void onGrazed(World world, int x, int y, int z, EntityAnimal animal) {
      if (!animal.getDisruptsEarthOnGraze()) {
         if (this.isSparse(world, x, y, z)) {
            world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
         } else {
            this.setSparse(world, x, y, z);
         }
      } else {
         world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
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
      world.setBlockMetadata(x, y, z, metadata | 2);
   }

   public void setFullyGrown(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      world.setBlockMetadata(x, y, z, metadata & 1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockMyceliumSlab_top");
      this.iconTopSparse = register.registerIcon("fcBlockMyceliumSparse");
      this.iconBottom = register.registerIcon("fcBlockMyceliumSlab_bottom");
      this.iconSide = register.registerIcon("fcBlockMyceliumSlab_side");
      this.iconSideHalf = register.registerIcon("fcBlockMyceliumSlab_side_half");
      this.iconSideSnow = register.registerIcon("snow_side");
      this.iconSideHalfSnow = register.registerIcon("FCBlockSlabDirt_grass_snow_side");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int metadata) {
      if (iSide < 2) {
         if (iSide == 0) {
            return this.iconBottom;
         } else {
            Icon topIcon;
            if (this.isSparse(metadata)) {
               topIcon = this.iconTopSparse;
            } else {
               topIcon = this.blockIcon;
            }

            return topIcon;
         }
      } else {
         return this.getIsUpsideDown(metadata) ? this.iconSide : this.iconSideHalf;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
      int metadata = blockAccess.getBlockMetadata(x, y, z);
      Icon topIcon;
      if (this.isSparse(blockAccess, x, y, z)) {
         topIcon = this.iconTopSparse;
      } else {
         topIcon = this.blockIcon;
      }

      if (side > 1 && this.isSnowCoveringTopSurface(blockAccess, x, y, z)) {
         Icon betterGrassIcon = RenderBlocksUtils.getGrassTexture(this, blockAccess, x, y, z, side, topIcon);
         if (betterGrassIcon != null) {
            return betterGrassIcon;
         } else {
            return this.getIsUpsideDown(metadata) ? this.iconSideSnow : this.iconSideHalfSnow;
         }
      } else {
         Icon betterGrassIcon = RenderBlocksUtils.getGrassTexture(Block.mycelium, blockAccess, x, y, z, side, topIcon);
         return betterGrassIcon != null ? betterGrassIcon : this.getIcon(side, metadata);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      super.b(world, i, j, k, rand);
      if (rand.nextInt(10) == 0) {
         double dYParticle = j + 0.6;
         if (this.getIsUpsideDown(world, i, j, k)) {
            dYParticle += 0.5;
         }

         world.spawnParticle("townaura", i + rand.nextDouble(), dYParticle, k + rand.nextDouble(), 0.0, 0.0, 0.0);
      }
   }
}
