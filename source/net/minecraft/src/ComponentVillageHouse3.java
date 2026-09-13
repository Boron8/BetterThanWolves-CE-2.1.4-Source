package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;

public class ComponentVillageHouse3 extends ComponentVillage {
   private int averageGroundLevel = -1;

   public ComponentVillageHouse3(
      ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5
   ) {
      super(par1ComponentVillageStartPiece, par2);
      this.coordBaseMode = par5;
      this.boundingBox = par4StructureBoundingBox;
   }

   public static ComponentVillageHouse3 func_74921_a(
      ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7
   ) {
      StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 9, 7, 12, par6);
      return a(var8) && StructureComponent.findIntersecting(par1List, var8) == null
         ? new ComponentVillageHouse3(par0ComponentVillageStartPiece, par7, par2Random, var8, par6)
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
      this.a(par1World, par3StructureBoundingBox, 2, 0, 5, 8, 0, 10, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 1, 7, 0, 4, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 8, 0, 0, 8, 3, 10, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 0, 7, 2, 0, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 0, 5, 2, 1, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 2, 0, 6, 2, 3, 10, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 3, 0, 10, 7, 3, 10, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 2, 0, 7, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 2, 5, 2, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 4, 1, 8, 4, 1, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 4, 4, 3, 4, 4, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 5, 2, 8, 5, 3, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, Block.planks.blockID, 0, 0, 4, 2, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 0, 4, 3, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 8, 4, 2, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 8, 4, 3, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 8, 4, 4, par3StructureBoundingBox);
      int var4 = this.c(Block.stairsWoodOak.blockID, 3);
      int var5 = this.c(Block.stairsWoodOak.blockID, 2);

      for (int var6 = -1; var6 <= 2; var6++) {
         for (int var7 = 0; var7 <= 8; var7++) {
            this.a(par1World, Block.stairsWoodOak.blockID, var4, var7, 4 + var6, var6, par3StructureBoundingBox);
            if ((var6 > -1 || var7 <= 1) && (var6 > 0 || var7 <= 3) && (var6 > 1 || var7 <= 4 || var7 >= 6)) {
               this.a(par1World, Block.stairsWoodOak.blockID, var5, var7, 4 + var6, 5 - var6, par3StructureBoundingBox);
            }
         }
      }

      this.a(par1World, par3StructureBoundingBox, 3, 4, 5, 3, 4, 10, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 7, 4, 2, 7, 4, 10, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 4, 5, 4, 4, 5, 10, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 6, 5, 4, 6, 5, 10, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 5, 6, 3, 5, 6, 10, Block.planks.blockID, Block.planks.blockID, false);
      int var12 = this.c(Block.stairsWoodOak.blockID, 0);

      for (int var7x = 4; var7x >= 1; var7x--) {
         this.a(par1World, Block.planks.blockID, 0, var7x, 2 + var7x, 7 - var7x, par3StructureBoundingBox);

         for (int var8 = 8 - var7x; var8 <= 10; var8++) {
            this.a(par1World, Block.stairsWoodOak.blockID, var12, var7x, 2 + var7x, var8, par3StructureBoundingBox);
         }
      }

      int var14 = this.c(Block.stairsWoodOak.blockID, 1);
      this.a(par1World, Block.planks.blockID, 0, 6, 6, 3, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 7, 5, 4, par3StructureBoundingBox);
      this.a(par1World, Block.stairsWoodOak.blockID, var14, 6, 6, 4, par3StructureBoundingBox);

      for (int var8 = 6; var8 <= 8; var8++) {
         for (int var9 = 5; var9 <= 10; var9++) {
            this.a(par1World, Block.stairsWoodOak.blockID, var14, var8, 12 - var8, var9, par3StructureBoundingBox);
         }
      }

      int iAbandonmentLevel = this.startPiece.getAbandonmentLevel(par1World);
      int iGlassBlockID = 0;
      if (iAbandonmentLevel == 0) {
         iGlassBlockID = Block.thinGlass.blockID;
      }

      this.a(par1World, Block.wood.blockID, 0, 0, 2, 1, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 0, 2, 4, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 0, 2, 2, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 0, 2, 3, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 4, 2, 0, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 5, 2, 0, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 6, 2, 0, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 8, 2, 1, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 8, 2, 2, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 8, 2, 3, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 8, 2, 4, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 8, 2, 5, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 8, 2, 6, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 8, 2, 7, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 8, 2, 8, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 8, 2, 9, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 2, 2, 6, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 2, 2, 7, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 2, 2, 8, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 2, 2, 9, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 4, 4, 10, par3StructureBoundingBox);
      this.a(par1World, iGlassBlockID, 0, 5, 4, 10, par3StructureBoundingBox);
      this.a(par1World, Block.wood.blockID, 0, 6, 4, 10, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 5, 5, 10, par3StructureBoundingBox);
      this.a(par1World, 0, 0, 2, 1, 0, par3StructureBoundingBox);
      this.a(par1World, 0, 0, 2, 2, 0, par3StructureBoundingBox);
      if (iAbandonmentLevel <= 1) {
         this.a(par1World, BTWBlocks.finiteUnlitTorch.blockID, 8, 2, 3, 1, par3StructureBoundingBox);
         this.a(par1World, par3StructureBoundingBox, par2Random, 2, 1, 0, this.c(BTWBlocks.woodenDoor.blockID, 1));
      }

      this.a(par1World, par3StructureBoundingBox, 1, 0, -1, 3, 2, -1, 0, 0, false);
      if (this.a(par1World, 2, 0, -1, par3StructureBoundingBox) == 0 && this.a(par1World, 2, -1, -1, par3StructureBoundingBox) != 0) {
         this.a(par1World, Block.stairsCobblestone.blockID, this.c(Block.stairsCobblestone.blockID, 3), 2, 0, -1, par3StructureBoundingBox);
      }

      for (int var16 = 0; var16 < 5; var16++) {
         for (int var9 = 0; var9 < 9; var9++) {
            this.b(par1World, var9, 7, var16, par3StructureBoundingBox);
            this.b(par1World, Block.cobblestone.blockID, 0, var9, -1, var16, par3StructureBoundingBox);
         }
      }

      for (int var17 = 5; var17 < 11; var17++) {
         for (int var9 = 2; var9 < 9; var9++) {
            this.b(par1World, var9, 7, var17, par3StructureBoundingBox);
            this.b(par1World, Block.cobblestone.blockID, 0, var9, -1, var17, par3StructureBoundingBox);
         }
      }

      this.a(par1World, par3StructureBoundingBox, 4, 1, 2, 2);
      return true;
   }
}
