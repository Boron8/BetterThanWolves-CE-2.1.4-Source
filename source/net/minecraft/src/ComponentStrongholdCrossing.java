package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdCrossing extends ComponentStronghold {
   protected final EnumDoor doorType;
   private boolean field_74996_b;
   private boolean field_74997_c;
   private boolean field_74995_d;
   private boolean field_74999_h;

   public ComponentStrongholdCrossing(int var1, Random var2, StructureBoundingBox var3, int var4) {
      super(var1);
      this.coordBaseMode = var4;
      this.doorType = this.a(var2);
      this.boundingBox = var3;
      this.field_74996_b = var2.nextBoolean();
      this.field_74997_c = var2.nextBoolean();
      this.field_74995_d = var2.nextBoolean();
      this.field_74999_h = var2.nextInt(3) > 0;
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      int var4 = 3;
      int var5 = 5;
      if (this.coordBaseMode == 1 || this.coordBaseMode == 2) {
         var4 = 8 - var4;
         var5 = 8 - var5;
      }

      this.a((ComponentStrongholdStairs2)var1, var2, var3, 5, 1);
      if (this.field_74996_b) {
         this.b((ComponentStrongholdStairs2)var1, var2, var3, var4, 1);
      }

      if (this.field_74997_c) {
         this.b((ComponentStrongholdStairs2)var1, var2, var3, var5, 7);
      }

      if (this.field_74995_d) {
         this.c((ComponentStrongholdStairs2)var1, var2, var3, var4, 1);
      }

      if (this.field_74999_h) {
         this.c((ComponentStrongholdStairs2)var1, var2, var3, var5, 7);
      }
   }

   public static ComponentStrongholdCrossing findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -4, -3, 0, 10, 9, 11, var5);
      return a(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new ComponentStrongholdCrossing(var6, var1, var7, var5) : null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if (this.a(var1, var3)) {
         return false;
      } else {
         this.a(var1, var3, 0, 0, 0, 9, 8, 10, true, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var2, var3, this.doorType, 4, 3, 0);
         if (this.field_74996_b) {
            this.a(var1, var3, 0, 3, 1, 0, 5, 3, 0, 0, false);
         }

         if (this.field_74995_d) {
            this.a(var1, var3, 9, 3, 1, 9, 5, 3, 0, 0, false);
         }

         if (this.field_74997_c) {
            this.a(var1, var3, 0, 5, 7, 0, 7, 9, 0, 0, false);
         }

         if (this.field_74999_h) {
            this.a(var1, var3, 9, 5, 7, 9, 7, 9, 0, 0, false);
         }

         this.a(var1, var3, 5, 1, 10, 7, 3, 10, 0, 0, false);
         this.a(var1, var3, 1, 2, 1, 8, 2, 6, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 4, 1, 5, 4, 4, 9, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 8, 1, 5, 8, 4, 9, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 1, 4, 7, 3, 4, 9, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 1, 3, 5, 3, 3, 6, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 1, 3, 4, 3, 3, 4, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
         this.a(var1, var3, 1, 4, 6, 3, 4, 6, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
         this.a(var1, var3, 5, 1, 7, 7, 1, 8, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 5, 1, 9, 7, 1, 9, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
         this.a(var1, var3, 5, 2, 7, 7, 2, 7, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
         this.a(var1, var3, 4, 5, 7, 4, 5, 9, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
         this.a(var1, var3, 8, 5, 7, 8, 5, 9, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
         this.a(var1, var3, 5, 5, 7, 7, 5, 9, Block.stoneDoubleSlab.blockID, Block.stoneDoubleSlab.blockID, false);
         this.a(var1, Block.torchWood.blockID, 0, 6, 5, 6, var3);
         return true;
      }
   }
}
