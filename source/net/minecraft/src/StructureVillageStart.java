package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

class StructureVillageStart extends StructureStart {
   private boolean hasMoreThanTwoComponents = false;

   public StructureVillageStart(World var1, Random var2, int var3, int var4, int var5) {
      ArrayList var6 = StructureVillagePieces.getStructureVillageWeightedPieceList(var2, var5);
      ComponentVillageStartPiece var7 = new ComponentVillageStartPiece(var1.getWorldChunkManager(), 0, var2, (var3 << 4) + 2, (var4 << 4) + 2, var6, var5);
      this.components.add(var7);
      var7.a(var7, this.components, var2);
      ArrayList var8 = var7.field_74930_j;
      ArrayList var9 = var7.field_74932_i;

      while (!var8.isEmpty() || !var9.isEmpty()) {
         if (var8.isEmpty()) {
            int var10 = var2.nextInt(var9.size());
            StructureComponent var11 = (StructureComponent)var9.remove(var10);
            var11.buildComponent(var7, this.components, var2);
         } else {
            int var13 = var2.nextInt(var8.size());
            StructureComponent var15 = (StructureComponent)var8.remove(var13);
            var15.buildComponent(var7, this.components, var2);
         }
      }

      this.c();
      int var14 = 0;

      for (StructureComponent var12 : this.components) {
         if (!(var12 instanceof ComponentVillageRoadPiece)) {
            var14++;
         }
      }

      this.hasMoreThanTwoComponents = var14 > 2;
   }

   @Override
   public boolean isSizeableStructure() {
      return this.hasMoreThanTwoComponents;
   }
}
