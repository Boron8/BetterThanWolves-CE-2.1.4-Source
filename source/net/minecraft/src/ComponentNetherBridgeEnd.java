package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeEnd extends ComponentNetherBridgePiece {
   private int fillSeed;

   public ComponentNetherBridgeEnd(int var1, Random var2, StructureBoundingBox var3, int var4) {
      super(var1);
      this.coordBaseMode = var4;
      this.boundingBox = var3;
      this.fillSeed = var2.nextInt();
   }

   public static ComponentNetherBridgeEnd func_74971_a(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -3, 0, 5, 10, 8, var5);
      return a(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new ComponentNetherBridgeEnd(var6, var1, var7, var5) : null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      Random var4 = new Random(this.fillSeed);

      for (int var5 = 0; var5 <= 4; var5++) {
         for (int var6 = 3; var6 <= 4; var6++) {
            int var7 = var4.nextInt(8);
            this.a(var1, var3, var5, var6, 0, var5, var6, var7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
         }
      }

      int var8 = var4.nextInt(8);
      this.a(var1, var3, 0, 5, 0, 0, 5, var8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      var8 = var4.nextInt(8);
      this.a(var1, var3, 4, 5, 0, 4, 5, var8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

      for (int var10 = 0; var10 <= 4; var10++) {
         int var12 = var4.nextInt(5);
         this.a(var1, var3, var10, 2, 0, var10, 2, var12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      }

      for (int var11 = 0; var11 <= 4; var11++) {
         for (int var13 = 0; var13 <= 1; var13++) {
            int var14 = var4.nextInt(3);
            this.a(var1, var3, var11, var13, 0, var11, var13, var14, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
         }
      }

      return true;
   }
}
