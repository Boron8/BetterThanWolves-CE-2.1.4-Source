package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class StructureMineshaftPieces {
   private static final WeightedRandomChestContent[] mineshaftChestContents = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10),
      new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5),
      new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5),
      new WeightedRandomChestContent(Item.dyePowder.itemID, 4, 4, 9, 5),
      new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 2, 3),
      new WeightedRandomChestContent(Item.coal.itemID, 0, 3, 8, 10),
      new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15),
      new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 1),
      new WeightedRandomChestContent(Block.rail.blockID, 0, 4, 8, 1),
      new WeightedRandomChestContent(Item.melonSeeds.itemID, 0, 2, 4, 10),
      new WeightedRandomChestContent(Item.pumpkinSeeds.itemID, 0, 2, 4, 10)
   };

   private static StructureComponent getRandomComponent(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = var1.nextInt(100);
      if (var7 >= 80) {
         StructureBoundingBox var8 = ComponentMineshaftCross.findValidPlacement(var0, var1, var2, var3, var4, var5);
         if (var8 != null) {
            return new ComponentMineshaftCross(var6, var1, var8, var5);
         }
      } else if (var7 >= 70) {
         StructureBoundingBox var9 = ComponentMineshaftStairs.findValidPlacement(var0, var1, var2, var3, var4, var5);
         if (var9 != null) {
            return new ComponentMineshaftStairs(var6, var1, var9, var5);
         }
      } else {
         StructureBoundingBox var10 = ComponentMineshaftCorridor.findValidPlacement(var0, var1, var2, var3, var4, var5);
         if (var10 != null) {
            return new ComponentMineshaftCorridor(var6, var1, var10, var5);
         }
      }

      return null;
   }

   private static StructureComponent getNextMineShaftComponent(
      StructureComponent var0, List var1, Random var2, int var3, int var4, int var5, int var6, int var7
   ) {
      if (var7 > 8) {
         return null;
      } else if (Math.abs(var3 - var0.getBoundingBox().minX) <= 80 && Math.abs(var5 - var0.getBoundingBox().minZ) <= 80) {
         StructureComponent var8 = getRandomComponent(var1, var2, var3, var4, var5, var6, var7 + 1);
         if (var8 != null) {
            var1.add(var8);
            var8.buildComponent(var0, var1, var2);
         }

         return var8;
      } else {
         return null;
      }
   }
}
