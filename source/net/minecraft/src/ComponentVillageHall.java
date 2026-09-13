package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;

public class ComponentVillageHall extends ComponentVillage {
   private int averageGroundLevel = -1;

   public ComponentVillageHall(
      ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5
   ) {
      super(par1ComponentVillageStartPiece, par2);
      this.coordBaseMode = par5;
      this.boundingBox = par4StructureBoundingBox;
   }

   public static ComponentVillageHall func_74906_a(
      ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7
   ) {
      StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 9, 7, 11, par6);
      return a(var8) && StructureComponent.findIntersecting(par1List, var8) == null
         ? new ComponentVillageHall(par0ComponentVillageStartPiece, par7, par2Random, var8, par6)
         : null;
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if (this.averageGroundLevel < 0) {
         this.averageGroundLevel = this.b(par1World, par3StructureBoundingBox);
         if (this.averageGroundLevel < 0) {
            return true;
         }

         this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7 - 1, 0);
      }

      this.a(par1World, par3StructureBoundingBox, 1, 1, 1, 7, 4, 4, 0, 0, false);
      this.a(par1World, par3StructureBoundingBox, 2, 1, 6, 8, 4, 10, 0, 0, false);
      this.a(par1World, par3StructureBoundingBox, 2, 0, 6, 8, 0, 10, Block.dirt.blockID, Block.dirt.blockID, false);
      this.a(par1World, Block.cobblestone.blockID, 0, 6, 0, 6, par3StructureBoundingBox);
      this.a(par1World, par3StructureBoundingBox, 2, 1, 6, 2, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 8, 1, 6, 8, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 3, 1, 10, 7, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 1, 7, 0, 4, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 8, 0, 0, 8, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 0, 7, 1, 0, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 5, 7, 1, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 2, 0, 7, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 2, 5, 7, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 4, 1, 8, 4, 1, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 4, 4, 8, 4, 4, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 5, 2, 8, 5, 3, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, Block.planks.blockID, 0, 0, 4, 2, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 0, 4, 3, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 8, 4, 2, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 8, 4, 3, par3StructureBoundingBox);
      int var4 = this.c(Block.stairsWoodOak.blockID, 3);
      int var5 = this.c(Block.stairsWoodOak.blockID, 2);

      for (int var6 = -1; var6 <= 2; var6++) {
         for (int var7 = 0; var7 <= 8; var7++) {
            this.a(par1World, Block.stairsWoodOak.blockID, var4, var7, 4 + var6, var6, par3StructureBoundingBox);
            this.a(par1World, Block.stairsWoodOak.blockID, var5, var7, 4 + var6, 5 - var6, par3StructureBoundingBox);
         }
      }

      this.a(par1World, Block.wood.blockID, 0, 0, 2, 1, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 0, 2, 4, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 8, 2, 1, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 8, 2, 4, par3StructureBoundingBox);
      int iAbandonmentLevel = this.startPiece.getAbandonmentLevel(par1World);
      if (iAbandonmentLevel == 0) {
         this.a(par1World, Block.thinGlass.blockID, 0, 0, 2, 2, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 0, 2, 3, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 8, 2, 2, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 8, 2, 3, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 2, 2, 5, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 3, 2, 5, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 5, 2, 0, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 6, 2, 5, par3StructureBoundingBox);
      } else {
         this.a(par1World, 0, 0, 0, 2, 2, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 0, 2, 3, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 8, 2, 2, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 8, 2, 3, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 2, 2, 5, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 3, 2, 5, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 5, 2, 0, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 6, 2, 5, par3StructureBoundingBox);
      }

      this.a(par1World, BTWBlocks.oakWoodMouldingAndDecorative.blockID, 15, 2, 1, 3, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 1, 1, 4, par3StructureBoundingBox);
      this.a(par1World, Block.stairsWoodOak.blockID, this.c(Block.stairsWoodOak.blockID, 3), 2, 1, 4, par3StructureBoundingBox);
      this.a(par1World, Block.stairsWoodOak.blockID, this.c(Block.stairsWoodOak.blockID, 1), 1, 1, 3, par3StructureBoundingBox);
      this.a(par1World, par3StructureBoundingBox, 5, 0, 1, 7, 0, 3, Block.stoneDoubleSlab.blockID, Block.stoneDoubleSlab.blockID, false);
      this.a(par1World, Block.stoneDoubleSlab.blockID, 0, 6, 1, 1, par3StructureBoundingBox);
      this.a(par1World, Block.stoneDoubleSlab.blockID, 0, 6, 1, 2, par3StructureBoundingBox);
      this.a(par1World, 0, 0, 2, 1, 0, par3StructureBoundingBox);
      this.a(par1World, 0, 0, 2, 2, 0, par3StructureBoundingBox);
      if (iAbandonmentLevel <= 1) {
         this.a(par1World, BTWBlocks.finiteUnlitTorch.blockID, 8, 2, 3, 1, par3StructureBoundingBox);
         this.a(par1World, par3StructureBoundingBox, par2Random, 2, 1, 0, this.c(BTWBlocks.woodenDoor.blockID, 1));
      }

      if (this.a(par1World, 2, 0, -1, par3StructureBoundingBox) == 0 && this.a(par1World, 2, -1, -1, par3StructureBoundingBox) != 0) {
         this.a(par1World, Block.stairsCobblestone.blockID, this.c(Block.stairsCobblestone.blockID, 3), 2, 0, -1, par3StructureBoundingBox);
      }

      this.a(par1World, 0, 0, 6, 1, 5, par3StructureBoundingBox);
      this.a(par1World, 0, 0, 6, 2, 5, par3StructureBoundingBox);
      if (iAbandonmentLevel <= 1) {
         this.a(par1World, BTWBlocks.finiteUnlitTorch.blockID, 8, 6, 3, 4, par3StructureBoundingBox);
         this.a(par1World, par3StructureBoundingBox, par2Random, 6, 1, 5, this.c(BTWBlocks.woodenDoor.blockID, 1));
      }

      for (int var9 = 0; var9 < 5; var9++) {
         for (int var7 = 0; var7 < 9; var7++) {
            this.b(par1World, var7, 7, var9, par3StructureBoundingBox);
            this.b(par1World, Block.cobblestone.blockID, 0, var7, -1, var9, par3StructureBoundingBox);
         }
      }

      this.a(par1World, par3StructureBoundingBox, 4, 1, 2, 2);
      return true;
   }

   @Override
   protected int getVillagerType(int par1) {
      return par1 == 0 ? 4 : 0;
   }
}
