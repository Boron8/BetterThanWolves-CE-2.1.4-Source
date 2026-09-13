package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdChestCorridor extends ComponentStronghold {
   private static final WeightedRandomChestContent[] strongholdChestContents = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.enderPearl.itemID, 0, 1, 1, 10),
      new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 3, 3),
      new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10),
      new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5),
      new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5),
      new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15),
      new WeightedRandomChestContent(Item.appleRed.itemID, 0, 1, 3, 15),
      new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.swordIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.plateIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.helmetIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.legsIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.bootsIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.appleGold.itemID, 0, 1, 1, 1)
   };
   private final EnumDoor doorType;
   private boolean hasMadeChest;

   public ComponentStrongholdChestCorridor(int var1, Random var2, StructureBoundingBox var3, int var4) {
      super(var1);
      this.coordBaseMode = var4;
      this.doorType = this.a(var2);
      this.boundingBox = var3;
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      this.a((ComponentStrongholdStairs2)var1, var2, var3, 1, 1);
   }

   public static ComponentStrongholdChestCorridor findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, 7, var5);
      return a(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new ComponentStrongholdChestCorridor(var6, var1, var7, var5) : null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if (this.a(var1, var3)) {
         return false;
      } else {
         this.a(var1, var3, 0, 0, 0, 4, 4, 6, true, var2, StructureStrongholdPieces.getStrongholdStones());
         this.a(var1, var2, var3, this.doorType, 1, 1, 0);
         this.a(var1, var2, var3, EnumDoor.OPENING, 1, 1, 6);
         this.a(var1, var3, 3, 1, 2, 3, 1, 4, Block.stoneBrick.blockID, Block.stoneBrick.blockID, false);
         this.a(var1, Block.stoneSingleSlab.blockID, 5, 3, 1, 1, var3);
         this.a(var1, Block.stoneSingleSlab.blockID, 5, 3, 1, 5, var3);
         this.a(var1, Block.stoneSingleSlab.blockID, 5, 3, 2, 2, var3);
         this.a(var1, Block.stoneSingleSlab.blockID, 5, 3, 2, 4, var3);

         for (int var4 = 2; var4 <= 4; var4++) {
            this.a(var1, Block.stoneSingleSlab.blockID, 5, 2, 1, var4, var3);
         }

         if (!this.hasMadeChest) {
            int var7 = this.a(2);
            int var5 = this.a(3, 3);
            int var6 = this.b(3, 3);
            if (var3.isVecInside(var5, var7, var6)) {
               this.hasMadeChest = true;
               this.a(
                  var1,
                  var3,
                  var2,
                  3,
                  2,
                  3,
                  WeightedRandomChestContent.func_92080_a(strongholdChestContents, Item.enchantedBook.func_92114_b(var2)),
                  2 + var2.nextInt(2)
               );
            }
         }

         return true;
      }
   }
}
