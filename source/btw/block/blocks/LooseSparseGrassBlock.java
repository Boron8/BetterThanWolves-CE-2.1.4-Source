package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.item.items.HoeItem;
import btw.item.util.ItemUtils;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LooseSparseGrassBlock extends FallingFullBlock {
   @Environment(EnvType.CLIENT)
   private boolean hasSnowOnTop;
   @Environment(EnvType.CLIENT)
   public static boolean secondPass;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassTopSparse;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassTopSparseDirt;
   @Environment(EnvType.CLIENT)
   private Icon iconSnowSide;
   @Environment(EnvType.CLIENT)
   private Icon iconGrassSideOverlay;

   public LooseSparseGrassBlock(int blockID) {
      super(blockID, Material.grass);
      this.c(0.5F);
      this.setShovelsEffectiveOn();
      this.setHoesEffectiveOn();
      this.a(Block.soundGravelFootstep);
      this.c("fcBlockGrassSparseLoose");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      super.a(world, x, y, z, rand);
      if (!GrassBlock.canGrassSurviveAtLocation(world, x, y, z)) {
         world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
      } else if (GrassBlock.canGrassSpreadFromLocation(world, x, y, z)) {
         if (rand.nextFloat() <= 0.8F) {
            GrassBlock.checkForGrassSpreadFromLocation(world, x, y, z);
         }

         if (rand.nextInt(12) == 0) {
            world.setBlockWithNotify(x, y, z, Block.grass.blockID);
         }
      }
   }

   @Override
   protected ItemStack createStackedBlock(int metadata) {
      return new ItemStack(BTWBlocks.looseDirt);
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
   public boolean canBePistonShoveled(World world, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int x, int y, int z, EntityAnimal animal) {
      return animal.isStarving();
   }

   @Override
   public void onGrazed(World world, int x, int y, int z, EntityAnimal animal) {
      world.setBlockWithNotify(x, y, z, BTWBlocks.looseDirt.blockID);
      this.notifyNeighborsBlockDisrupted(world, x, y, z);
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

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockGrassSparseLoose_side");
      this.iconSnowSide = register.registerIcon("snow_side");
      this.iconGrassSideOverlay = register.registerIcon("grass_side_overlay");
      this.iconGrassTopSparse = register.registerIcon("fcBlockGrassSparseLoose");
      this.iconGrassTopSparseDirt = register.registerIcon("fcBlockGrassSparseDirtLoose");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
      return Block.grass.colorMultiplier(blockAccess, x, y, z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBlockColor() {
      return Block.grass.getBlockColor();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderColor(int par1) {
      return Block.grass.getRenderColor(par1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side) {
      if (secondPass) {
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
         if (side == 1) {
            return this.iconGrassTopSparseDirt;
         } else if (side > 1) {
            Icon betterGrassIcon = RenderBlocksUtils.getGrassTexture(this, blockAccess, x, y, z, side, this.iconGrassTopSparse);
            if (betterGrassIcon != null && betterGrassIcon != this.iconGrassTopSparse) {
               return betterGrassIcon;
            } else {
               return this.hasSnowOnTop ? this.iconSnowSide : this.blockIcon;
            }
         } else {
            return BTWBlocks.looseDirt.blockIcon;
         }
      } else {
         return this.getBlockTextureSecondPass(blockAccess, x, y, z, side);
      }
   }

   @Environment(EnvType.CLIENT)
   public Icon getBlockTextureSecondPass(IBlockAccess blockAccess, int x, int y, int z, int side) {
      Icon topIcon = this.iconGrassTopSparse;
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

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int itemDamage, float brightness) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, 0);
   }
}
