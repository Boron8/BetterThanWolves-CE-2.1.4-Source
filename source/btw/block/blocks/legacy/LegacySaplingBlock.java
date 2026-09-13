package btw.block.blocks.legacy;

import btw.block.BTWBlocks;
import btw.block.blocks.PlanterBlockBase;
import btw.crafting.util.FurnaceBurnTime;
import btw.util.ReflectionUtils;
import btw.world.feature.trees.BigTreeGenerator;
import btw.world.feature.trees.JungleTreeGenerator;
import btw.world.feature.trees.TreeUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSapling;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class LegacySaplingBlock extends BlockSapling {
   public static final String[] saplingTypes = new String[]{
      "oak",
      "spruce",
      "birch",
      "jungle",
      "oak",
      "spruce",
      "birch",
      "jungle",
      "oak",
      "spruce",
      "birch",
      "jungle",
      "oakMature",
      "spruceMature",
      "birchMature",
      "jungleMature"
   };
   private static final double WIDTH = 0.8;
   private static final double HALF_WIDTH = 0.4;
   @Environment(EnvType.CLIENT)
   private Icon[][] iconArray = new Icon[4][4];
   @Environment(EnvType.CLIENT)
   public static final String[] baseTextureNames = new String[]{
      "fcBlockSaplingOak_0", "fcBlockSaplingSpruce_0", "fcBlockSaplingBirch_0", "fcBlockSaplingJungle_0"
   };

   public LegacySaplingBlock(int iBlockID) {
      super(iBlockID);
      this.setFurnaceBurnTime(FurnaceBurnTime.KINDLING);
      this.setFilterableProperties(0);
      this.initBlockBounds(0.09999999999999998, 0.0, 0.09999999999999998, 0.9, 0.8, 0.9);
      this.a(null);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      this.e(world, i, j, k);
      if (world.provider.dimensionId != 1 && world.getBlockId(i, j, k) == this.blockID && world.getBlockLightValue(i, j + 1, k) >= 9) {
         this.attemptToGrow(world, i, j, k, random);
      }
   }

   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if (!ReflectionUtils.isObfuscated()) {
         if (!world.isRemote) {
            this.attemptToGrow(world, x, y, z, world.rand);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public int damageDropped(int metadata) {
      return metadata;
   }

   @Override
   public void growTree(World world, int x, int y, int z, Random random) {
      int treeType = world.getBlockMetadata(x, y, z) & 3;
      boolean success = false;
      int xOffset = 0;
      int zOffset = 0;
      boolean generatedHuge = false;
      boolean planter = Block.blocksList[world.getBlockId(x, y - 1, z)] instanceof PlanterBlockBase;
      if (treeType != 3) {
         world.setBlock(x, y, z, 0);
      }

      if (treeType == 1) {
         success = TreeUtils.generateTaiga2(world, random, x, y, z);
      } else if (treeType == 2) {
         success = TreeUtils.generateForest(world, random, x, y, z);
      } else if (treeType != 3) {
         if (random.nextInt(10) == 0) {
            BigTreeGenerator bigTree = new BigTreeGenerator(true);
            success = bigTree.a(world, random, x, y, z);
         } else {
            success = TreeUtils.generateTrees(world, random, x, y, z);
         }
      } else {
         while (true) {
            if (xOffset >= -1) {
               for (zOffset = 0; zOffset >= -1; zOffset--) {
                  if (this.d(world, x + xOffset, y, z + zOffset, 3)
                     && this.d(world, x + xOffset + 1, y, z + zOffset, 3)
                     && this.d(world, x + xOffset, y, z + zOffset + 1, 3)
                     && this.d(world, x + xOffset + 1, y, z + zOffset + 1, 3)) {
                     if (this.getSaplingGrowthStage(world, x + xOffset, y, z + zOffset) != 3
                        || this.getSaplingGrowthStage(world, x + xOffset + 1, y, z + zOffset) != 3
                        || this.getSaplingGrowthStage(world, x + xOffset, y, z + zOffset + 1) != 3
                        || this.getSaplingGrowthStage(world, x + xOffset + 1, y, z + zOffset + 1) != 3) {
                        return;
                     }

                     world.setBlock(x + xOffset, y, z + zOffset, 0);
                     world.setBlock(x + xOffset + 1, y, z + zOffset, 0);
                     world.setBlock(x + xOffset, y, z + zOffset + 1, 0);
                     world.setBlock(x + xOffset + 1, y, z + zOffset + 1, 0);
                     JungleTreeGenerator hugeTree = new JungleTreeGenerator(true, 10 + random.nextInt(20), 3, 3);
                     success = hugeTree.generate(world, random, x + xOffset, y, z + zOffset);
                     generatedHuge = true;
                     break;
                  }
               }

               if (!generatedHuge) {
                  xOffset--;
                  continue;
               }
            }

            if (!generatedHuge) {
               zOffset = 0;
               xOffset = 0;
               world.setBlock(x, y, z, 0);
               success = TreeUtils.generateTrees(world, random, x, y, z, 4 + random.nextInt(7), 3, 3, false);
            }
            break;
         }
      }

      if (!success) {
         int saplingMetadata = treeType + 12;
         if (generatedHuge) {
            world.setBlockAndMetadata(x + xOffset, y, z + zOffset, this.blockID, saplingMetadata);
            world.setBlockAndMetadata(x + xOffset + 1, y, z + zOffset, this.blockID, saplingMetadata);
            world.setBlockAndMetadata(x + xOffset, y, z + zOffset + 1, this.blockID, saplingMetadata);
            world.setBlockAndMetadata(x + xOffset + 1, y, z + zOffset + 1, this.blockID, saplingMetadata);
         } else {
            world.setBlockAndMetadata(x, y, z, this.blockID, saplingMetadata);
         }
      } else if (planter) {
         world.setBlockMetadata(x, y, z, treeType);
         world.playAuxSFX(2001, x, y - 1, z, BTWBlocks.planterWithSoil.blockID);
         world.setBlockAndMetadata(x, y - 1, z, Block.wood.blockID, treeType | 12);
      }
   }

   @Override
   public boolean onBlockSawed(World world, int i, int j, int k) {
      return false;
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      Block blockOn = Block.blocksList[world.getBlockId(i, j, k)];
      return blockOn != null && blockOn.canSaplingsGrowOnBlock(world, i, j, k);
   }

   public int getSaplingGrowthStage(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return (iMetadata & -4) >> 2;
   }

   public void attemptToGrow(World world, int x, int y, int z, Random rand) {
      int growthChance = 32;
      int blockIDBelow = world.getBlockId(x, y - 1, z);
      Block blockBelow = Block.blocksList[blockIDBelow];
      if (blockBelow.getIsFertilizedForPlantGrowth(world, x, y - 1, z)) {
         growthChance = (int)(growthChance / blockBelow.getPlantGrowthOnMultiplier(world, x, y - 1, z, this));
      }

      int metadata = world.getBlockMetadata(x, y, z);
      int growthStage = (metadata & -4) >> 2;
      if (growthStage == 3) {
         growthChance /= 2;
      }

      if (rand.nextInt(growthChance) == 0) {
         if (growthStage < 3) {
            metadata = metadata & 3 | ++growthStage << 2;
            world.setBlockMetadataWithNotify(x, y, z, metadata);
            if (growthStage == 3) {
               blockBelow.notifyOfFullStagePlantGrowthOn(world, x, y - 1, z, this);
            }
         } else if (!(blockBelow instanceof PlanterBlockBase) || blockBelow.getIsFertilizedForPlantGrowth(world, x, y - 1, z)) {
            this.growTree(world, x, y, z, rand);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      for (int iTempSaplingType = 0; iTempSaplingType < 4; iTempSaplingType++) {
         for (int iTempGrowthStage = 0; iTempGrowthStage < 4; iTempGrowthStage++) {
            this.iconArray[iTempSaplingType][iTempGrowthStage] = register.registerIcon(baseTextureNames[iTempSaplingType] + iTempGrowthStage);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iSaplingType = iMetadata & 3;
      int iGrowthStage = (iMetadata & -4) >> 2;
      return this.iconArray[iSaplingType][iGrowthStage];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 12));
      par3List.add(new ItemStack(par1, 1, 13));
      par3List.add(new ItemStack(par1, 1, 14));
      par3List.add(new ItemStack(par1, 1, 15));
   }
}
