package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.util.hardcorespawn.HardcoreSpawnUtils;
import java.util.Random;

public class ComponentScatteredFeatureDesertPyramid extends ComponentScatteredFeature {
   private boolean[] field_74940_h = new boolean[4];
   private static final WeightedRandomChestContent[] lootListArray = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.helmetGold.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.plateGold.itemID, 0, 1, 1, 2),
      new WeightedRandomChestContent(Item.legsGold.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.bootsGold.itemID, 0, 1, 1, 2),
      new WeightedRandomChestContent(Item.swordGold.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.emerald.itemID, 0, 1, 5, 15),
      new WeightedRandomChestContent(Item.bone.itemID, 0, 4, 6, 20),
      new WeightedRandomChestContent(Item.rottenFlesh.itemID, 0, 3, 7, 11),
      new WeightedRandomChestContent(Item.skull.itemID, 0, 1, 1, 5)
   };
   private static final WeightedRandomChestContent[] lootedLootListArray = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.bone.itemID, 0, 4, 6, 20),
      new WeightedRandomChestContent(Item.rottenFlesh.itemID, 0, 3, 7, 11),
      new WeightedRandomChestContent(Item.skull.itemID, 0, 1, 1, 5)
   };

   public ComponentScatteredFeatureDesertPyramid(Random par1Random, int par2, int par3) {
      super(par1Random, par2, 64, par3, 21, 15, 21);
   }

   @Override
   public boolean addComponentParts(World world, Random generatorRand, StructureBoundingBox boundingBox) {
      boolean bIsLooted = HardcoreSpawnUtils.isInLootedTempleRadius(world, boundingBox.getCenterX(), boundingBox.getCenterZ());
      this.a(
         world,
         boundingBox,
         0,
         -4,
         0,
         this.scatteredFeatureSizeX - 1,
         0,
         this.scatteredFeatureSizeZ - 1,
         Block.sandStone.blockID,
         Block.sandStone.blockID,
         false
      );

      for (int var4 = 1; var4 <= 9; var4++) {
         this.a(
            world,
            boundingBox,
            var4,
            var4,
            var4,
            this.scatteredFeatureSizeX - 1 - var4,
            var4,
            this.scatteredFeatureSizeZ - 1 - var4,
            Block.sandStone.blockID,
            Block.sandStone.blockID,
            false
         );
         this.a(world, boundingBox, var4 + 1, var4, var4 + 1, this.scatteredFeatureSizeX - 2 - var4, var4, this.scatteredFeatureSizeZ - 2 - var4, 0, 0, false);
      }

      for (int var15 = 0; var15 < this.scatteredFeatureSizeX; var15++) {
         for (int var5 = 0; var5 < this.scatteredFeatureSizeZ; var5++) {
            this.b(world, Block.sandStone.blockID, 0, var15, -5, var5, boundingBox);
         }
      }

      int var16 = this.c(Block.stairsSandStone.blockID, 3);
      int var5 = this.c(Block.stairsSandStone.blockID, 2);
      int var6 = this.c(Block.stairsSandStone.blockID, 0);
      int var7 = this.c(Block.stairsSandStone.blockID, 1);
      this.a(world, boundingBox, 0, 0, 0, 4, 9, 4, Block.sandStone.blockID, 0, false);
      this.a(world, boundingBox, 1, 10, 1, 3, 10, 3, Block.sandStone.blockID, Block.sandStone.blockID, false);
      this.a(world, Block.stairsSandStone.blockID, var16, 2, 10, 0, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var5, 2, 10, 4, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var6, 0, 10, 2, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var7, 4, 10, 2, boundingBox);
      this.a(world, boundingBox, this.scatteredFeatureSizeX - 5, 0, 0, this.scatteredFeatureSizeX - 1, 9, 4, Block.sandStone.blockID, 0, false);
      this.a(
         world,
         boundingBox,
         this.scatteredFeatureSizeX - 4,
         10,
         1,
         this.scatteredFeatureSizeX - 2,
         10,
         3,
         Block.sandStone.blockID,
         Block.sandStone.blockID,
         false
      );
      this.a(world, Block.stairsSandStone.blockID, var16, this.scatteredFeatureSizeX - 3, 10, 0, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var5, this.scatteredFeatureSizeX - 3, 10, 4, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var6, this.scatteredFeatureSizeX - 5, 10, 2, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var7, this.scatteredFeatureSizeX - 1, 10, 2, boundingBox);
      this.a(world, boundingBox, 8, 0, 0, 12, 4, 4, Block.sandStone.blockID, 0, false);
      this.a(world, boundingBox, 9, 1, 0, 11, 3, 4, 0, 0, false);
      this.a(world, Block.sandStone.blockID, 2, 9, 1, 1, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 9, 2, 1, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 9, 3, 1, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 10, 3, 1, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 11, 3, 1, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 11, 2, 1, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 11, 1, 1, boundingBox);
      this.a(world, boundingBox, 4, 1, 1, 8, 3, 3, Block.sandStone.blockID, 0, false);
      this.a(world, boundingBox, 4, 1, 2, 8, 2, 2, 0, 0, false);
      this.a(world, boundingBox, 12, 1, 1, 16, 3, 3, Block.sandStone.blockID, 0, false);
      this.a(world, boundingBox, 12, 1, 2, 16, 2, 2, 0, 0, false);
      this.a(
         world,
         boundingBox,
         5,
         4,
         5,
         this.scatteredFeatureSizeX - 6,
         4,
         this.scatteredFeatureSizeZ - 6,
         Block.sandStone.blockID,
         Block.sandStone.blockID,
         false
      );
      this.a(world, boundingBox, 9, 4, 9, 11, 4, 11, 0, 0, false);
      this.a(world, boundingBox, 8, 1, 8, 8, 3, 8, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(world, boundingBox, 12, 1, 8, 12, 3, 8, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(world, boundingBox, 8, 1, 12, 8, 3, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(world, boundingBox, 12, 1, 12, 12, 3, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(world, boundingBox, 1, 1, 5, 4, 4, 11, Block.sandStone.blockID, Block.sandStone.blockID, false);
      this.a(
         world,
         boundingBox,
         this.scatteredFeatureSizeX - 5,
         1,
         5,
         this.scatteredFeatureSizeX - 2,
         4,
         11,
         Block.sandStone.blockID,
         Block.sandStone.blockID,
         false
      );
      this.a(world, boundingBox, 6, 7, 9, 6, 7, 11, Block.sandStone.blockID, Block.sandStone.blockID, false);
      this.a(
         world,
         boundingBox,
         this.scatteredFeatureSizeX - 7,
         7,
         9,
         this.scatteredFeatureSizeX - 7,
         7,
         11,
         Block.sandStone.blockID,
         Block.sandStone.blockID,
         false
      );
      this.a(world, boundingBox, 5, 5, 9, 5, 7, 11, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(
         world,
         boundingBox,
         this.scatteredFeatureSizeX - 6,
         5,
         9,
         this.scatteredFeatureSizeX - 6,
         7,
         11,
         Block.sandStone.blockID,
         2,
         Block.sandStone.blockID,
         2,
         false
      );
      this.a(world, 0, 0, 5, 5, 10, boundingBox);
      this.a(world, 0, 0, 5, 6, 10, boundingBox);
      this.a(world, 0, 0, 6, 6, 10, boundingBox);
      this.a(world, 0, 0, this.scatteredFeatureSizeX - 6, 5, 10, boundingBox);
      this.a(world, 0, 0, this.scatteredFeatureSizeX - 6, 6, 10, boundingBox);
      this.a(world, 0, 0, this.scatteredFeatureSizeX - 7, 6, 10, boundingBox);
      this.a(world, boundingBox, 2, 4, 4, 2, 6, 4, 0, 0, false);
      this.a(world, boundingBox, this.scatteredFeatureSizeX - 3, 4, 4, this.scatteredFeatureSizeX - 3, 6, 4, 0, 0, false);
      this.a(world, Block.stairsSandStone.blockID, var16, 2, 4, 5, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var16, 2, 3, 4, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var16, this.scatteredFeatureSizeX - 3, 4, 5, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var16, this.scatteredFeatureSizeX - 3, 3, 4, boundingBox);
      this.a(world, boundingBox, 1, 1, 3, 2, 2, 3, Block.sandStone.blockID, Block.sandStone.blockID, false);
      this.a(
         world,
         boundingBox,
         this.scatteredFeatureSizeX - 3,
         1,
         3,
         this.scatteredFeatureSizeX - 2,
         2,
         3,
         Block.sandStone.blockID,
         Block.sandStone.blockID,
         false
      );
      this.a(world, Block.stairsSandStone.blockID, 0, 1, 1, 2, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, 0, this.scatteredFeatureSizeX - 2, 1, 2, boundingBox);
      this.a(world, Block.stoneSingleSlab.blockID, 1, 1, 2, 2, boundingBox);
      this.a(world, Block.stoneSingleSlab.blockID, 1, this.scatteredFeatureSizeX - 2, 2, 2, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var7, 2, 1, 2, boundingBox);
      this.a(world, Block.stairsSandStone.blockID, var6, this.scatteredFeatureSizeX - 3, 1, 2, boundingBox);
      this.a(world, boundingBox, 4, 3, 5, 4, 3, 18, Block.sandStone.blockID, Block.sandStone.blockID, false);
      this.a(
         world,
         boundingBox,
         this.scatteredFeatureSizeX - 5,
         3,
         5,
         this.scatteredFeatureSizeX - 5,
         3,
         17,
         Block.sandStone.blockID,
         Block.sandStone.blockID,
         false
      );
      this.a(world, boundingBox, 3, 1, 5, 4, 2, 16, 0, 0, false);
      this.a(world, boundingBox, this.scatteredFeatureSizeX - 6, 1, 5, this.scatteredFeatureSizeX - 5, 2, 16, 0, 0, false);

      for (int var10 = 5; var10 <= 17; var10 += 2) {
         this.a(world, Block.sandStone.blockID, 2, 4, 1, var10, boundingBox);
         this.a(world, Block.sandStone.blockID, 1, 4, 2, var10, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, this.scatteredFeatureSizeX - 5, 1, var10, boundingBox);
         this.a(world, Block.sandStone.blockID, 1, this.scatteredFeatureSizeX - 5, 2, var10, boundingBox);
      }

      this.a(world, Block.obsidian.blockID, 0, 10, 0, 7, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 10, 0, 8, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 9, 0, 9, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 11, 0, 9, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 8, 0, 10, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 12, 0, 10, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 7, 0, 10, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 13, 0, 10, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 9, 0, 11, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 11, 0, 11, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 10, 0, 12, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 10, 0, 13, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 10, 0, 10, boundingBox);

      for (int var18 = 0; var18 <= this.scatteredFeatureSizeX - 1; var18 += this.scatteredFeatureSizeX - 1) {
         this.a(world, Block.sandStone.blockID, 2, var18, 2, 1, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 2, 2, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 2, 3, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 3, 1, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 3, 2, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 3, 3, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 4, 1, boundingBox);
         this.a(world, Block.sandStone.blockID, 1, var18, 4, 2, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 4, 3, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 5, 1, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 5, 2, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 5, 3, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 6, 1, boundingBox);
         this.a(world, Block.sandStone.blockID, 1, var18, 6, 2, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 6, 3, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 7, 1, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 7, 2, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var18, 7, 3, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 8, 1, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 8, 2, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var18, 8, 3, boundingBox);
      }

      for (int var19 = 2; var19 <= this.scatteredFeatureSizeX - 3; var19 += this.scatteredFeatureSizeX - 3 - 2) {
         this.a(world, Block.sandStone.blockID, 2, var19 - 1, 2, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19, 2, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19 + 1, 2, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19 - 1, 3, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19, 3, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19 + 1, 3, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19 - 1, 4, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 1, var19, 4, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19 + 1, 4, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19 - 1, 5, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19, 5, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19 + 1, 5, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19 - 1, 6, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 1, var19, 6, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19 + 1, 6, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19 - 1, 7, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19, 7, 0, boundingBox);
         this.a(world, Block.obsidian.blockID, 0, var19 + 1, 7, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19 - 1, 8, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19, 8, 0, boundingBox);
         this.a(world, Block.sandStone.blockID, 2, var19 + 1, 8, 0, boundingBox);
      }

      this.a(world, boundingBox, 8, 4, 0, 12, 6, 0, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(world, 0, 0, 8, 6, 0, boundingBox);
      this.a(world, 0, 0, 12, 6, 0, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 9, 5, 0, boundingBox);
      this.a(world, Block.sandStone.blockID, 1, 10, 5, 0, boundingBox);
      this.a(world, Block.obsidian.blockID, 0, 11, 5, 0, boundingBox);
      this.a(world, boundingBox, 8, -14, 8, 12, -11, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(world, boundingBox, 8, -10, 8, 12, -10, 12, Block.sandStone.blockID, 1, Block.sandStone.blockID, 1, false);
      this.a(world, boundingBox, 8, -9, 8, 12, -9, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
      this.a(world, boundingBox, 8, -8, 8, 12, -1, 12, Block.sandStone.blockID, Block.sandStone.blockID, false);
      this.a(world, boundingBox, 9, -11, 9, 11, -1, 11, 0, 0, false);
      this.a(world, Block.pressurePlatePlanks.blockID, 0, 10, -11, 10, boundingBox);
      this.a(world, boundingBox, 9, -13, 9, 11, -13, 11, Block.tnt.blockID, 0, false);
      this.a(world, 0, 0, 8, -11, 10, boundingBox);
      this.a(world, 0, 0, 8, -10, 10, boundingBox);
      this.a(world, Block.sandStone.blockID, 1, 7, -10, 10, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 7, -11, 10, boundingBox);
      this.a(world, 0, 0, 12, -11, 10, boundingBox);
      this.a(world, 0, 0, 12, -10, 10, boundingBox);
      this.a(world, Block.sandStone.blockID, 1, 13, -10, 10, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 13, -11, 10, boundingBox);
      this.a(world, 0, 0, 10, -11, 8, boundingBox);
      this.a(world, 0, 0, 10, -10, 8, boundingBox);
      this.a(world, Block.sandStone.blockID, 1, 10, -10, 7, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 10, -11, 7, boundingBox);
      this.a(world, 0, 0, 10, -11, 12, boundingBox);
      this.a(world, 0, 0, 10, -10, 12, boundingBox);
      this.a(world, Block.sandStone.blockID, 1, 10, -10, 13, boundingBox);
      this.a(world, Block.sandStone.blockID, 2, 10, -11, 13, boundingBox);

      for (int var20 = 0; var20 < 4; var20++) {
         if (!this.field_74940_h[var20]) {
            int iXOffset = Direction.offsetX[var20] * 2;
            int iZOffset = Direction.offsetZ[var20] * 2;
            WeightedRandomChestContent[] lootList = lootListArray;
            int iNumItems = 2 + generatorRand.nextInt(5);
            if (bIsLooted) {
               lootList = lootedLootListArray;
               iNumItems /= 2;
            }

            WeightedRandomChestContent[] moddedLootList = WeightedRandomChestContent.func_92080_a(lootList, Item.enchantedBook.func_92114_b(generatorRand));
            this.field_74940_h[var20] = this.a(world, boundingBox, generatorRand, 10 + iXOffset, -11, 10 + iZOffset, moddedLootList, iNumItems);
         }
      }

      if (bIsLooted) {
         this.a(world, boundingBox, 9, 0, 9, 10, 0, 10, 0, 0, false);
         this.a(world, boundingBox, 9, -13, 9, 11, -11, 11, 0, 0, false);
         int iLadderFacing = this.c(Block.ladder.blockID, 5);
         int iLadderMetadata = BTWBlocks.ladder.setFacing(0, iLadderFacing);

         for (int iTempY = -13; iTempY <= 0; iTempY++) {
            this.a(world, BTWBlocks.ladder.blockID, iLadderMetadata, 9, iTempY, 9, boundingBox);
         }
      } else {
         this.a(world, Block.enchantmentTable.blockID, 0, 10, 1, 10, boundingBox);
      }

      return true;
   }
}
