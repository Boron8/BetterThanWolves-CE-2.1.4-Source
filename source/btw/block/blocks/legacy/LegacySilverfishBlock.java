package btw.block.blocks.legacy;

import btw.item.util.ItemUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSilverfish;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LegacySilverfishBlock extends BlockSilverfish {
   private static final int HATCH_FREQUENCY = 1200;

   public LegacySilverfishBlock(int iBlockID) {
      super(iBlockID);
      this.c(1.5F);
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.b(true);
      this.a(null);
      this.c("monsterStoneEgg");
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (world.provider.dimensionId == 1 && rand.nextInt(1200) == 0) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         world.playAuxSFX(2252, i, j, k, this.blockID + (iMetadata << 12));
         world.setBlockWithNotify(i, j, k, 0);
         int iNumSilverfish = 1;
         if (rand.nextInt(2) == 0) {
            iNumSilverfish++;
         }

         for (int iTempCount = 0; iTempCount < iNumSilverfish; iTempCount++) {
            EntitySilverfish silverfish = (EntitySilverfish)EntityList.createEntityOfType(EntitySilverfish.class, world);
            silverfish.b(i + 0.5, j, k + 0.5, 0.0F, 0.0F);
            world.spawnEntityInWorld(silverfish);
         }

         ItemUtils.dropSingleItemAsIfBlockHarvested(world, i, j, k, Block.gravel.blockID, 0);
         ItemUtils.dropSingleItemAsIfBlockHarvested(world, i, j, k, Item.clay.itemID, 0);
      }
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      Block block = Block.stone;
      int iItemDamage = 0;
      if (iMetadata == 1) {
         block = Block.cobblestone;
      } else if (iMetadata == 2) {
         block = Block.stoneBrick;
      } else if (iMetadata == 14) {
         iItemDamage = 1;
      } else if (iMetadata == 15) {
         iItemDamage = 2;
      }

      return new ItemStack(block, 1, iItemDamage);
   }

   @Override
   public boolean hasStrata() {
      return true;
   }

   @Override
   public int getMetadataConversionForStrataLevel(int iLevel, int iMetadata) {
      if (iMetadata == 0) {
         if (iLevel == 1) {
            iMetadata = 14;
         } else if (iLevel == 2) {
            iMetadata = 15;
         }
      }

      return iMetadata;
   }

   public static int getMetadataConversionOnInfest(int iBlockID, int iMetadata) {
      int iNewMetadata = 0;
      if (iBlockID == Block.cobblestone.blockID) {
         iNewMetadata = 1;
      } else if (iBlockID == Block.stoneBrick.blockID) {
         iNewMetadata = 2;
      } else if (iMetadata == 1) {
         iNewMetadata = 14;
      } else if (iMetadata == 2) {
         iNewMetadata = 15;
      }

      return iNewMetadata;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      if (rand.nextInt(32) == 0) {
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "mob.silverfish.step", rand.nextFloat() * 0.05F + 0.2F, rand.nextFloat() * 1.0F + 0.5F, false);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iMetadata == 1) {
         return Block.cobblestone.getBlockTextureFromSide(iSide);
      } else if (iMetadata == 2) {
         return Block.stoneBrick.getBlockTextureFromSide(iSide);
      } else if (iMetadata == 14) {
         return Block.stone.getIcon(iSide, 1);
      } else {
         return iMetadata == 15 ? Block.stone.getIcon(iSide, 2) : Block.stone.getBlockTextureFromSide(iSide);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return renderer.renderStandardFullBlock(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesItemRenderAsBlock(int iItemDamage) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockMovedByPiston(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.renderStandardFullBlockMovedByPiston(this, i, j, k);
   }
}
