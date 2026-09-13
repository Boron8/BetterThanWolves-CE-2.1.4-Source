package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdRightTurn extends ComponentStrongholdLeftTurn {
   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      if (this.coordBaseMode != 2 && this.coordBaseMode != 3) {
         this.b((ComponentStrongholdStairs2)var1, var2, var3, 1, 1);
      } else {
         this.c((ComponentStrongholdStairs2)var1, var2, var3, 1, 1);
      }
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if (this.a(var1, var3)) {
         return false;
      } else {
         this.a(var1, var3, 0, 0, 0, 4, 4, 4, true, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var2, var3, this.doorType, 1, 1, 0);
         if (this.coordBaseMode != 2 && this.coordBaseMode != 3) {
            this.a(var1, var3, 0, 1, 1, 0, 3, 3, 0, 0, false);
         } else {
            this.a(var1, var3, 4, 1, 1, 4, 3, 3, 0, 0, false);
         }

         return true;
      }
   }
}
