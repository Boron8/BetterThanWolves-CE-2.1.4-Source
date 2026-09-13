package btw.block.blocks;

import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSilverfish;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class SilverfishBlock extends BlockSilverfish {
   private static final int GROW_FREQUENCY = 2;
   private static final int REPRODUCE_FREQUENCY = 10;
   protected final Block referenceBlock;
   protected final int referenceBlockMetadata;
   @Environment(EnvType.CLIENT)
   public Icon[] crackIcons;

   public SilverfishBlock(int iBlockID, Block refBlock, int refBlockMetadata) {
      super(iBlockID);
      this.c(1.5F);
      this.referenceBlock = refBlock;
      this.referenceBlockMetadata = refBlockMetadata;
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.b(true);
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (world.provider.dimensionId == 1) {
         int metadata = world.getBlockMetadata(x, y, z);
         if (metadata < 6) {
            if (rand.nextInt(2) == 0) {
               world.setBlockMetadataWithNotify(x, y, z, ++metadata, 3);
            }
         } else {
            int fishInside = 1 + metadata % 2;

            for (int cycle = 0; cycle < fishInside; cycle++) {
               if (rand.nextInt(10) == 0) {
                  EntitySilverfish silverfish = (EntitySilverfish)EntityList.createEntityOfType(EntitySilverfish.class, world);
                  int offset = rand.nextInt(6);
                  int targetX = x + Facing.offsetsXForSide[offset];
                  int targetY = y + Facing.offsetsYForSide[offset];
                  int targetZ = z + Facing.offsetsZForSide[offset];
                  int neighborblockid = world.getBlockId(targetX, targetY, targetZ);
                  int neighborblockmetadata = world.getBlockMetadata(targetX, targetY, targetZ);
                  if (neighborblockid != 0 && !Block.blocksList[neighborblockid].isAirBlock() && !Block.blocksList[neighborblockid].isGroundCover()) {
                     if (Block.blocksList[neighborblockid].isBlockInfestable(silverfish, neighborblockmetadata)) {
                        Block.blocksList[neighborblockid].onInfested(world, silverfish, targetX, targetY, targetZ, neighborblockmetadata);
                        fishInside--;
                     }
                  } else {
                     silverfish.b(targetX + 0.5, targetY, targetZ + 0.5, 0.0F, 0.0F);
                     world.spawnEntityInWorld(silverfish);
                     fishInside--;
                  }

                  if (metadata > 13 || fishInside > 1) {
                     world.playAuxSFX(2252, x, y, z, this.blockID + (metadata << 12));
                     world.setBlockWithNotify(x, y, z, 0);
                     int numsilverfish = 2;
                     if (metadata % 2 == 1) {
                        numsilverfish++;
                     }

                     for (int tempCount = 0; tempCount < numsilverfish; tempCount++) {
                        EntitySilverfish eruptionSilverfish = (EntitySilverfish)EntityList.createEntityOfType(EntitySilverfish.class, world);
                        eruptionSilverfish.b(x + 0.5, y, z + 0.5, 0.0F, 0.0F);
                        world.spawnEntityInWorld(eruptionSilverfish);
                     }

                     this.dropBlockAsItemWithChance(world, x, y, z, metadata, 1.0F, 0);
                     break;
                  }

                  int progress = 2;
                  if (metadata % 2 == 1) {
                     progress = 1;
                  } else if (fishInside > 0) {
                     progress = 3;
                  }

                  world.setBlockMetadataWithNotify(x, y, z, metadata + progress, 3);
               }
            }
         }
      }
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      return new ItemStack(this.referenceBlock, 1, this.referenceBlockMetadata);
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int iMetadata, float fChance, int iFortuneModifier) {
      for (int rubbleCount = 0; rubbleCount < 8; rubbleCount++) {
         if (rubbleCount * 2 <= iMetadata) {
            ItemUtils.dropSingleItemAsIfBlockHarvested(world, x, y, z, Item.clay.itemID, 0);
         } else {
            ItemUtils.dropSingleItemAsIfBlockHarvested(world, x, y, z, BTWItems.gravelPile.itemID, 0);
         }
      }

      ItemUtils.dropSingleItemAsIfBlockHarvested(world, x, y, z, BTWItems.gravelPile.itemID, 0);
   }

   @Override
   public boolean isBlockInfestedBy(EntityLiving entity) {
      return entity instanceof EntitySilverfish;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.crackIcons = new Icon[7];

      for (int iTempIndex = 0; iTempIndex < 7; iTempIndex++) {
         this.crackIcons[iTempIndex] = register.registerIcon("fcOverlayStoneRough_" + (iTempIndex + 1));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.referenceBlock.getIcon(iSide, this.referenceBlockMetadata);
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
   public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult) {
         IBlockAccess blockAccess = renderBlocks.blockAccess;
         int progress = blockAccess.getBlockMetadata(i, j, k) - 2;
         if (progress > 0) {
            int texindex = Math.floorDiv(progress, 2);
            Icon overlayTexture = this.crackIcons[texindex];
            if (overlayTexture != null) {
               this.renderBlockWithTexture(renderBlocks, i, j, k, overlayTexture);
            }
         }
      }
   }
}
