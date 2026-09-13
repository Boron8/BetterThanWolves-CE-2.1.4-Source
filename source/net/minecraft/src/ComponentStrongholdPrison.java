package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdPrison extends ComponentStronghold {
   protected final EnumDoor doorType;

   public ComponentStrongholdPrison(int var1, Random var2, StructureBoundingBox var3, int var4) {
      super(var1);
      this.coordBaseMode = var4;
      this.doorType = this.a(var2);
      this.boundingBox = var3;
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      this.a((ComponentStrongholdStairs2)var1, var2, var3, 1, 1);
   }

   public static ComponentStrongholdPrison findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 9, 5, 11, var5);
      return a(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new ComponentStrongholdPrison(var6, var1, var7, var5) : null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if (this.a(var1, var3)) {
         return false;
      } else {
         this.a(var1, var3, 0, 0, 0, 8, 4, 10, true, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var2, var3, this.doorType, 1, 1, 0);
         this.a(var1, var3, 1, 1, 10, 3, 3, 10, 0, 0, false);
         this.a(var1, var3, 4, 1, 1, 4, 3, 1, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 4, 1, 3, 4, 3, 3, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 4, 1, 7, 4, 3, 7, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 4, 1, 9, 4, 3, 9, false, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var3, 4, 1, 4, 4, 3, 6, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
         this.a(var1, var3, 5, 1, 5, 7, 3, 5, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
         this.a(var1, Block.fenceIron.blockID, 0, 4, 3, 2, var3);
         this.a(var1, Block.fenceIron.blockID, 0, 4, 3, 8, var3);
         this.a(var1, Block.doorIron.blockID, this.c(Block.doorIron.blockID, 3), 4, 1, 2, var3);
         this.a(var1, Block.doorIron.blockID, this.c(Block.doorIron.blockID, 3) + 8, 4, 2, 2, var3);
         this.a(var1, Block.doorIron.blockID, this.c(Block.doorIron.blockID, 3), 4, 1, 8, var3);
         this.a(var1, Block.doorIron.blockID, this.c(Block.doorIron.blockID, 3) + 8, 4, 2, 8, var3);
         return true;
      }
   }
}
