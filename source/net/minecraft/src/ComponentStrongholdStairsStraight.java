package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdStairsStraight extends ComponentStronghold {
   private final EnumDoor doorType;

   public ComponentStrongholdStairsStraight(int var1, Random var2, StructureBoundingBox var3, int var4) {
      super(var1);
      this.coordBaseMode = var4;
      this.doorType = this.a(var2);
      this.boundingBox = var3;
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      this.a((ComponentStrongholdStairs2)var1, var2, var3, 1, 1);
   }

   public static ComponentStrongholdStairsStraight findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -7, 0, 5, 11, 8, var5);
      return a(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new ComponentStrongholdStairsStraight(var6, var1, var7, var5) : null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if (this.a(var1, var3)) {
         return false;
      } else {
         this.a(var1, var3, 0, 0, 0, 4, 10, 7, true, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var2, var3, this.doorType, 1, 7, 0);
         this.a(var1, var2, var3, EnumDoor.OPENING, 1, 1, 7);
         int var4 = this.c(Block.stairsCobblestone.blockID, 2);

         for (int var5 = 0; var5 < 6; var5++) {
            this.a(var1, Block.stairsCobblestone.blockID, var4, 1, 6 - var5, 1 + var5, var3);
            this.a(var1, Block.stairsCobblestone.blockID, var4, 2, 6 - var5, 1 + var5, var3);
            this.a(var1, Block.stairsCobblestone.blockID, var4, 3, 6 - var5, 1 + var5, var3);
            if (var5 < 5) {
               this.a(var1, Block.stoneBrick.blockID, 0, 1, 5 - var5, 1 + var5, var3);
               this.a(var1, Block.stoneBrick.blockID, 0, 2, 5 - var5, 1 + var5, var3);
               this.a(var1, Block.stoneBrick.blockID, 0, 3, 5 - var5, 1 + var5, var3);
            }
         }

         return true;
      }
   }
}
