package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdStraight extends ComponentStronghold {
   private final EnumDoor doorType;
   private final boolean expandsX;
   private final boolean expandsZ;

   public ComponentStrongholdStraight(int var1, Random var2, StructureBoundingBox var3, int var4) {
      super(var1);
      this.coordBaseMode = var4;
      this.doorType = this.a(var2);
      this.boundingBox = var3;
      this.expandsX = var2.nextInt(2) == 0;
      this.expandsZ = var2.nextInt(2) == 0;
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      this.a((ComponentStrongholdStairs2)var1, var2, var3, 1, 1);
      if (this.expandsX) {
         this.b((ComponentStrongholdStairs2)var1, var2, var3, 1, 2);
      }

      if (this.expandsZ) {
         this.c((ComponentStrongholdStairs2)var1, var2, var3, 1, 2);
      }
   }

   public static ComponentStrongholdStraight findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, 7, var5);
      return a(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new ComponentStrongholdStraight(var6, var1, var7, var5) : null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if (this.a(var1, var3)) {
         return false;
      } else {
         this.a(var1, var3, 0, 0, 0, 4, 4, 6, true, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var2, var3, this.doorType, 1, 1, 0);
         this.a(var1, var2, var3, EnumDoor.OPENING, 1, 1, 6);
         this.a(var1, var3, var2, 0.1F, 1, 2, 1, Block.torchWood.blockID, 0);
         this.a(var1, var3, var2, 0.1F, 3, 2, 1, Block.torchWood.blockID, 0);
         this.a(var1, var3, var2, 0.1F, 1, 2, 5, Block.torchWood.blockID, 0);
         this.a(var1, var3, var2, 0.1F, 3, 2, 5, Block.torchWood.blockID, 0);
         if (this.expandsX) {
            this.a(var1, var3, 0, 1, 2, 0, 3, 4, 0, 0, false);
         }

         if (this.expandsZ) {
            this.a(var1, var3, 4, 1, 2, 4, 3, 4, 0, 0, false);
         }

         return true;
      }
   }
}
