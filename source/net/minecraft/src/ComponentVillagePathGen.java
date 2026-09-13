package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillagePathGen extends ComponentVillageRoadPiece {
   private int averageGroundLevel;

   public ComponentVillagePathGen(ComponentVillageStartPiece var1, int var2, Random var3, StructureBoundingBox var4, int var5) {
      super(var1, var2);
      this.coordBaseMode = var5;
      this.boundingBox = var4;
      this.averageGroundLevel = Math.max(var4.getXSize(), var4.getZSize());
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      boolean var4 = false;

      for (int var5 = var3.nextInt(5); var5 < this.averageGroundLevel - 8; var5 += 2 + var3.nextInt(5)) {
         StructureComponent var6 = this.a((ComponentVillageStartPiece)var1, var2, var3, 0, var5);
         if (var6 != null) {
            var5 += Math.max(var6.boundingBox.getXSize(), var6.boundingBox.getZSize());
            var4 = true;
         }
      }

      for (int var7 = var3.nextInt(5); var7 < this.averageGroundLevel - 8; var7 += 2 + var3.nextInt(5)) {
         StructureComponent var8 = this.b((ComponentVillageStartPiece)var1, var2, var3, 0, var7);
         if (var8 != null) {
            var7 += Math.max(var8.boundingBox.getXSize(), var8.boundingBox.getZSize());
            var4 = true;
         }
      }

      if (var4 && var3.nextInt(3) > 0) {
         switch (this.coordBaseMode) {
            case 0:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, 1, this.c()
               );
               break;
            case 1:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.c()
               );
               break;
            case 2:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, 1, this.c()
               );
               break;
            case 3:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.c()
               );
         }
      }

      if (var4 && var3.nextInt(3) > 0) {
         switch (this.coordBaseMode) {
            case 0:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, 3, this.c()
               );
               break;
            case 1:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.c()
               );
               break;
            case 2:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, 3, this.c()
               );
               break;
            case 3:
               StructureVillagePieces.getNextStructureComponentVillagePath(
                  (ComponentVillageStartPiece)var1, var2, var3, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.c()
               );
         }
      }
   }

   public static StructureBoundingBox func_74933_a(ComponentVillageStartPiece var0, List var1, Random var2, int var3, int var4, int var5, int var6) {
      for (int var7 = 7 * MathHelper.getRandomIntegerInRange(var2, 3, 5); var7 >= 7; var7 -= 7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 3, 3, var7, var6);
         if (StructureComponent.findIntersecting(var1, var8) == null) {
            return var8;
         }
      }

      return null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      int var4 = this.d(Block.gravel.blockID, 0);

      for (int var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; var5++) {
         for (int var6 = this.boundingBox.minZ; var6 <= this.boundingBox.maxZ; var6++) {
            if (var3.isVecInside(var5, 64, var6)) {
               int var7 = var1.getTopSolidOrLiquidBlock(var5, var6) - 1;
               var1.setBlock(var5, var7, var6, var4, 0, 2);
            }
         }
      }

      return true;
   }
}
