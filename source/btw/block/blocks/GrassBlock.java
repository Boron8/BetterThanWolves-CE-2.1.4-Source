package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.items.HoeItem;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockGrass;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class GrassBlock extends BlockGrass {
   public static final int SPREAD_LIGHT_LEVEL = 9;
   public static final int SURVIVE_LIGHT_LEVEL = 9;
   public static final float GROWTH_CHANCE = 0.8F;
   public static final int SELF_GROWTH_CHANCE = 12;
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
   private Icon iconGrassSideOverlay;

   public GrassBlock(int blockID) {
      super(blockID);
      this.c(0.6F);
      this.setShovelsEffectiveOn();
      this.setHoesEffectiveOn();
      this.a(i);
      this.c("grass");
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (!canGrassSurviveAtLocation(world, x, y, z)) {
         world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
      } else if (canGrassSpreadFromLocation(world, x, y, z)) {
         if (rand.nextFloat() <= 0.8F) {
            checkForGrassSpreadFromLocation(world, x, y, z);
         }

         if (this.isSparse(world, x, y, z) && rand.nextInt(12) == 0) {
            this.setFullyGrown(world, x, y, z);
         }
      }
   }

   @Override
   protected ItemStack createStackedBlock(int metadata) {
      return this.isSparse(metadata) ? new ItemStack(Block.dirt) : new ItemStack(this);
   }

   @Override
   public int idDropped(int metadata, Random rand, int fortuneModifier) {
      return BTWBlocks.looseDirt.blockID;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int x, int y, int z, int metadata, float chanceOfDrop) {
      this.dropItemsIndividually(world, x, y, z, BTWItems.dirtPile.itemID, 6, 0, chanceOfDrop);
      return true;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int x, int y, int z, int metadata) {
      super.onBlockDestroyedWithImproperTool(world, player, x, y, z, metadata);
      this.onDirtDugWithImproperTool(world, x, y, z);
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
      super.a(world, x, y, z, explosion);
      this.onDirtDugWithImproperTool(world, x, y, z);
   }

   @Override
   protected void onNeighborDirtDugWithImproperTool(World world, int x, int y, int z, int toFacing) {
      if (toFacing == 0) {
         world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
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
   public boolean canReedsGrowOnBlock(World world, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean canSaplingsGrowOnBlock(World world, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean canWildVegetationGrowOnBlock(World world, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean getCanBlightSpreadToBlock(World world, int x, int y, int z, int blightLevel) {
      return true;
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int x, int y, int z) {
      return stack != null && stack.getItem() instanceof HoeItem;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int x, int y, int z, int fromSide) {
      world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
      if (!world.isRemote && world.rand.nextInt(25) == 0) {
         ItemUtils.ejectStackFromBlockTowardsFacing(world, x, y, z, new ItemStack(BTWItems.hempSeeds), fromSide);
      }

      return true;
   }

   @Override
   public boolean getCanGrassSpreadToBlock(World world, int x, int y, int z) {
      return this.isSparse(world, x, y, z);
   }

   @Override
   public boolean spreadGrassToBlock(World world, int x, int y, int z) {
      if (this.isSparse(world, x, y, z)) {
         this.setFullyGrown(world, x, y, z);
         return true;
      } else {
         return false;
      }
   }

   public static boolean canGrassSurviveAtLocation(World world, int x, int y, int z) {
      int blockAboveID = world.getBlockId(x, y + 1, z);
      Block blockAbove = Block.blocksList[blockAboveID];
      int blockAboveMaxNaturalLight = world.getBlockNaturalLightValueMaximum(x, y + 1, z);
      int blockAboveCurrentNaturalLight = blockAboveMaxNaturalLight - world.skylightSubtracted;
      return blockAboveMaxNaturalLight >= 9
         && Block.lightOpacity[blockAboveID] <= 2
         && (blockAbove == null || blockAbove.getCanGrassGrowUnderBlock(world, x, y + 1, z, false));
   }

   public static boolean canGrassSpreadFromLocation(World world, int x, int y, int z) {
      int blockAboveID = world.getBlockId(x, y + 1, z);
      Block blockAbove = Block.blocksList[blockAboveID];
      int blockAboveMaxNaturalLight = world.getBlockNaturalLightValueMaximum(x, y + 1, z);
      int blockAboveCurrentNaturalLight = blockAboveMaxNaturalLight - world.skylightSubtracted;
      int blockAboveCurrentBlockLight = world.getBlockLightValue(x, y + 1, z);
      int currentLight = Math.max(blockAboveCurrentNaturalLight, blockAboveCurrentBlockLight);
      return currentLight >= 9;
   }

   public static void checkForGrassSpreadFromLocation(World world, int x, int y, int z) {
      if (world.provider.dimensionId != 1 && !GroundCoverBlock.isGroundCoverRestingOnBlock(world, x, y, z)) {
         int i = x + world.rand.nextInt(3) - 1;
         int j = y + world.rand.nextInt(4) - 2;
         int k = z + world.rand.nextInt(3) - 1;
         Block targetBlock = Block.blocksList[world.getBlockId(i, j, k)];
         if (targetBlock != null) {
            attempToSpreadGrassToLocation(world, i, j, k);
         }
      }
   }

   public static boolean attempToSpreadGrassToLocation(World world, int x, int y, int z) {
      int targetBlockID = world.getBlockId(x, y, z);
      Block targetBlock = Block.blocksList[targetBlockID];
      return canGrassSurviveAtLocation(world, x, y, z)
            && targetBlock.getCanGrassSpreadToBlock(world, x, y, z)
            && Block.lightOpacity[world.getBlockId(x, y + 1, z)] <= 2
            && !GroundCoverBlock.isGroundCoverRestingOnBlock(world, x, y, z)
         ? targetBlock.spreadGrassToBlock(world, x, y, z)
         : false;
   }

   public boolean isSparse(IBlockAccess blockAccess, int x, int y, int z) {
      return this.isSparse(blockAccess.getBlockMetadata(x, y, z));
   }

   public boolean isSparse(int metadata) {
      return metadata == 1;
   }

   public void setSparse(World world, int x, int y, int z) {
      world.setBlockMetadataWithNotify(x, y, z, 1);
   }

   public void setFullyGrown(World world, int x, int y, int z) {
      world.setBlockMetadataWithNotify(x, y, z, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconGrassTop = register.registerIcon("grass_top");
      this.iconSnowSide = register.registerIcon("snow_side");
      this.iconGrassSideOverlay = register.registerIcon("grass_side_overlay");
      this.iconGrassTopSparse = register.registerIcon("fcBlockGrassSparse");
      this.iconGrassTopSparseDirt = register.registerIcon("fcBlockGrassSparseDirt");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
      return !this.hasSnowOnTop && secondPass ? super.colorMultiplier(blockAccess, x, y, z) : 16777215;
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
            return betterGrassIcon != null && betterGrassIcon != this.iconGrassTop && betterGrassIcon != this.iconGrassTopSparse
               ? betterGrassIcon
               : this.iconSnowSide;
         } else {
            return Block.grass.m(side);
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
      } else {
         return side > 1 ? this.iconGrassSideOverlay : null;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks render, int x, int y, int z) {
      this.hasSnowOnTop = this.isSnowCoveringTopSurface(render.blockAccess, x, y, z);
      render.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      return render.renderStandardBlock(this, x, y, z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks render, int x, int y, int z, boolean firstPassResult) {
      secondPass = true;
      render.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      render.renderStandardBlock(this, x, y, z);
      secondPass = false;
   }
}
