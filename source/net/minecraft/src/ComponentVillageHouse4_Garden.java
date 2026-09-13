package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;

public class ComponentVillageHouse4_Garden extends ComponentVillage {
   private int averageGroundLevel = -1;
   private final boolean isRoofAccessible;

   public ComponentVillageHouse4_Garden(
      ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5
   ) {
      super(par1ComponentVillageStartPiece, par2);
      this.coordBaseMode = par5;
      this.boundingBox = par4StructureBoundingBox;
      this.isRoofAccessible = par3Random.nextBoolean();
   }

   public static ComponentVillageHouse4_Garden func_74912_a(
      ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7
   ) {
      StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 5, 6, 5, par6);
      return StructureComponent.findIntersecting(par1List, var8) != null
         ? null
         : new ComponentVillageHouse4_Garden(par0ComponentVillageStartPiece, par7, par2Random, var8, par6);
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if (this.averageGroundLevel < 0) {
         this.averageGroundLevel = this.b(par1World, par3StructureBoundingBox);
         if (this.averageGroundLevel < 0) {
            return true;
         }

         this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 6 - 1, 0);
      }

      this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 4, 0, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 4, 0, 4, 4, 4, Block.wood.blockID, Block.wood.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 4, 1, 3, 4, 3, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, Block.cobblestone.blockID, 0, 0, 1, 0, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 0, 2, 0, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 0, 3, 0, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 4, 1, 0, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 4, 2, 0, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 4, 3, 0, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 0, 1, 4, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 0, 2, 4, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 0, 3, 4, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 4, 1, 4, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 4, 2, 4, par3StructureBoundingBox);
      this.a(par1World, Block.cobblestone.blockID, 0, 4, 3, 4, par3StructureBoundingBox);
      this.a(par1World, par3StructureBoundingBox, 0, 1, 1, 0, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 4, 1, 1, 4, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 1, 4, 3, 3, 4, Block.planks.blockID, Block.planks.blockID, false);
      int iAbandonmentLevel = this.startPiece.getAbandonmentLevel(par1World);
      if (iAbandonmentLevel == 0) {
         this.a(par1World, Block.thinGlass.blockID, 0, 0, 2, 2, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 2, 2, 4, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 4, 2, 2, par3StructureBoundingBox);
      } else {
         this.a(par1World, 0, 0, 0, 2, 2, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 2, 2, 4, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 4, 2, 2, par3StructureBoundingBox);
      }

      this.a(par1World, Block.planks.blockID, 0, 1, 1, 0, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 1, 2, 0, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 1, 3, 0, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 2, 3, 0, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 3, 3, 0, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 3, 2, 0, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 3, 1, 0, par3StructureBoundingBox);
      if (this.a(par1World, 2, 0, -1, par3StructureBoundingBox) == 0 && this.a(par1World, 2, -1, -1, par3StructureBoundingBox) != 0) {
         this.a(par1World, Block.stairsCobblestone.blockID, this.c(Block.stairsCobblestone.blockID, 3), 2, 0, -1, par3StructureBoundingBox);
      }

      this.a(par1World, par3StructureBoundingBox, 1, 1, 1, 3, 3, 3, 0, 0, false);
      if (this.isRoofAccessible) {
         this.a(par1World, Block.fence.blockID, 0, 0, 5, 0, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 1, 5, 0, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 2, 5, 0, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 3, 5, 0, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 4, 5, 0, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 0, 5, 4, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 1, 5, 4, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 2, 5, 4, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 3, 5, 4, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 4, 5, 4, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 4, 5, 1, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 4, 5, 2, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 4, 5, 3, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 0, 5, 1, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 0, 5, 2, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 0, 5, 3, par3StructureBoundingBox);
      }

      if (this.isRoofAccessible) {
         int iFacing = this.c(Block.ladder.blockID, 3);
         int iMetadata = BTWBlocks.ladder.setFacing(0, iFacing);
         this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 3, 1, 3, par3StructureBoundingBox);
         this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 3, 2, 3, par3StructureBoundingBox);
         this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 3, 3, 3, par3StructureBoundingBox);
         this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 3, 4, 3, par3StructureBoundingBox);
      }

      if (this.startPiece.getAbandonmentLevel(par1World) <= 1) {
         this.a(par1World, BTWBlocks.finiteUnlitTorch.blockID, 8, 2, 3, 1, par3StructureBoundingBox);
      }

      for (int var4 = 0; var4 < 5; var4++) {
         for (int var5 = 0; var5 < 5; var5++) {
            this.b(par1World, var5, 6, var4, par3StructureBoundingBox);
            this.b(par1World, Block.cobblestone.blockID, 0, var5, -1, var4, par3StructureBoundingBox);
         }
      }

      this.a(par1World, par3StructureBoundingBox, 1, 1, 2, 1);
      return true;
   }
}
