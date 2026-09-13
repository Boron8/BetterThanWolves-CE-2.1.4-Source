package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.items.HoeItem;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockMycelium;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class MyceliumBlock extends BlockMycelium {
   @Environment(EnvType.CLIENT)
   private boolean hasSnowOnTop;
   @Environment(EnvType.CLIENT)
   private Icon iconTop;
   @Environment(EnvType.CLIENT)
   private Icon iconTopSparse;
   @Environment(EnvType.CLIENT)
   private Icon iconSnowSide;

   public MyceliumBlock(int blockID) {
      super(blockID);
      this.c(0.6F);
      this.setShovelsEffectiveOn();
      this.setHoesEffectiveOn();
      this.a(i);
      this.c("mycel");
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (!canMyceliumSurviveAtLocation(world, x, y, z)) {
         world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
      } else {
         checkForMyceliumSpreadFromLocation(world, x, y, z);
         if (this.isSparse(world, x, y, z) && rand.nextInt(4) == 0) {
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
   public boolean getCanBlightSpreadToBlock(World world, int x, int y, int z, int blightLevel) {
      return blightLevel >= 2;
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int x, int y, int z) {
      return stack != null && stack.getItem() instanceof HoeItem;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int x, int y, int z, int fromSide) {
      world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
      return true;
   }

   public static boolean canMyceliumSurviveAtLocation(World world, int x, int y, int z) {
      int blockAboveID = world.getBlockId(x, y + 1, z);
      Block blockAbove = Block.blocksList[blockAboveID];
      return Block.lightOpacity[blockAboveID] <= 2 && (blockAbove == null || blockAbove.getCanGrassGrowUnderBlock(world, x, y + 1, z, false));
   }

   public static void checkForMyceliumSpreadFromLocation(World world, int x, int y, int z) {
      if (world.provider.dimensionId != 1 && !GroundCoverBlock.isGroundCoverRestingOnBlock(world, x, y, z)) {
         int i = x + world.rand.nextInt(3) - 1;
         int j = y + world.rand.nextInt(4) - 2;
         int k = z + world.rand.nextInt(3) - 1;
         Block targetBlock = Block.blocksList[world.getBlockId(i, j, k)];
         if (targetBlock != null) {
            attempToSpreadMyceliumToLocation(world, i, j, k);
         }
      }
   }

   public static boolean attempToSpreadMyceliumToLocation(World world, int x, int y, int z) {
      int targetBlockID = world.getBlockId(x, y, z);
      Block targetBlock = Block.blocksList[targetBlockID];
      return targetBlock.getCanMyceliumSpreadToBlock(world, x, y, z)
            && Block.lightOpacity[world.getBlockId(x, y + 1, z)] <= 2
            && !GroundCoverBlock.isGroundCoverRestingOnBlock(world, x, y, z)
         ? targetBlock.spreadMyceliumToBlock(world, x, y, z)
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
      this.iconTop = register.registerIcon("mycel_top");
      this.iconTopSparse = register.registerIcon("fcBlockMyceliumSparse");
      this.iconSnowSide = register.registerIcon("snow_side");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
      Icon topIcon;
      if (this.isSparse(blockAccess, x, y, z)) {
         topIcon = this.iconTopSparse;
      } else {
         topIcon = this.iconTop;
      }

      Icon betterGrassIcon = RenderBlocksUtils.getGrassTexture(this, blockAccess, x, y, z, side, topIcon);
      if (betterGrassIcon != null) {
         return betterGrassIcon;
      } else if (side == 1) {
         return topIcon;
      } else if (side == 0) {
         return Block.dirt.getBlockTextureFromSide(side);
      } else {
         return this.hasSnowOnTop ? this.iconSnowSide : this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
      IBlockAccess blockAccess = renderer.blockAccess;
      this.hasSnowOnTop = this.isSnowCoveringTopSurface(blockAccess, x, y, z);
      renderer.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      return renderer.renderStandardBlock(this, x, y, z);
   }
}
