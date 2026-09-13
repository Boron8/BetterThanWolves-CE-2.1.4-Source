package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;

public class ComponentVillageField2 extends ComponentVillage {
   private int averageGroundLevel = -1;
   private int secondaryCrop;

   public ComponentVillageField2(
      ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5
   ) {
      super(par1ComponentVillageStartPiece, par2);
      this.coordBaseMode = par5;
      this.boundingBox = par4StructureBoundingBox;
      this.secondaryCrop = this.pickRandomCrop(par3Random);
      this.secondaryCrop = this.pickRandomCrop(par3Random);
   }

   private int pickRandomCrop(Random par1Random) {
      switch (par1Random.nextInt(5)) {
         case 0:
            return Block.carrot.blockID;
         case 1:
            return Block.potato.blockID;
         default:
            return BTWBlocks.wheatCrop.blockID;
      }
   }

   public static ComponentVillageField2 func_74902_a(
      ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7
   ) {
      StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 7, 4, 9, par6);
      return a(var8) && StructureComponent.findIntersecting(par1List, var8) == null
         ? new ComponentVillageField2(par0ComponentVillageStartPiece, par7, par2Random, var8, par6)
         : null;
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      this.secondaryCrop = this.startPiece.getSecondaryCropBlockID(par1World);
      if (this.secondaryCrop == BTWBlocks.carrotCrop.blockID && par1World.rand.nextBoolean()) {
         this.secondaryCrop = BTWBlocks.floweringCarrotCrop.blockID;
      }

      if (this.averageGroundLevel < 0) {
         this.averageGroundLevel = this.b(par1World, par3StructureBoundingBox);
         if (this.averageGroundLevel < 0) {
            return true;
         }

         this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4 - 1, 0);
      }

      this.a(par1World, par3StructureBoundingBox, 0, 1, 0, 6, 4, 8, 0, 0, false);
      int iAbandonmentLevel = this.startPiece.getAbandonmentLevel(par1World);
      if (iAbandonmentLevel <= 1) {
         this.a(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, BTWBlocks.farmland.blockID, BTWBlocks.farmland.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, BTWBlocks.farmland.blockID, BTWBlocks.farmland.blockID, false);
      } else {
         this.a(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, BTWBlocks.looseDirt.blockID, BTWBlocks.looseDirt.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, BTWBlocks.looseDirt.blockID, BTWBlocks.looseDirt.blockID, false);
      }

      this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 0, 5, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 8, 5, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
      if (iAbandonmentLevel <= 1) {
         this.a(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
      }

      for (int var4 = 1; var4 <= 7; var4++) {
         int cropGrowth = MathHelper.getRandomIntegerInRange(par2Random, 2, 5) + 3;
         this.a(par1World, BTWBlocks.wheatCrop.blockID, cropGrowth, 1, 1, var4, par3StructureBoundingBox);
         if (cropGrowth == 7) {
            this.a(par1World, BTWBlocks.wheatCropTop.blockID, 3, 1, 2, var4, par3StructureBoundingBox);
         }

         cropGrowth = MathHelper.getRandomIntegerInRange(par2Random, 2, 5) + 3;
         this.a(par1World, BTWBlocks.wheatCrop.blockID, cropGrowth, 2, 1, var4, par3StructureBoundingBox);
         if (cropGrowth == 7) {
            this.a(par1World, BTWBlocks.wheatCropTop.blockID, 3, 2, 2, var4, par3StructureBoundingBox);
         }

         cropGrowth = MathHelper.getRandomIntegerInRange(par2Random, 2, 5) + 3;
         this.a(par1World, this.secondaryCrop, cropGrowth, 4, 1, var4, par3StructureBoundingBox);
         if (cropGrowth == 7 && this.secondaryCrop == BTWBlocks.wheatCrop.blockID) {
            this.a(par1World, BTWBlocks.wheatCropTop.blockID, 3, 4, 2, var4, par3StructureBoundingBox);
         }

         cropGrowth = MathHelper.getRandomIntegerInRange(par2Random, 2, 5) + 3;
         this.a(par1World, this.secondaryCrop, cropGrowth, 5, 1, var4, par3StructureBoundingBox);
         if (cropGrowth == 7 && this.secondaryCrop == BTWBlocks.wheatCrop.blockID) {
            this.a(par1World, BTWBlocks.wheatCropTop.blockID, 3, 5, 2, var4, par3StructureBoundingBox);
         }

         if (iAbandonmentLevel > 1) {
            this.a(par1World, 0, 0, 1, 1, var4, par3StructureBoundingBox);
            this.a(par1World, 0, 0, 2, 1, var4, par3StructureBoundingBox);
            this.a(par1World, 0, 0, 4, 1, var4, par3StructureBoundingBox);
            this.a(par1World, 0, 0, 5, 1, var4, par3StructureBoundingBox);
            this.a(par1World, 0, 0, 1, 2, var4, par3StructureBoundingBox);
            this.a(par1World, 0, 0, 2, 2, var4, par3StructureBoundingBox);
            this.a(par1World, 0, 0, 4, 2, var4, par3StructureBoundingBox);
            this.a(par1World, 0, 0, 5, 2, var4, par3StructureBoundingBox);
         } else if (iAbandonmentLevel == 1) {
            for (int iTempCount = 1; iTempCount <= 4; iTempCount += 3) {
               if (par1World.rand.nextInt(3) != 0) {
                  this.a(par1World, 0, 0, iTempCount, 1, var4, par3StructureBoundingBox);
                  this.a(par1World, 0, 0, iTempCount, 2, var4, par3StructureBoundingBox);
               }

               if (par1World.rand.nextInt(3) != 0) {
                  this.a(par1World, 0, 0, iTempCount + 1, 1, var4, par3StructureBoundingBox);
                  this.a(par1World, 0, 0, iTempCount + 1, 2, var4, par3StructureBoundingBox);
               }
            }
         }
      }

      for (int var8 = 0; var8 < 9; var8++) {
         for (int var5 = 0; var5 < 7; var5++) {
            this.b(par1World, var5, 4, var8, par3StructureBoundingBox);
            this.b(par1World, Block.dirt.blockID, 0, var5, -1, var8, par3StructureBoundingBox);
         }
      }

      return true;
   }
}
